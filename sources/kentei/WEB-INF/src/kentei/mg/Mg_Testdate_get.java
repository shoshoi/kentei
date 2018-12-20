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
 * 検定実施日参照機能.
 * <ul>
 * <li>検定実施日メンテナンス画面(mg_testdate.jsp)からの情報をもとに検定情報参照、検定実施日を参照する
 * <li>検定実施日一覧、検定情報をリクエストスコープに格納する
 * <li>検定実施日登録・削除画面(mg_testdate_get)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_Testdate_get extends HttpServlet{
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
	 * <li>検定番号、検定実施日を受け取る
	 * <li>データベースから該当する検定実施日を参照する
	 * <li>データベースの検定情報を参照する
	 * <li>検定実施日一覧、検定情報をリクエストスコープに格納する
	 * <li>検定実施日登録・削除画面を表示する
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
		String returnURL="/";		//戻り先URL
		List<Map<String,Object>> datelist = null;		//検定実施日一覧
		Map<String,Object> test_performdate = null;		//検定実施日
		Map<String,Object> test_data = null;			//検定取得日
		String test_no = request.getParameter("test_no");	//入力された検定番号
		
		String testdateSQL = "select * from testdate where test_no=? order by test_perform_date desc";	//検定実施日一覧参照
		//検定情報参照
		String testSQL ="select test.test_name,association.associ_name from test join association on test.associ_no = association.associ_no where test.test_no=?";

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
				//検定番号が指定されている場合、データベースの検定実施日を参照する
				stmt = conn.prepareStatement(testdateSQL);
				stmt.setString(1,test_no);
				rs = stmt.executeQuery();

				datelist = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					//検定実施日一覧に検定実施日を追加
					test_performdate = new HashMap<String,Object>();
					test_performdate.put("test_perform_date",rs.getString("test_perform_date"));
					test_performdate.put("test_get_date",rs.getString("test_get_date"));
					datelist.add(test_performdate);
				}
				rs.close();
				stmt.close();
				
				//データベースの検定情報を参照する
				stmt = conn.prepareStatement(testSQL);
				stmt.setString(1,test_no);
				rs = stmt.executeQuery();
				if(rs.next()){
					//検定情報を保持
					test_data = new HashMap<String,Object>();
					test_data.put("test_name",rs.getString("test.test_name"));
					test_data.put("associ_name",rs.getString("association.associ_name"));
				}
				rs.close();
				stmt.close();
			}
			
			//検定実施日一覧、検定情報をリクエストスコープに格納する
			request.setAttribute("datelist",datelist);
			request.setAttribute("test_data",test_data);
			
			//戻り先URLの指定
			returnURL = "./manager/mg_testdate_insert.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_testdate_insert.jsp";
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