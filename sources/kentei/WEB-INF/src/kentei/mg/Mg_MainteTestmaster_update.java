package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

import java.sql.SQLException;


/**
 * 検定情報更新機能.
 * <ul>
 * <li>データベースに検定の情報を追加、または更新を参照する
 * <li>検定情報更新画面(mg_maintetestmaster_get)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteTestmaster_update extends HttpServlet{

	/**
	 * <ul>
	 * <li>POSTされた検定情報、追加フラグをもとにデータの追加、または更新を行う
	 * <li>検定情報更新画面を表示する
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
		String msg="";
		String test_no = request.getParameter("test_no");			//POSTされた検定番号
		String test_name = request.getParameter("test_name");		//POSTされた検定名
		String associ_no = request.getParameter("associ_no");		//POSTされた実施団体番号
		String insertflg = request.getParameter("insertflg");		//追加フラグ(true=追加,false=更新)
		
		String testSQL_insert = "insert into test values(?,?,?)";	//検定情報追加
		String testSQL_update = "update test set test_name = ?,associ_no = ? where test_no = ?";	//検定情報更新

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

			if(insertflg.equals("true")){
				//データベースに検定情報を追加する
				stmt = conn.prepareStatement(testSQL_insert);
				stmt.setString(1,test_no);
				stmt.setString(2,test_name);
				stmt.setString(3,associ_no);
				msg="追加しました。";
			}else{
				//データベースの検定情報を更新する
				stmt = conn.prepareStatement(testSQL_update);
				stmt.setString(1,test_name);
				stmt.setString(2,associ_no);
				stmt.setString(3,test_no);
				msg="更新しました。";
			}
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg",msg);
			
			//戻り先URLの設定
			returnURL = "./mg_maintetestmaster_get?st_no=" + test_no;
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./mg_maintetestmaster_get?st_no=" + test_no;
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//検定情報更新画面を表示する
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