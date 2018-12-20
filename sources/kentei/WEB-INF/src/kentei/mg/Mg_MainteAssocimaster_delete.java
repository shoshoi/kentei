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
 * ���{�c�̏��폜�@�\.
 * <ul>
 * <li>���{�c�̃����e�i���X���(mg_mainteassocimaster)����POST���ꂽ�������ƂɊY��������{�c�̏����폜����
 * <li>���{�c�̃����e�i���X���(mg_mainteassocimaster)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteAssocimaster_delete extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>���{�c�̔ԍ����󂯎��
	 * <li>�f�[�^�x�[�X�̎��{�c�̏����폜����
	 * <li>���b�Z�[�W�Ƌ��Ɏ��{�c�̃����e�i���X��ʂ�\������
	 * </ul>
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
		String associ_no = request.getParameter("associ_no");				//POST���ꂽ���{�c�̔ԍ�
		
		String associSQL = "delete from association where associ_no = ?";	//���{�c�̏��폜
		
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
			
			//���{�c�̂̏����폜����
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			
			stmt = conn.prepareStatement(associSQL);
			stmt.setString(1,associ_no);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","�폜���܂����B");
			
			//�߂��URL�̐ݒ�
			returnURL = "./mg_mainteassocimaster";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./mg_mainteassocimaster";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//���b�Z�[�W�Ƌ��Ɏ��{�c�̃����e�i���X��ʂ�\������
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