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
 * <li>学生情報を参照する
 * <li>学科情報を参照する
 * <li>セッションに学生一覧、学科一覧を格納する
 * <li>学生⇒検定検索画面(mg_stsearchtest.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_StSearchTest extends HttpServlet{

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
	 * <li>絞込み学科番号、絞込み学年を受け取る
	 * <li>データベースの学生情報を参照する
	 * <li>絞込み学科番号、または絞込み学年が指定されている場合絞込みを行う
	 * <li>データベースの学科情報を参照する
	 * <li>セッションに学生一覧、学科一覧を格納する
	 * <li>学生⇒検定検索画面を表示する
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
		List<Map<String,Object>> stlist = null;		//学生一覧
		List<Map<String,Object>> classlist = null;	//学科一覧
		Map<String,Object> stdata;					//学生情報
		Map<String,Object> classdata;				//学科情報
		String limit_class = request.getParameter("class");		//入力された絞込み学科番号
		String limit_year = request.getParameter("year");		//入力された絞込み学年
		
		/*　学生参照SQL
		 * stSQL : 絞込みなし
		 * stSQL_limitClass : 学科絞込み
		 * stSQL_limitYear : 学年絞込み
		 * stSQL_limitClassYear : 学科・学年絞込み
		 */
		String stSQL = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no order by class.class_no ASC,student.st_no+0 asc";
		String stSQL_limitClass = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no where student.class_no = ? order by class.class_no ASC,student.st_no+0 asc";
		String stSQL_limitYear = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no where student.year = ? order by class.class_no ASC,student.st_no+0 asc";
		String stSQL_limitClassYear = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no where student.class_no = ? AND student.year = ? order by class.class_no ASC,student.st_no+0 asc";
		String classSQL = "select class_no,class_name from class";		//学科参照

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

			//データベースの学生情報を参照する
			if(limit_class == null) limit_class = "0";	//絞込み学科番号が指定されていない場合絞込みなしとみなす
			if(limit_year == null) limit_year = "0";	//絞込み学年が指定されていない場合絞込みなしとみなす
			
			if(limit_class.equals("0") && limit_year.equals("0")){
				//絞り込みなし
				stmt = conn.prepareStatement(stSQL);
			}else if(!limit_class.equals("0") && limit_year.equals("0")){
				//学科のみ絞込み
				stmt = conn.prepareStatement(stSQL_limitClass);
				stmt.setString(1,limit_class);
			}else if(limit_class.equals("0") && !limit_year.equals("0")){
				//学年のみ絞込み
				stmt = conn.prepareStatement(stSQL_limitYear);
				stmt.setString(1,limit_year);
			}else{
				//学科・学年絞り込み
				stmt = conn.prepareStatement(stSQL_limitClassYear);
				stmt.setString(1,limit_class);
				stmt.setString(2,limit_year);
			}
			ResultSet rs = stmt.executeQuery();
			
			stlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//学生一覧に学生情報を追加
				stdata = new HashMap<String,Object>();
				stdata.put("st_no",rs.getString("st_no"));
				stdata.put("class_name",rs.getString("class_name"));
				stdata.put("year",rs.getString("year"));
				stdata.put("st_name",rs.getString("st_name"));
				stlist.add(stdata);
			}
			rs.close();
			stmt.close();
			
			//データベースの学科情報を参照する
			stmt = conn.prepareStatement(classSQL);
			rs = stmt.executeQuery();
			
			classlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//学科一覧に学科情報を追加
				classdata = new HashMap<String,Object>();
				classdata.put("class_no",rs.getString("class_no"));
				classdata.put("class_name",rs.getString("class_name"));
				classlist.add(classdata);
			}
			rs.close();
			stmt.close();
			
			//セッションに学生一覧、学科一覧を格納する
			session.setAttribute("list",stlist);
			session.setAttribute("classlist",classlist);
			
			//リクエストスコープに絞込みフラグを格納する
			request.setAttribute("limit_class",limit_class);
			request.setAttribute("limit_year",limit_year);
			
			//戻り先URLの指定
			returnURL = "./manager/mg_stsearchtest.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_stsearchtest.jsp";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//学生⇒検定検索画面を表示する
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