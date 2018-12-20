package kentei;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ���O�A�E�g�@�\.
 * <ul>
 * <li>�e�y�[�W����A�N�Z�X����B
 * <li>�Z�b�V���������݂���ꍇ�A�Z�b�V������j������B
 * <li>���O�C�����(index.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Logout extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>�Z�b�V������j������
	 * <li>���O�C����ʂ�\������
	 * </ul>
	 * @param request HTTP���N�G�X�g
	 * @param response HTTP���X�|���X
	 * @exception ServletException ����̃G���[�y�[�W��\��
	 */
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");	//�߂��̐ݒ�
		
		//�Z�b�V�����̔j��
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
		request.setAttribute("msg","���O�A�E�g���܂����B");
		dispatcher.forward(request,response);
	}
}