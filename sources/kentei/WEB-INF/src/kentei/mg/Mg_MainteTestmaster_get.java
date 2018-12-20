package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import java.util.*;

import kentei.beans.User;
import kentei.exception.*;

/**
 * 検定情報参照機能.
 * <ul>
 * <li>データベースの検定の情報を参照する
 * <li>セッションに検定の情報を格納する
 * <li>検定情報更新画面(mg_maintetestmaster_update.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteTestmaster_get extends HttpServlet{
	/**
	 * 処理を @link #doGet() に移行する
	 * 
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @exception ServletException 既定のエラーページを表示
	 * @exception IOException 既定のエラーページを表示
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		doGet(request,response);
	}
	
	/**
	 * <ul>
	 * <li>データベースの検定情報を参照する
	 * <li>学生マスタメンテナンス画面を表示する
	 * <li>データベースから実施団体の情報を参照する
	 * <li>データベースの検定情報、実施団体一覧をセッションに格納する
	 * </ul>
	 * 
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @exception ServletException 既定のエラーページを表示
	 * @exception IOException 既定のエラーページを表示
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		
		request.setCharacterEncoding("utf-8");
		
		//変数の準備
		Connection conn = null;
		RequestDispatcher dispatcher = null;
		String returnURL = "/"; 	//戻り先URL

		List<Map<String,Object>> associlist = null;	//実施団体一覧
		Map<String,Object> associ_data = null;		//実施団体情報
		Map<String,Object> test_data = null;		//検定情報
		String test_no = request.getParameter("test_no");		//入力された検定番号
		String newflg = request.getParameter("newflg");			//入力された新規登録フラグ
		
		String testSQL = "select * from test where test_no=?";	//検定情報参照
		String associSQL = "select * from association";			//実施団体情報参照
		
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
			ResultSet rs = null;
			
			if(test_no != null){
				//検定番号が指定されている場合のみデータベースの検定情報を参照する
				stmt = conn.prepareStatement(testSQL);
				stmt.setString(1,test_no);
				rs = stmt.executeQuery();
				if(rs.next()){
					//検定一覧に検定情報を追加
					test_data = new HashMap<String,Object>();
					test_data.put("test_no",rs.getString("test_no"));
					test_data.put("test_name",rs.getString("test_name"));
					test_data.put("associ_no",rs.getString("associ_no"));
				}
				rs.close();
				stmt.close();
			}
			
			//データベースから実施団体の情報を参照する
			stmt = conn.prepareStatement(associSQL);
			rs = stmt.executeQuery();
			associlist = new ArrayList<Map<String,Object>>();
			
			while(rs.next()){
				//実施団体一覧に実施団体情報を追加
				associ_data = new HashMap<String,Object>();
				associ_data.put("associ_no",rs.getString("associ_no"));
				associ_data.put("associ_name",rs.getString("associ_name"));
				associ_data.put("associ_kana",rs.getString("associ_kana"));
				associlist.add(associ_data);
				associ_data=null;
			}
			rs.close();
			stmt.close();
			
			//新規登録フラグをリクエストスコープに格納する
			request.setAttribute("newflg", newflg);
			
			//データベースの検定情報、実施団体一覧をセッションに格納する
			session.setAttribute("test_data",test_data);
			session.setAttribute("associlist",associlist);
			
			//戻り先URLの設定
			returnURL = "./manager/mg_maintetestmaster_update.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_maintetestmaster_update.jsp";
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