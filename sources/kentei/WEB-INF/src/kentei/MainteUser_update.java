package kentei;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

/**
 * �p�X���[�h�ύX�@�\.
 * <ul>
 * <li>�p�X���[�h�ύX���(st_mainteuser,mg_mainteuser)����POST���ꂽ�������ƂɃA�J�E���g�����؂���
 * <li>�V�����p�X���[�h��DB�Ɋi�[����
 * <li>�p�X���[�h�ύX���(st_mainteuser,mg_mainteuser)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class MainteUser_update extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>���[�U�[ID�A�p�X���[�h���󂯎��
	 * <li>�f�[�^�x�[�X�̃��[�U�[�����Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̃��[�U�[�p�X���[�h���X�V����
	 * <li>���b�Z�[�W�Ƌ��Ƀp�X���[�h�ύX��ʂ�\������
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
		
		String returnURL = "/";	//�߂��URL
		String msg="";			//���쐬���ۃ��b�Z�[�W
		String user_id;			//���[�U�[ID
		int loginKubun;			//���O�C���敪(0=�w��,1=�Ǘ���)
		String now_pass = request.getParameter("now_pass");		//�|�X�g���ꂽ�ύX�O�p�X���[�h
		String new_pass = request.getParameter("new_pass");		//�|�X�g���ꂽ�ύX��p�X���[�h
		String stSQL_select = "SELECT * FROM student WHERE st_no = ? AND st_pass = password(?)";	//�w�����Q��
		String mgSQL_select = "SELECT * FROM manager WHERE mg_no = ? AND mg_pass = password(?)";	//�Ǘ��ҏ��Q��
		String stSQL_update = "UPDATE student SET st_pass = password(?) where st_no = ?";			//�w���p�X���[�h�X�V
		String mgSQL_update = "UPDATE manager SET mg_pass = password(?) where mg_no = ?";			//�Ǘ��҃p�X���[�h�X�V
		
		try{
			//���O�C�����[�U�[���ێ�
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");
			if(session==null || user==null){
				//�Z�b�V�����������ł���ꍇ�G���[�\��
				throw new TimeoutException("�Z�b�V�������^�C���A�E�g���܂����B");
			}
			user_id = user.getId();
			loginKubun = user.getLoginKubun();
			
			//�f�[�^�x�[�X�̃��[�U�[�����Q�Ƃ���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			if(loginKubun == 0){
				//�w�����ێ�
				stmt = conn.prepareStatement(stSQL_select);
				returnURL = "./st_mainteuser";

			}else{
				//�Ǘ��ҏ��ێ�
				stmt = conn.prepareStatement(mgSQL_select);
				returnURL = "./mg_mainteuser";
			}
				
			stmt.setString(1,user_id);
			stmt.setString(2,now_pass);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				//�f�[�^�x�[�X�̃��[�U�[�p�X���[�h���X�V����
				stmt.close();
				if(loginKubun == 0){
					//�w���p�X���[�h�X�V
					stmt = conn.prepareStatement(stSQL_update);
				}else{
					//�Ǘ��҃p�X���[�h�X�V
					stmt = conn.prepareStatement(mgSQL_update);
				}
				stmt.setString(1,new_pass);
				stmt.setString(2,user_id);
				stmt.executeUpdate();
				msg="�X�V���܂����B";
			}else{
				msg="�p�X���[�h���Ԉ���Ă��܂��B";
			}
			stmt.close();
			rs.close();
			
			request.setAttribute("msg",msg);
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//���b�Z�[�W�Ƌ��Ƀp�X���[�h�ύX��ʂ�\������
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