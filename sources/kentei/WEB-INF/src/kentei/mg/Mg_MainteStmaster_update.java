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
 * 学生情報更新機能.
 * <ul>
 * <li>学生マスタメンテナンス画面(mg_maintestmaster_update.jsp)から指定された情報をもとに学生個人の情報を更新する
 * <li>学生情報更新画面(/mg_maintestmaster_get)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class Mg_MainteStmaster_update extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>POSTされた情報を元に学生情報を更新する
	 * <li>パスワード初期化フラグが指定されている場合、パスワードを初期化する
	 * <li>学生情報更新画面を表示する
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
		String st_no = request.getParameter("st_no");		//POSTされた学生番号
		String st_name = request.getParameter("st_name");	//POSTされた学生名
		String st_kana = request.getParameter("st_kana");	//POSTされた学生名カナ
		String class_no = request.getParameter("class_no");	//POSTされた学科番号
		String pass = request.getParameter("pass");			//POSTされたパスワード初期化フラグ(true:パスワード初期化)
		String year = request.getParameter("year");			//POSTされた学年
		
		String stSQL = "update student set " +			//学生情報更新
				"st_name = ?," +
				"st_kana = ?," +
				"class_no = ?," +
				"year = ?" +
				"where st_no = ?";
		
		String stSQL_passwd = "update student set " +	//パスワードを初期化し学生情報更新
				"st_name = ?," +
				"st_kana = ?," +
				"class_no = ?," +
				"year = ?," +
				"st_pass = password(?) " + 
				"where st_no = ?";
	
		String stSQL_birthday = "select birthday from student where st_no = ?";	//学生生年月日取得
	
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
			
			//データベースの学生情報を更新する
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;

			if(pass.equals("false")){
				//学生情報更新
				stmt = conn.prepareStatement(stSQL);
				stmt.setString(1,st_name);
				stmt.setString(2,st_kana);
				stmt.setString(3,class_no);
				stmt.setString(4,year);
				stmt.setString(5,st_no);
				stmt.executeUpdate();
			}else{
				//学生情報更新、パスワードを初期化
				stmt = conn.prepareStatement(stSQL_birthday);
				stmt.setString(1,st_no);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()){
					//誕生年月日(西暦、8ケタ)をパスワードとし学生情報を更新する
					String defaultpass = rs.getString("birthday").replaceAll("-", "");
					stmt.close();
					stmt = conn.prepareStatement(stSQL_passwd);
					stmt.setString(1,st_name);
					stmt.setString(2,st_kana);
					stmt.setString(3,class_no);
					stmt.setString(4,year);
					stmt.setString(5,defaultpass);
					stmt.setString(6,st_no);
					stmt.executeUpdate();
				}
				rs.close();
			}
			stmt.close();
			request.setAttribute("msg","更新しました");	
			
			//戻り先URLの設定
			returnURL = "./mg_maintestmaster_get?st_no=" + st_no;
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index/jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./mg_maintestmaster_get?st_no=" + st_no;
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//学生情報更新画面を表示する
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