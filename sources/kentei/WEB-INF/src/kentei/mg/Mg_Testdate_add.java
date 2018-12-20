package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

/**
 * ������{���o�^�@�\.
 * <ul>
 * <li>������{���o�^�E�폜���(mg_testdate_insert.jsp)����POST���ꂽ�������ƂɌ�����{����ǉ�����
 * <li>������{���o�^�E�폜���(mg_testdate_get)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_Testdate_add extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>������{���A����擾���A����ԍ����󂯎��
	 * <li>�f�[�^�x�[�X�Ɍ�����{����ǉ�����
	 * <li>������{���o�^�E�폜��ʂ�\������
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
		String returnURL = "/";
		String test_perform_date = request.getParameter("perform_date");	//POST���ꂽ������{��
		String test_get_date = request.getParameter("get_date");			//POST���ꂽ����擾��
		String test_no = request.getParameter("test_no");					//POST���ꂽ����ԍ�
		
		String testdateSQL = "insert into testdate values(?,?,?)";		//������{���ǉ�
		
		try{
			//�Z�b�V�����L���ۊm�F
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");
			if(session==null || user==null){
				//�Z�b�V�����������ł���ꍇ�G���[�\��
				throw new TimeoutException("�Z�b�V�������^�C���A�E�g���܂����B");
			}else if(user.getLoginKubun()!=1){
				//���O�C���敪���Ǘ��҂łȂ��ꍇ�G���[�\��
				throw new TimeoutException("�A�N�Z�X����������܂���B");
			}
			
			//DB�ڑ��̏���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			
			//�f�[�^�x�[�X�Ɍ�����{����ǉ�����
			stmt = conn.prepareStatement(testdateSQL);
			stmt.setString(1,test_no);
			stmt.setString(2,test_perform_date);
			stmt.setString(3,test_get_date);
			stmt.executeUpdate();
			stmt.close();

			request.setAttribute("msg","�o�^���܂����B");
			
			//�߂��URL�̎w��
			returnURL = "./mg_testdate_get?test_no=" + test_no;		
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";	
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./mg_testdate_get?test_no=" + test_no;	
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//������{���o�^�E�폜��ʂ�\������
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