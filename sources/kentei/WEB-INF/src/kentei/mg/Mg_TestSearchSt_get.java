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
 * 学生一覧参照機能.
 * <ul>
 * <li>学生情報、検定実施日情報、検定情報を参照する
 * <li>検定一覧、実施日一覧、検定名、検定番号をセッションに格納する
 * <li>検定⇒学生検索結果表示画面(mg_testsearchst_get.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")

public class Mg_TestSearchSt_get extends HttpServlet{

	/**
	 * <ul>
	 * <li>検定番号、絞込み日時を受け取る
	 * <li>データベースの学生情報を参照する
	 * <li>データベースの検定実施日を参照する
	 * <li>データベースの検定名を参照する
	 * <li>検定一覧、実施日一覧、検定名、検定番号をセッションに格納する
	 * <li>検定⇒学生検索結果表示画面を表示する
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
		String returnURL = "/";		//戻り先URL
		String test_name = null;	//データベースから取得した検定名
		List<Map<String,Object>> stlist = new ArrayList<Map<String,Object>>();		//学生一覧
		List<Map<String,Object>> datelist = new ArrayList<Map<String,Object>>();	//実施日一覧
		Map<String,Object> stdata = null;	//学生情報
		Map<String,Object> datedata = null;	//実施日
		String test_no = request.getParameter("test_no");		//入力された検定番号
		String limit_date = request.getParameter("date");		//入力された絞込み日時
		
		/*　
		 * stSQL : 学生情報参照
		 * stSQL_limitDate : 学生情報参照(学科絞込み)
		 * testdateSQL : 実施日参照
		 * testSQL : 検定情報参照
		 */
		String stSQL = "select student.st_no,student.st_name,class.class_name,student.year,gettest.test_perform_date,testdate.test_get_date from student join class on student.class_no = class.class_no join gettest on student.st_no = gettest.st_no join testdate on gettest.test_perform_date = testdate.test_perform_date AND gettest.test_no = testdate.test_no where gettest.test_no = ?";
		String stSQL_limitDate = "select student.st_no,student.st_name,class.class_name,student.year,gettest.test_perform_date,testdate.test_get_date from student join class on student.class_no = class.class_no join gettest on student.st_no = gettest.st_no join testdate on gettest.test_perform_date = testdate.test_perform_date AND gettest.test_no = testdate.test_no where gettest.test_no = ? AND gettest.test_perform_date = ?";
		String testdateSQL = "select testdate.test_perform_date from testdate where testdate.test_no = ?";
		String testSQL = "select test_name from test where test_no = ?";
		
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
			
			//データベースの学生情報を参照する
			if(limit_date == null) limit_date = "0";
			if(limit_date.equals("0")){
				stmt = conn.prepareStatement(stSQL);
				stmt.setString(1,test_no);
			}else{
				stmt = conn.prepareStatement(stSQL_limitDate);
				stmt.setString(1,test_no);
				stmt.setString(2,limit_date);
			}
			rs = stmt.executeQuery();

			while(rs.next()){
				//学生一覧に学生情報を追加
				stdata = new HashMap<String,Object>();
				stdata.put("st_no",rs.getString("student.st_no"));
				stdata.put("st_name",rs.getString("student.st_name"));
				stdata.put("class_name",rs.getString("class.class_name"));
				stdata.put("year",rs.getString("student.year"));
				stdata.put("test_perform_date",rs.getString("gettest.test_perform_date"));
				stdata.put("test_get_date",rs.getString("testdate.test_get_date"));
				stlist.add(stdata);
			}
			
			rs.close();
			stmt.close();
			
			//データベースの検定実施日を参照する
			stmt = conn.prepareStatement(testdateSQL);
			stmt.setString(1,test_no);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				//検定一覧に検定実施日を追加
				datedata = new HashMap<String,Object>();
				datedata.put("test_perform_date",rs.getString("testdate.test_perform_date"));
				datelist.add(datedata);
			}
			rs.close();
			stmt.close();
			
			//データベースの検定名を参照する
			stmt = conn.prepareStatement(testSQL);
			stmt.setString(1,test_no);
			rs = stmt.executeQuery();
			if(rs.next()){
				test_name = rs.getString("test_name");
			}
			rs.close();
			stmt.close();
			
			//検定一覧、実施日一覧、検定名、検定番号をセッションに格納する
			session.setAttribute("list",stlist);
			session.setAttribute("datelist",datelist);
			session.setAttribute("test_name",test_name);
			session.setAttribute("test_no",test_no);
			
			//絞込み日時をリクエストスコープに格納する
			request.setAttribute("limit_date",limit_date);
			
			//戻り先URLの設定
			returnURL = "./manager/mg_testsearchst_get.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_testsearchst_get.jsp";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//検定⇒学生検索結果表示画面を表示
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