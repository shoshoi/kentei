package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.List;
import java.util.Map;

import javax.sql.*;
import javax.naming.*;
import kentei.beans.*;
import kentei.exception.TimeoutException;

/**
 * �擾����폜�@�\.
 * <ul>
 * <li>�擾����Q�Ɖ��(st_gettest.jsp)����󂯎�����������ƂɎ擾������폜����
 * <li>����擾�󋵎Q�Ɖ��(st_gettest.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")

public class St_remTest extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>�폜����s�ԍ����󂯎��
	 * <li>�Z�b�V�����̎擾����ꗗ����A�폜���錟��ԍ��A���{�������o��
	 * <li>�f�[�^�x�[�X�̎擾������폜����
	 * <li>����擾�󋵎Q�Ɖ�ʂ�\������
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
		int listnum = Integer.parseInt(request.getParameter("remtest"));	//�폜����s�ԍ�
		String set_test_no = null;						//����ԍ�
		String set_test_perform_date = null;			//������{��	
		List<Map<String,Object>> gettestlist = null;	//�擾����ꗗ
		Map<String,Object> gettestdata = null;			//������
		
		String sql = "DELETE FROM gettest where st_no = ? AND test_no = ? AND test_perform_date = ?";	//�擾����폜
		
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
			stmt = conn.prepareStatement(sql);
			
			//�Z�b�V�����̎擾����ꗗ����A�폜���錟��ԍ��A���{�����擾
			gettestlist = (List<Map<String, Object>>)session.getAttribute("list");
			gettestdata = gettestlist.get(listnum);
			set_test_no = (String)gettestdata.get("test_no");
			set_test_perform_date = (String)gettestdata.get("test_perform_date");	
			
			//�f�[�^�x�[�X�̎擾������폜����
			stmt.setString(1,user.getId());
			stmt.setString(2,set_test_no);
			stmt.setString(3,set_test_perform_date);
			stmt.executeUpdate();
			stmt.close();
			request.setAttribute("msg","�폜���܂����B");
			
			//�߂��URL�̎w��
			returnURL = "./st_gettest";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./st_gettest";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//����擾�󋵎Q�Ɖ�ʂ�\������
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