package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

/**
 * 検定実施日登録機能.
 * <ul>
 * <li>検定実施日登録・削除画面(mg_testdate_insert.jsp)からPOSTされた情報をもとに検定実施日を追加する
 * <li>検定実施日登録・削除画面(mg_testdate_get)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_Testdate_add extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>検定実施日、検定取得日、検定番号を受け取る
	 * <li>データベースに検定実施日を追加する
	 * <li>検定実施日登録・削除画面を表示する
	 * </ul>
	 * 
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @exception ServletException 既定のエラーページを表示
	 * @exception IOException 既定のエラーページを表示
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		
		request.setCharacterEncoding("utf-8");
		
		//変数の準備
		Connection conn = null;
		RequestDispatcher dispatcher = null;
		String returnURL = "/";
		String test_perform_date = request.getParameter("perform_date");	//POSTされた検定実施日
		String test_get_date = request.getParameter("get_date");			//POSTされた検定取得日
		String test_no = request.getParameter("test_no");					//POSTされた検定番号
		
		String testdateSQL = "insert into testdate values(?,?,?)";		//検定実施日追加
		
		try{
			//セッション有効可否確認
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");
			if(session==null || user==null){
				//セッションが無効である場合エラー表示
				throw new TimeoutException("セッションがタイムアウトしました。");
			}else if(user.getLoginKubun()!=1){
				//ログイン区分が管理者でない場合エラー表示
				throw new TimeoutException("アクセス権限がありません。");
			}
			
			//DB接続の準備
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			
			//データベースに検定実施日を追加する
			stmt = conn.prepareStatement(testdateSQL);
			stmt.setString(1,test_no);
			stmt.setString(2,test_perform_date);
			stmt.setString(3,test_get_date);
			stmt.executeUpdate();
			stmt.close();

			request.setAttribute("msg","登録しました。");
			
			//戻り先URLの指定
			returnURL = "./mg_testdate_get?test_no=" + test_no;		
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";	
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./mg_testdate_get?test_no=" + test_no;	
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//検定実施日登録・削除画面を表示する
			dispatcher = request.getRequestDispatcher(returnURL);
			dispatcher.forward(request,response);
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException e){
				throw new ServletException(e);
			}
		}
	}
}