package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import java.util.*;

import kentei.beans.User;
import kentei.exception.TimeoutException;

/**
 * �w���擾����Q�Ƌ@�\.
 * <ul>
 * <li>�w���擾������A�w�������Q�Ƃ���
 * <li>�Z�b�V�����Ɋw���擾����ꗗ�A�w�������i�[����
 * <li>�w���擾����ꗗ�\�����(mg_stsearchtest_get.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_StSearchTest_get extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>�f�[�^�x�[�X����w���擾������Q�Ƃ���
	 * <li>POST���ꂽ�w���ԍ������ƂɃf�[�^�x�[�X�̊w���̖��O���Q�Ƃ���
	 * <li>�Z�b�V�����Ɋw���擾����ꗗ�A�w�������i�[����
	 * <li>�w���擾����ꗗ�\����ʂ�\������
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
		List<Map<String,Object>> gettestlist = null;	//�w���擾����ꗗ
		Map<String,Object> testdata;					//������
		String st_name = "";							//�w����
		String st_no = request.getParameter("st_no");	//���͂��ꂽ�w���ԍ�
		
		//�w���擾����Q��
		String gettestSQL = "select test.test_no,test.test_name,association.associ_name,testdate.test_perform_date,testdate.test_get_date from association join test on association.associ_no = test.associ_no join testdate on test.test_no = testdate.test_no join gettest on testdate.test_no = gettest.test_no AND testdate.test_perform_date = gettest.test_perform_date where gettest.st_no = ? ORDER BY testdate.test_perform_date DESC";
		String stSQL = "select student.st_name from student where st_no = ?";	//�w�����Q��
		
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

			//�f�[�^�x�[�X�̊w���擾��������Q�Ƃ���
			stmt = conn.prepareStatement(gettestSQL);
			stmt.setString(1,st_no);
			ResultSet rs = stmt.executeQuery();
			
			gettestlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//����ꗗ�Ɍ������ǉ�
				testdata = new HashMap<String,Object>();
				testdata.put("test_no",rs.getString("test.test_no"));
				testdata.put("test_name",rs.getString("test.test_name"));
				testdata.put("association",rs.getString("association.associ_name"));
				testdata.put("test_perform_date",rs.getString("testdate.test_perform_date"));
				testdata.put("test_get_date",rs.getString("testdate.test_get_date"));
				gettestlist.add(testdata);
			}
			rs.close();
			stmt.close();
			
			//�f�[�^�x�[�X�̊w���̖��O���Q�Ƃ���
			stmt = conn.prepareStatement(stSQL);
			stmt.setString(1,st_no);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				//�w�����ێ�
				st_name = rs.getString("student.st_name");
				rs.close();
				stmt.close();
			}
			
			//�Z�b�V�����Ɋw���擾����ꗗ�A�w�������i�[����
			session.setAttribute("list",gettestlist);
			session.setAttribute("st_name",st_name);

			//�߂��URL�̎w��
			returnURL = "./manager/mg_stsearchtest_get.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_stsearchtest_get.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//�w���擾����ꗗ�\����ʂ�\������
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