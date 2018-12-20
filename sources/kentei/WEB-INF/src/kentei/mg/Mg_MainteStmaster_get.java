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
 * 学生情報参照機能.
 * <ul>
 * <li>学生マスタメンテナンス画面(mg_maintestmaster.jsp)から指定された情報をもとに学生個人の情報を参照する
 * <li>学生個人情報をセッションに格納する
 * <li>学科一覧をセッションに格納する
 * <li>学生情報更新画面(mg_maintestmaster_update.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class Mg_MainteStmaster_get extends HttpServlet{

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
	 * <li>学生番号が指定されている場合、該当する学生個人の情報をデータベースから参照する
	 * <li>データベースの学科情報を参照する
	 * <li>データベースより参照した学生個人情報をセッションに格納する
	 * <li>データベースより参照した学科一覧をセッションに格納する
	 * <li>学生情報更新画面を表示する
	 * </ul>
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
		List<Map<String,Object>> classlist = null;		//学科一覧
		Map<String,Object> class_data = null;			//学科情報
		Map<String,Object> st_data = null;				//学生情報
		
		String st_no = request.getParameter("st_no");				//入力された学生番号
		String stSQL = "select * from student where st_no=?";		//学生情報参照
		String classSQL = "select class_no,class_name from class";	//学科情報参照
		
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
			
			if(st_no!=null){
				//学生番号が指定されている場合のみデータベースの学生個人情報を参照する
				stmt = conn.prepareStatement(stSQL);
				stmt.setString(1,st_no);
				rs = stmt.executeQuery();
				
				st_data = new HashMap<String,Object>();
				if(rs.next()){
					//学生情報の保持
					st_data.put("st_no",rs.getString("st_no"));
					st_data.put("st_name",rs.getString("st_name"));
					st_data.put("st_kana",rs.getString("st_kana"));
					st_data.put("class_no",rs.getString("class_no"));
					st_data.put("year",rs.getString("year"));
				}
				rs.close();
				stmt.close();
			}
			
			//データベースの学科情報を参照する
			stmt = conn.prepareStatement(classSQL);
			rs = stmt.executeQuery();
			classlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//学科一覧に学科情報を追加
				class_data = new HashMap<String,Object>();
				class_data.put("class_no",rs.getString("class_no"));
				class_data.put("class_name",rs.getString("class_name"));
				classlist.add(class_data);
				class_data=null;
			}
			rs.close();
			stmt.close();
			
			//データベースの学生個人情報、学科一覧情報をセッションに格納する
			session.setAttribute("st_data",st_data);
			session.setAttribute("classlist",classlist);
			
			//戻り先URLの設定
			returnURL = "./manager/mg_maintestmaster_update.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_maintestmaster_update.jsp";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//学生情報更新画面を表示する
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