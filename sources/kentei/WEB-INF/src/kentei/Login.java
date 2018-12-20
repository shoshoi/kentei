package kentei;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;

/**
 * ログイン機能.
 * <ul>
 * <li>ログイン画面(login.jsp)からPOSTされた情報をもとにアカウントを検証する
 * <li>ユーザ情報をセッションに格納する
 * <li>メニュー画面(sg_main, mg_main)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Login extends HttpServlet{

	/**
	 * <ul>
	 * <li>ユーザーID、パスワードを受け取る
	 * <li>データベースのユーザー情報を参照する
	 * <li>データベースのユーザー情報をセッションに格納する
	 * <li>ユーザー情報の照会に成功した場合メインメニューを表示、それ以外の場合はメッセージと共にログイン画面を再表示
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
		
		String returnURL = "/";		//戻り先URL
		String user_id = null;				//ユーザーID
		String user_name = null;			//ユーザー名
		String stSQL = "SELECT * FROM student WHERE st_no = ? AND st_pass = password(?)";	//学生情報参照
		String mgSQL = "SELECT * FROM manager WHERE mg_no = ? AND mg_pass = password(?)";	//管理者情報参照
		User user = null;
		String requestId = request.getParameter("id");							//POSTされたユーザー名
		String requestPass = request.getParameter("pass");						//POSTされたパスワード
		int loginKubun;
		
		try{
			//データベースのユーザー情報を参照する
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			stmt = conn.prepareStatement(stSQL);
			stmt.setString(1,requestId);
			stmt.setString(2,requestPass);
			rs = stmt.executeQuery();

			if(rs.next()){
				//学生ログイン成功
				user_id = rs.getString("st_no");
				user_name = rs.getString("st_name");
				loginKubun = 0;
				user = new User(user_id,user_name,loginKubun);
				returnURL = "/st_main";
			}else{
				//学生テーブルと一致しない場合、管理者テーブルを参照
				rs.close();
				stmt.close();
				stmt = conn.prepareStatement(mgSQL);
				stmt.setString(1,requestId);
				stmt.setString(2,requestPass);
				rs = stmt.executeQuery();
				if(rs.next()){
					//管理者ログイン成功
					user_id = rs.getString("mg_no");
					user_name = rs.getString("mg_name");
					loginKubun = 1;
					user = new User(user_id,user_name,loginKubun);
					returnURL = "/mg_main";
				}else{
					//ログイン失敗
					request.setAttribute("msg","ユーザＩＤまたはパスワードが間違っています");
					returnURL = "/index.jsp";
				}
			}
			rs.close();
			stmt.close();
			
			if(user!=null){
				//ログインに成功した場合、データベースのユーザー情報をセッションに格納する
				HttpSession session = request.getSession(false);
				if(session != null){
					session.invalidate();
				}
				session = request.getSession(true);
				session.setAttribute("user",user);
			}
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "/index.jsp";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//ユーザー情報の照会に成功した場合メインメニューを表示、それ以外の場合はメッセージと共にログイン画面を再表示
			dispatcher = request.getRequestDispatcher(returnURL);
			dispatcher.forward(request,response);
			try{
				if(conn != null){
					conn.close();
				}
			}catch(Exception e){
				throw new ServletException(e);
			}
		}
	}
}
