package kentei;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ログアウト機能.
 * <ul>
 * <li>各ページからアクセスする。
 * <li>セッションが存在する場合、セッションを破棄する。
 * <li>ログイン画面(index.jsp)へ遷移する
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Logout extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>セッションを破棄する
	 * <li>ログイン画面を表示する
	 * </ul>
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @exception ServletException 既定のエラーページを表示
	 */
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");	//戻り先の設定
		
		//セッションの破棄
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
		request.setAttribute("msg","ログアウトしました。");
		dispatcher.forward(request,response);
	}
}