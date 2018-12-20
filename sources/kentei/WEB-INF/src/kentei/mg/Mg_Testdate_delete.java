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
 * 検定実施日削除機能.
 * <ul>
 * <li>検定実施日登録・削除画面(mg_testdate_insert.jsp)からPOSTされた情報をもとに検定実施日を削除する
 * <li>検定実施日登録・削除画面(mg_testdate_get)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")

public class Mg_Testdate_delete extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>検定番号、検定実施日を受け取る
	 * <li>データベースから該当する検定実施日を削除する
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
		String returnURL = "/";			//戻り先URL
		String test_no = request.getParameter("test_no");		//POSTされた検定番号
		String remdate = request.getParameter("remdate");		//POSTされた検定実施日
		
		String testdateSQL = "DELETE FROM testdate where test_no = ? AND test_perform_date = ?";	//検定実施日削除
		
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
			
			//データベースから検定実施日を削除する
			stmt = conn.prepareStatement(testdateSQL);
			stmt.setString(1,test_no);
			stmt.setString(2,remdate);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","削除しました。");
			
			//戻り先URLの設定
			returnURL = "./mg_testdate_get?test_no=" + test_no;
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/ndex.jsp";
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