package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import kentei.beans.*;
import kentei.exception.TimeoutException;

/**
 * �擾�\���{���Q�Ƌ@�\.
 * <ul>
 * <li>������{���I�����(st_addtest_insert.jsp)����󂯎�����������ƂɎ擾�����o�^����
 * <li>������{���I�����(st_addtest)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class St_addTest_add extends HttpServlet{

	/**
	 * <ul>
	 * <li>����ԍ��A������{�����󂯎��
	 * <li>���͂��ꂽ����ԍ��A������{�������ƂɃf�[�^�x�[�X�ɓo�^����
	 * <li>�擾����Q�Ɖ�ʂ�\������
	 * </ul>
	 * 
	 * @param request HTTP���N�G�X�g
	 * @param response HTTP���X�|���X
	 * @exception ServletException ����̃G���[�y�[�W��\��
	 * @exception IOException ����̃G���[�y�[�W��\��
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		
		request.setCharacterEncoding("utf-8");
		
		//�ϐ��̏���
		Connection conn = null;
		RequestDispatcher dispatcher = null;
		String returnURL = "/";		//�߂��URL
		String set_test_no = request.getParameter("test_no");		//POST���ꂽ����ԍ�
		String set_test_perform_date = request.getParameter("test_perform_date");	//POST���ꂽ������{��

		String gettestSQL = "insert into gettest values(?,?,?)";		//������{���o�^
		
		try{
			//�Z�b�V�����L���ۊm�F
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");
			if(session==null || user==null){
				//�Z�b�V�����������ł���ꍇ�G���[�\��
				throw new TimeoutException("�Z�b�V�������^�C���A�E�g���܂����B");
			}else if(user.getLoginKubun()!=0){
				//���O�C���敪���w���łȂ��ꍇ�G���[�\��
				throw new TimeoutException("�A�N�Z�X����������܂���B");
			}
			
			//DB�ڑ��̏���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			
			//�擾�����ǉ�����
			stmt = conn.prepareStatement(gettestSQL);
			stmt.setString(1,user.getId());
			stmt.setString(2,set_test_no);
			stmt.setString(3,set_test_perform_date);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","�ǉ����܂����B");
			
			//�߂��URL�̐ݒ�
			returnURL = "./st_addtest";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./st_addtest";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//�擾����Q�Ɖ�ʂ�\������
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