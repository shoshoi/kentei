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
 * 実施団体情報登録機能.
 * <ul>
 * <li>実施団体メンテナンス画面(mg_mainteassocimaster)からPOSTされた実施団体情報をデータベースに登録する
 * <li>実施団体メンテナンス画面(mg_mainteassocimaster)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteAssocimaster_update extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>実施団体情報を受け取る
	 * <li>データベースに実施団体情報を登録する
	 * <li>メッセージと共に実施団体メンテナンス画面を表示する
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
		String associ_no = request.getParameter("associ_no");		//POSTされた実施団体番号
		String associ_name = request.getParameter("associ_name");	//POSTされた実施団体名
		String associ_kana = request.getParameter("associ_kana");	//POSTされた実施団体フリガナ
		
		String associSQL = "insert into association values(?,?,?)";	//実施団体情報登録
		
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
			
			//実施団体の情報を登録する
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;

			stmt = conn.prepareStatement(associSQL);
			stmt.setString(1,associ_no);
			stmt.setString(2,associ_name);
			stmt.setString(3,associ_kana);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","登録しました。");	
			
			//戻り先URLの設定
			returnURL = "./mg_mainteassocimaster";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./mg_mainteassocimaster";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//メッセージと共に実施団体メンテナンス画面を表示する
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