package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import java.util.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

/**
 * 学生取得検定参照機能.
 * <ul>
 * <li>学生取得検定情報、学生情報を参照する
 * <li>セッションに学生取得検定一覧、学生名を格納する
 * <li>学生取得検定一覧表示画面(mg_stsearchtest_get.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_StSearchTest_get extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>データベースから学生取得検定を参照する
	 * <li>POSTされた学生番号をもとにデータベースの学生の名前を参照する
	 * <li>セッションに学生取得検定一覧、学生名を格納する
	 * <li>学生取得検定一覧表示画面を表示する
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
		List<Map<String,Object>> gettestlist = null;	//学生取得検定一覧
		Map<String,Object> testdata;					//検定情報
		String st_name = "";							//学生名
		String st_no = request.getParameter("st_no");	//入力された学生番号
		
		//学生取得検定参照
		String gettestSQL = "select test.test_no,test.test_name,association.associ_name,testdate.test_perform_date,testdate.test_get_date from association join test on association.associ_no = test.associ_no join testdate on test.test_no = testdate.test_no join gettest on testdate.test_no = gettest.test_no AND testdate.test_perform_date = gettest.test_perform_date where gettest.st_no = ? ORDER BY testdate.test_perform_date DESC";
		String stSQL = "select student.st_name from student where st_no = ?";	//学生情報参照
		
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

			//データベースの学生取得検定情報を参照する
			stmt = conn.prepareStatement(gettestSQL);
			stmt.setString(1,st_no);
			ResultSet rs = stmt.executeQuery();
			
			gettestlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//検定一覧に検定情報を追加
				testdata = new HashMap<String,Object>();
				testdata.put("test_no",rs.getString("test.test_no"));
				testdata.put("test_name",rs.getString("test.test_name"));
				testdata.put("association",rs.getString("association.associ_name"));
				testdata.put("test_perform_date",rs.getString("testdate.test_perform_date"));
				testdata.put("test_get_date",rs.getString("testdate.test_get_date"));
				gettestlist.add(testdata);
			}
			rs.close();
			stmt.close();
			
			//データベースの学生の名前を参照する
			stmt = conn.prepareStatement(stSQL);
			stmt.setString(1,st_no);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				//学生名保持
				st_name = rs.getString("student.st_name");
				rs.close();
				stmt.close();
			}
			
			//セッションに学生取得検定一覧、学生名を格納する
			session.setAttribute("list",gettestlist);
			session.setAttribute("st_name",st_name);

			//戻り先URLの指定
			returnURL = "./manager/mg_stsearchtest_get.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_stsearchtest_get.jsp";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//学生取得検定一覧表示画面を表示する
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