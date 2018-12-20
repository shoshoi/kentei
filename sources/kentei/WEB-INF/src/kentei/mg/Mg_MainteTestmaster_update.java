package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

import java.sql.SQLException;


/**
 * ������X�V�@�\.
 * <ul>
 * <li>�f�[�^�x�[�X�Ɍ���̏���ǉ��A�܂��͍X�V���Q�Ƃ���
 * <li>������X�V���(mg_maintetestmaster_get)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteTestmaster_update extends HttpServlet{

	/**
	 * <ul>
	 * <li>POST���ꂽ������A�ǉ��t���O�����ƂɃf�[�^�̒ǉ��A�܂��͍X�V���s��
	 * <li>������X�V��ʂ�\������
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
		String msg="";
		String test_no = request.getParameter("test_no");			//POST���ꂽ����ԍ�
		String test_name = request.getParameter("test_name");		//POST���ꂽ���薼
		String associ_no = request.getParameter("associ_no");		//POST���ꂽ���{�c�̔ԍ�
		String insertflg = request.getParameter("insertflg");		//�ǉ��t���O(true=�ǉ�,false=�X�V)
		
		String testSQL_insert = "insert into test values(?,?,?)";	//������ǉ�
		String testSQL_update = "update test set test_name = ?,associ_no = ? where test_no = ?";	//������X�V

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

			if(insertflg.equals("true")){
				//�f�[�^�x�[�X�Ɍ������ǉ�����
				stmt = conn.prepareStatement(testSQL_insert);
				stmt.setString(1,test_no);
				stmt.setString(2,test_name);
				stmt.setString(3,associ_no);
				msg="�ǉ����܂����B";
			}else{
				//�f�[�^�x�[�X�̌�������X�V����
				stmt = conn.prepareStatement(testSQL_update);
				stmt.setString(1,test_name);
				stmt.setString(2,associ_no);
				stmt.setString(3,test_no);
				msg="�X�V���܂����B";
			}
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg",msg);
			
			//�߂��URL�̐ݒ�
			returnURL = "./mg_maintetestmaster_get?st_no=" + test_no;
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./mg_maintetestmaster_get?st_no=" + test_no;
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//������X�V��ʂ�\������
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