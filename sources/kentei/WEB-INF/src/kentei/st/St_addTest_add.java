package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import kentei.beans.*;
import kentei.exception.TimeoutException;

/**
 * 取得可能実施日参照機能.
 * <ul>
 * <li>検定実施日選択画面(st_addtest_insert.jsp)から受け取った情報をもとに取得検定を登録する
 * <li>検定実施日選択画面(st_addtest)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class St_addTest_add extends HttpServlet{

	/**
	 * <ul>
	 * <li>検定番号、検定実施日を受け取る
	 * <li>入力された検定番号、検定実施日をもとにデータベースに登録する
	 * <li>取得検定参照画面を表示する
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
		String returnURL = "/";		//戻り先URL
		String set_test_no = request.getParameter("test_no");		//POSTされた検定番号
		String set_test_perform_date = request.getParameter("test_perform_date");	//POSTされた検定実施日

		String gettestSQL = "insert into gettest values(?,?,?)";		//検定実施日登録
		
		try{
			//セッション有効可否確認
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");
			if(session==null || user==null){
				//セッションが無効である場合エラー表示
				throw new TimeoutException("セッションがタイムアウトしました。");
			}else if(user.getLoginKubun()!=0){
				//ログイン区分が学生でない場合エラー表示
				throw new TimeoutException("アクセス権限がありません。");
			}
			
			//DB接続の準備
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			
			//取得検定を追加する
			stmt = conn.prepareStatement(gettestSQL);
			stmt.setString(1,user.getId());
			stmt.setString(2,set_test_no);
			stmt.setString(3,set_test_perform_date);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","追加しました。");
			
			//戻り先URLの設定
			returnURL = "./st_addtest";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./st_addtest";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//取得検定参照画面を表示する
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