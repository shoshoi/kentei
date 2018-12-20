package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.List;
import java.util.Map;

import javax.sql.*;
import javax.naming.*;
import kentei.beans.*;
import kentei.exception.TimeoutException;

/**
 * 取得検定削除機能.
 * <ul>
 * <li>取得検定参照画面(st_gettest.jsp)から受け取った情報をもとに取得検定を削除する
 * <li>検定取得状況参照画面(st_gettest.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")

public class St_remTest extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>削除検定行番号を受け取る
	 * <li>セッションの取得検定一覧から、削除する検定番号、実施日を取り出す
	 * <li>データベースの取得検定を削除する
	 * <li>検定取得状況参照画面を表示する
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
		int listnum = Integer.parseInt(request.getParameter("remtest"));	//削除検定行番号
		String set_test_no = null;						//検定番号
		String set_test_perform_date = null;			//検定実施日	
		List<Map<String,Object>> gettestlist = null;	//取得検定一覧
		Map<String,Object> gettestdata = null;			//検定情報
		
		String sql = "DELETE FROM gettest where st_no = ? AND test_no = ? AND test_perform_date = ?";	//取得検定削除
		
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
			stmt = conn.prepareStatement(sql);
			
			//セッションの取得検定一覧から、削除する検定番号、実施日を取得
			gettestlist = (List<Map<String, Object>>)session.getAttribute("list");
			gettestdata = gettestlist.get(listnum);
			set_test_no = (String)gettestdata.get("test_no");
			set_test_perform_date = (String)gettestdata.get("test_perform_date");	
			
			//データベースの取得検定を削除する
			stmt.setString(1,user.getId());
			stmt.setString(2,set_test_no);
			stmt.setString(3,set_test_perform_date);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","削除しました。");
			
			//戻り先URLの指定
			returnURL = "./st_gettest";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./st_gettest";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//検定取得状況参照画面を表示する
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