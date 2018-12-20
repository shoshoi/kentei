package kentei;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

/**
 * パスワード変更機能.
 * <ul>
 * <li>パスワード変更画面(st_mainteuser,mg_mainteuser)からPOSTされた情報をもとにアカウントを検証する
 * <li>新しいパスワードをDBに格納する
 * <li>パスワード変更画面(st_mainteuser,mg_mainteuser)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class MainteUser_update extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>ユーザーID、パスワードを受け取る
	 * <li>データベースのユーザー情報を参照する
	 * <li>データベースのユーザーパスワードを更新する
	 * <li>メッセージと共にパスワード変更画面を表示する
	 * </ul>
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
		
		String returnURL = "/";	//戻り先URL
		String msg="";			//操作成功可否メッセージ
		String user_id;			//ユーザーID
		int loginKubun;			//ログイン区分(0=学生,1=管理者)
		String now_pass = request.getParameter("now_pass");		//ポストされた変更前パスワード
		String new_pass = request.getParameter("new_pass");		//ポストされた変更後パスワード
		String stSQL_select = "SELECT * FROM student WHERE st_no = ? AND st_pass = password(?)";	//学生情報参照
		String mgSQL_select = "SELECT * FROM manager WHERE mg_no = ? AND mg_pass = password(?)";	//管理者情報参照
		String stSQL_update = "UPDATE student SET st_pass = password(?) where st_no = ?";			//学生パスワード更新
		String mgSQL_update = "UPDATE manager SET mg_pass = password(?) where mg_no = ?";			//管理者パスワード更新
		
		try{
			//ログインユーザー情報保持
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");
			if(session==null || user==null){
				//セッションが無効である場合エラー表示
				throw new TimeoutException("セッションがタイムアウトしました。");
			}
			user_id = user.getId();
			loginKubun = user.getLoginKubun();
			
			//データベースのユーザー情報を参照する
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			if(loginKubun == 0){
				//学生情報保持
				stmt = conn.prepareStatement(stSQL_select);
				returnURL = "./st_mainteuser";

			}else{
				//管理者情報保持
				stmt = conn.prepareStatement(mgSQL_select);
				returnURL = "./mg_mainteuser";
			}
				
			stmt.setString(1,user_id);
			stmt.setString(2,now_pass);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				//データベースのユーザーパスワードを更新する
				stmt.close();
				if(loginKubun == 0){
					//学生パスワード更新
					stmt = conn.prepareStatement(stSQL_update);
				}else{
					//管理者パスワード更新
					stmt = conn.prepareStatement(mgSQL_update);
				}
				stmt.setString(1,new_pass);
				stmt.setString(2,user_id);
				stmt.executeUpdate();
				msg="更新しました。";
			}else{
				msg="パスワードが間違っています。";
			}
			stmt.close();
			rs.close();
			
			request.setAttribute("msg",msg);
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//メッセージと共にパスワード変更画面を表示する
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