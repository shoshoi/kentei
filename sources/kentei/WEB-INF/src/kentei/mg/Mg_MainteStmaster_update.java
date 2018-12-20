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
 * �w�����X�V�@�\.
 * <ul>
 * <li>�w���}�X�^�����e�i���X���(mg_maintestmaster_update.jsp)����w�肳�ꂽ�������ƂɊw���l�̏����X�V����
 * <li>�w�����X�V���(/mg_maintestmaster_get)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class Mg_MainteStmaster_update extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>POST���ꂽ�������Ɋw�������X�V����
	 * <li>�p�X���[�h�������t���O���w�肳��Ă���ꍇ�A�p�X���[�h������������
	 * <li>�w�����X�V��ʂ�\������
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
		String returnURL = "/";			//�߂��URL
		String st_no = request.getParameter("st_no");		//POST���ꂽ�w���ԍ�
		String st_name = request.getParameter("st_name");	//POST���ꂽ�w����
		String st_kana = request.getParameter("st_kana");	//POST���ꂽ�w�����J�i
		String class_no = request.getParameter("class_no");	//POST���ꂽ�w�Ȕԍ�
		String pass = request.getParameter("pass");			//POST���ꂽ�p�X���[�h�������t���O(true:�p�X���[�h������)
		String year = request.getParameter("year");			//POST���ꂽ�w�N
		
		String stSQL = "update student set " +			//�w�����X�V
				"st_name = ?," +
				"st_kana = ?," +
				"class_no = ?," +
				"year = ?" +
				"where st_no = ?";
		
		String stSQL_passwd = "update student set " +	//�p�X���[�h�����������w�����X�V
				"st_name = ?," +
				"st_kana = ?," +
				"class_no = ?," +
				"year = ?," +
				"st_pass = password(?) " + 
				"where st_no = ?";
	
		String stSQL_birthday = "select birthday from student where st_no = ?";	//�w�����N�����擾
	
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
			
			//�f�[�^�x�[�X�̊w�������X�V����
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;

			if(pass.equals("false")){
				//�w�����X�V
				stmt = conn.prepareStatement(stSQL);
				stmt.setString(1,st_name);
				stmt.setString(2,st_kana);
				stmt.setString(3,class_no);
				stmt.setString(4,year);
				stmt.setString(5,st_no);
				stmt.executeUpdate();
			}else{
				//�w�����X�V�A�p�X���[�h��������
				stmt = conn.prepareStatement(stSQL_birthday);
				stmt.setString(1,st_no);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()){
					//�a���N����(����A8�P�^)���p�X���[�h�Ƃ��w�������X�V����
					String defaultpass = rs.getString("birthday").replaceAll("-", "");
					stmt.close();
					stmt = conn.prepareStatement(stSQL_passwd);
					stmt.setString(1,st_name);
					stmt.setString(2,st_kana);
					stmt.setString(3,class_no);
					stmt.setString(4,year);
					stmt.setString(5,defaultpass);
					stmt.setString(6,st_no);
					stmt.executeUpdate();
				}
				rs.close();
			}
			stmt.close();
			request.setAttribute("msg","�X�V���܂���");	
			
			//�߂��URL�̐ݒ�
			returnURL = "./mg_maintestmaster_get?st_no=" + st_no;
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index/jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./mg_maintestmaster_get?st_no=" + st_no;
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//�w�����X�V��ʂ�\������
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