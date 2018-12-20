package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import kentei.beans.*;
import kentei.exception.TimeoutException;

import java.util.*;

/**
 * 取得検定参照機能.
 * <ul>
 * <li>セッションに格納されている学生情報をもとに取得検定情報を参照する
 * <li>取得検定一覧をセッションに格納する
 * <li>検定取得状況参照画面(st_gettest.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class St_getTest extends HttpServlet{

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
	 * <li>セッションから学生番号を取り出す
	 * <li>データベースの取得検定情報を参照する
	 * <li>検定取得状況参照画面を表示する
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
		String returnURL = "/";
		String st_no = null;	
		List<Map<String,Object>> gettestlist = null;	//取得検定一覧
		Map<String,Object> gettestdata = null;
		
		String gettestSQL = "select test.test_no,test.test_name,association.associ_name,testdate.test_perform_date,testdate.test_get_date from association join test on association.associ_no = test.associ_no join testdate on test.test_no = testdate.test_no join gettest on testdate.test_no = gettest.test_no AND testdate.test_perform_date = gettest.test_perform_date where gettest.st_no = ? ORDER BY testdate.test_perform_date DESC";
		
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
			
			//セッションから学生番号を取り出す
			st_no = user.getId();
			
			//DB接続の準備
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			//データベースの取得検定情報を参照する
			stmt = conn.prepareStatement(gettestSQL);
			stmt.setString(1,st_no);
			rs = stmt.executeQuery();

			gettestlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//取得検定一覧に取得検定情報を追加
				gettestdata = new HashMap<String,Object>();
				gettestdata.put("test_no",rs.getString("test.test_no"));
				gettestdata.put("test_name",rs.getString("test.test_name"));
				gettestdata.put("association",rs.getString("association.associ_name"));
				gettestdata.put("test_perform_date",rs.getString("testdate.test_perform_date"));
				gettestdata.put("test_get_date",rs.getString("testdate.test_get_date"));
				gettestlist.add(gettestdata);
			}
			rs.close();
			stmt.close();
			
			//取得検定一覧をセッションに格納する
			session.setAttribute("list",gettestlist);

			//戻り先URLの指定
			returnURL = "./student/st_gettest.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./student/st_gettest.jsp";			
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