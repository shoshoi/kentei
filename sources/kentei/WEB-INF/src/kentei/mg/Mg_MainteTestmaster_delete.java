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
 * 検定情報削除機能.
 * <ul>
 * <li>データベースの検定の情報を削除する
 * <li>学生マスタメンテナンス画面(/mg_maintestmaster)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")

public class Mg_MainteTestmaster_delete extends HttpServlet{

	/**
	 * <ul>
	 * <li>POSTされた検定番号をもとにデータベースの検定の情報を削除する
	 * <li>学生マスタメンテナンス画面を表示する
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
		String returnURL ="/";		//戻り先URL
		String remst_no = request.getParameter("remtest");		//POSTされた検定番号
		
		String testSQL = "DELETE FROM test where test_no = ?";	//検定削除
		
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

			//データベースの検定の情報を削除する
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();		
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement(testSQL);
			stmt.setString(1,remst_no);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","削除しました。");
			
			//戻り先URLの設定
			returnURL = "./mg_maintetestmaster";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./mg_maintetestmaster";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//メッセージと共に検定マスタメンテナンス画面を表示
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