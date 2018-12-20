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
 * 検定マスタメンテナンス機能.
 * <ul>
 * <li>検定情報、実施団体情報を参照する
 * <li>検定一覧、実施団体一覧をセッションに格納する
 * <li>検定マスタメンテナンス画面(mg_maintetestmaster.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteTestmaster extends HttpServlet{
	
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
	 * <li>データベースから検定情報を参照する
	 * <li>絞込み実施団体番号が指定されている場合、絞込みを行う
	 * <li>データベースから実施団体情報を参照する
	 * <li>セッションに検定一覧、実施団体一覧を格納する
	 * <li>検定情報更新画面を表示する
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
		String returnURL ="/";		//戻り先URL
		List<Map<String,Object>> testlist = null;		//検定一覧
		List<Map<String,Object>> associlist = null;		//実施団体一覧
		Map<String,Object> testdata = null;				//検定情報
		Map<String,Object> associdata = null;			//実施団体情報
		String limit_associ = request.getParameter("associ");	//入力された絞込み実施団体番号
		
		/* testSQL : 検定情報参照
		 * testSQL_limitAssoci : 検定情報参照（実施団体絞込み）
		 * associSQL : 実施団体参照 
		 */
		String testSQL = "select test.test_no,test.test_name,association.associ_name from test join association on test.associ_no = association.associ_no order by test.test_no asc";
		String testSQL_limitAssoci = "select test.test_no,test.test_name,association.associ_name from test join association on test.associ_no = association.associ_no where association.associ_no = ? order by test.test_no asc";
		String associSQL = "select associ_no,associ_name from association";
		
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
			
			//データベースから検定情報を参照する
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;

			//絞込みの指定
			if(limit_associ == null) limit_associ = "0";	//フラグが設定されていない場合絞込みなしとみなす			
			if(limit_associ.equals("0")){
				//絞り込みなし
				stmt = conn.prepareStatement(testSQL);
			}else{
				//学科絞込み
				stmt = conn.prepareStatement(testSQL_limitAssoci);
				stmt.setString(1,limit_associ);
			}

			ResultSet rs = stmt.executeQuery();
			testlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//検定一覧に検定情報を追加
				testdata = new HashMap<String,Object>();
				testdata.put("test_no",rs.getString("test.test_no"));
				testdata.put("test_name",rs.getString("test.test_name"));
				testdata.put("associ_name",rs.getString("association.associ_name"));
				testlist.add(testdata);
			}
			rs.close();
			stmt.close();
			
			//データベースから実施団体情報を参照する
			stmt = conn.prepareStatement(associSQL);
			rs = stmt.executeQuery();
			associlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//実施団体一覧に実施団体情報を追加
				associdata = new HashMap<String,Object>();
				associdata.put("associ_no",rs.getString("associ_no"));
				associdata.put("associ_name",rs.getString("associ_name"));
				associlist.add(associdata);
			}
			rs.close();
			stmt.close();
			
			//セッションに検定一覧、実施団体一覧を格納する
			session.setAttribute("list",testlist);
			session.setAttribute("associlist",associlist);
			
			//リクエストスコープに絞込みフラグを格納する
			request.setAttribute("limit_associ",limit_associ);
			
			//戻り先URLの設定
			returnURL = "./manager/mg_maintetestmaster.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","データ操作に失敗しました。");
			returnURL = "./manager/mg_maintetestmaster.jsp";
		}catch(Exception e){
			//既定のエラーページを表示
			throw new ServletException(e);
		}finally{
			//検定マスタメンテナンス画面を表示する
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