package kentei;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;

/**
 * ���O�C���@�\.
 * <ul>
 * <li>���O�C�����(login.jsp)����POST���ꂽ�������ƂɃA�J�E���g�����؂���
 * <li>���[�U�����Z�b�V�����Ɋi�[����
 * <li>���j���[���(sg_main, mg_main)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Login extends HttpServlet{

	/**
	 * <ul>
	 * <li>���[�U�[ID�A�p�X���[�h���󂯎��
	 * <li>�f�[�^�x�[�X�̃��[�U�[�����Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̃��[�U�[�����Z�b�V�����Ɋi�[����
	 * <li>���[�U�[���̏Ɖ�ɐ��������ꍇ���C�����j���[��\���A����ȊO�̏ꍇ�̓��b�Z�[�W�Ƌ��Ƀ��O�C����ʂ��ĕ\��
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
		String user_id = null;				//���[�U�[ID
		String user_name = null;			//���[�U�[��
		String stSQL = "SELECT * FROM student WHERE st_no = ? AND st_pass = password(?)";	//�w�����Q��
		String mgSQL = "SELECT * FROM manager WHERE mg_no = ? AND mg_pass = password(?)";	//�Ǘ��ҏ��Q��
		User user = null;
		String requestId = request.getParameter("id");							//POST���ꂽ���[�U�[��
		String requestPass = request.getParameter("pass");						//POST���ꂽ�p�X���[�h
		int loginKubun;
		
		try{
			//�f�[�^�x�[�X�̃��[�U�[�����Q�Ƃ���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			stmt = conn.prepareStatement(stSQL);
			stmt.setString(1,requestId);
			stmt.setString(2,requestPass);
			rs = stmt.executeQuery();

			if(rs.next()){
				//�w�����O�C������
				user_id = rs.getString("st_no");
				user_name = rs.getString("st_name");
				loginKubun = 0;
				user = new User(user_id,user_name,loginKubun);
				returnURL = "/st_main";
			}else{
				//�w���e�[�u���ƈ�v���Ȃ��ꍇ�A�Ǘ��҃e�[�u�����Q��
				rs.close();
				stmt.close();
				stmt = conn.prepareStatement(mgSQL);
				stmt.setString(1,requestId);
				stmt.setString(2,requestPass);
				rs = stmt.executeQuery();
				if(rs.next()){
					//�Ǘ��҃��O�C������
					user_id = rs.getString("mg_no");
					user_name = rs.getString("mg_name");
					loginKubun = 1;
					user = new User(user_id,user_name,loginKubun);
					returnURL = "/mg_main";
				}else{
					//���O�C�����s
					request.setAttribute("msg","���[�U�h�c�܂��̓p�X���[�h���Ԉ���Ă��܂�");
					returnURL = "/index.jsp";
				}
			}
			rs.close();
			stmt.close();
			
			if(user!=null){
				//���O�C���ɐ��������ꍇ�A�f�[�^�x�[�X�̃��[�U�[�����Z�b�V�����Ɋi�[����
				HttpSession session = request.getSession(false);
				if(session != null){
					session.invalidate();
				}
				session = request.getSession(true);
				session.setAttribute("user",user);
			}
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "/index.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//���[�U�[���̏Ɖ�ɐ��������ꍇ���C�����j���[��\���A����ȊO�̏ꍇ�̓��b�Z�[�W�Ƌ��Ƀ��O�C����ʂ��ĕ\��
			dispatcher = request.getRequestDispatcher(returnURL);
			dispatcher.forward(request,response);
			try{
				if(conn != null){
					conn.close();
				}
			}catch(Exception e){
				throw new ServletException(e);
			}
		}
	}
}
