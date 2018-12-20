package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.*;
import kentei.exception.TimeoutException;

/**
 * 取得可能実施日参照機能.
 * <ul>
 * <li>取得検定更新画面(st_addtest.jsp)から受け取った情報をもとに取得可能な実施日を参照する
 * <li>検定実施日一覧をセッションに追加する
 * <li>検定実施日選択画面(st_addtest_insert.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class St_addTest_get extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>検定番号を受け取る
	 * <li>入力された検定番号をもとにデータベースの検定情報を参照する
	 * <li>該当する検定が存在する場合、データベースの取得済み検定情報を参照する
	 * <li>取得済みでない場合検定実施日を参照する
	 * <li>検定実施日一覧をセッションに追加する
	 * <li>検定実施日選択画面を表示する
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
		String returnURL = "/";			//戻り先URL
		String test_no = request.getParameter("test_no");	//入力された検定番号
		List<Map<String,Object>> datelist = null;			//検定実施日一覧
		Map<String,Object> test_date = null;				//検定実施日情報
		
		String testdateSQL = "select * from testdate where test_no=? ORDER BY test_perform_date DESC";	//検定実施日参照
		String gettestSQL = "select * from gettest where st_no=? AND test_no = ?";						//取得済み検定参照
		String testSQL = "select * from test where test_no = ?";		//検定情報参照
		
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
			ResultSet rs = null;
			
			//データベースの検定情報を参照する
			stmt = conn.prepareStatement(testSQL);
			stmt.setString(1,test_no);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				//該当する検定が存在する場合、データベースの取得済み検定情報を参照する
				rs.close();
				stmt.close();	
				stmt = conn.prepareStatement(gettestSQL);
				stmt.setString(1,user.getId());
				stmt.setString(2,test_no);
				rs = stmt.executeQuery();
				
				if(!rs.next()){
					//取得済みでない場合検定実施日を参照する
					rs.close();
					stmt.close();
					stmt = conn.prepareStatement(testdateSQL);
					stmt.setString(1,test_no);
					rs = stmt.executeQuery();
					
					datelist = new ArrayList<Map<String,Object>>();
					while(rs.next()){
						//検定実施日一覧に検定実施日情報を追加する
						test_date = new HashMap<String,Object>();
						test_date.put("test_perform_date",rs.getString("test_perform_date"));
						test_date.put("test_get_date",rs.getString("test_get_date"));
						datelist.add(test_date);
						test_date = null;
					}
				}else{
					//取得済みの場合メッセージ表示
					request.setAttribute("msg","取得済みです。");
				}
				rs.close();
				stmt.close();
			}else{
				request.setAttribute("msg","存在しない検定番号です。");
			}
			rs.close();
			stmt.close();	
			
			//検定実施日一覧をセッションに追加する
			session.setAttribute("datelist",datelist);
			
			//戻り先URL
			returnURL = "./student/st_addtest_insert.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			dispatcher = request.getRequestDispatcher("/");
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			dispatcher = request.getRequestDispatcher(returnURL);
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//検定実施日選択画面を表示する
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