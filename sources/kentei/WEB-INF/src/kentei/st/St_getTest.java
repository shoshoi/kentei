package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import kentei.beans.*;
import kentei.exception.TimeoutException;

import java.util.*;

/**
 * �擾����Q�Ƌ@�\.
 * <ul>
 * <li>�Z�b�V�����Ɋi�[����Ă���w���������ƂɎ擾��������Q�Ƃ���
 * <li>�擾����ꗗ���Z�b�V�����Ɋi�[����
 * <li>����擾�󋵎Q�Ɖ��(st_gettest.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class St_getTest extends HttpServlet{

	/**
	 * ������ @link #doGet() �Ɉڍs����
	 * 
	 * @param request HTTP���N�G�X�g
	 * @param response HTTP���X�|���X
	 * @exception ServletException ����̃G���[�y�[�W��\��
	 * @exception IOException ����̃G���[�y�[�W��\��
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		doGet(request,response);
	}
	
	/**
	 * <ul>
	 * <li>�Z�b�V��������w���ԍ������o��
	 * <li>�f�[�^�x�[�X�̎擾��������Q�Ƃ���
	 * <li>����擾�󋵎Q�Ɖ�ʂ�\������
	 * </ul>
	 * 
	 * @param request HTTP���N�G�X�g
	 * @param response HTTP���X�|���X
	 * @exception ServletException ����̃G���[�y�[�W��\��
	 * @exception IOException ����̃G���[�y�[�W��\��
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		
		request.setCharacterEncoding("utf-8");
		
		//�ϐ��̏���
		Connection conn = null;
		RequestDispatcher dispatcher = null;
		String returnURL = "/";
		String st_no = null;	
		List<Map<String,Object>> gettestlist = null;	//�擾����ꗗ
		Map<String,Object> gettestdata = null;
		
		String gettestSQL = "select test.test_no,test.test_name,association.associ_name,testdate.test_perform_date,testdate.test_get_date from association join test on association.associ_no = test.associ_no join testdate on test.test_no = testdate.test_no join gettest on testdate.test_no = gettest.test_no AND testdate.test_perform_date = gettest.test_perform_date where gettest.st_no = ? ORDER BY testdate.test_perform_date DESC";
		
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
			
			//�Z�b�V��������w���ԍ������o��
			st_no = user.getId();
			
			//DB�ڑ��̏���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			//�f�[�^�x�[�X�̎擾��������Q�Ƃ���
			stmt = conn.prepareStatement(gettestSQL);
			stmt.setString(1,st_no);
			rs = stmt.executeQuery();

			gettestlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//�擾����ꗗ�Ɏ擾�������ǉ�
				gettestdata = new HashMap<String,Object>();
				gettestdata.put("test_no",rs.getString("test.test_no"));
				gettestdata.put("test_name",rs.getString("test.test_name"));
				gettestdata.put("association",rs.getString("association.associ_name"));
				gettestdata.put("test_perform_date",rs.getString("testdate.test_perform_date"));
				gettestdata.put("test_get_date",rs.getString("testdate.test_get_date"));
				gettestlist.add(gettestdata);
			}
			rs.close();
			stmt.close();
			
			//�擾����ꗗ���Z�b�V�����Ɋi�[����
			session.setAttribute("list",gettestlist);

			//�߂��URL�̎w��
			returnURL = "./student/st_gettest.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./student/st_gettest.jsp";			
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