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
 * �w���ꗗ�Q�Ƌ@�\.
 * <ul>
 * <li>�w�����A������{�����A��������Q�Ƃ���
 * <li>����ꗗ�A���{���ꗗ�A���薼�A����ԍ����Z�b�V�����Ɋi�[����
 * <li>����ˊw���������ʕ\�����(mg_testsearchst_get.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")

public class Mg_TestSearchSt_get extends HttpServlet{

	/**
	 * <ul>
	 * <li>����ԍ��A�i���ݓ������󂯎��
	 * <li>�f�[�^�x�[�X�̊w�������Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̌�����{�����Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̌��薼���Q�Ƃ���
	 * <li>����ꗗ�A���{���ꗗ�A���薼�A����ԍ����Z�b�V�����Ɋi�[����
	 * <li>����ˊw���������ʕ\����ʂ�\������
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
		String returnURL = "/";		//�߂��URL
		String test_name = null;	//�f�[�^�x�[�X����擾�������薼
		List<Map<String,Object>> stlist = new ArrayList<Map<String,Object>>();		//�w���ꗗ
		List<Map<String,Object>> datelist = new ArrayList<Map<String,Object>>();	//���{���ꗗ
		Map<String,Object> stdata = null;	//�w�����
		Map<String,Object> datedata = null;	//���{��
		String test_no = request.getParameter("test_no");		//���͂��ꂽ����ԍ�
		String limit_date = request.getParameter("date");		//���͂��ꂽ�i���ݓ���
		
		/*�@
		 * stSQL : �w�����Q��
		 * stSQL_limitDate : �w�����Q��(�w�ȍi����)
		 * testdateSQL : ���{���Q��
		 * testSQL : ������Q��
		 */
		String stSQL = "select student.st_no,student.st_name,class.class_name,student.year,gettest.test_perform_date,testdate.test_get_date from student join class on student.class_no = class.class_no join gettest on student.st_no = gettest.st_no join testdate on gettest.test_perform_date = testdate.test_perform_date AND gettest.test_no = testdate.test_no where gettest.test_no = ?";
		String stSQL_limitDate = "select student.st_no,student.st_name,class.class_name,student.year,gettest.test_perform_date,testdate.test_get_date from student join class on student.class_no = class.class_no join gettest on student.st_no = gettest.st_no join testdate on gettest.test_perform_date = testdate.test_perform_date AND gettest.test_no = testdate.test_no where gettest.test_no = ? AND gettest.test_perform_date = ?";
		String testdateSQL = "select testdate.test_perform_date from testdate where testdate.test_no = ?";
		String testSQL = "select test_name from test where test_no = ?";
		
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
			ResultSet rs = null;
			
			//�f�[�^�x�[�X�̊w�������Q�Ƃ���
			if(limit_date == null) limit_date = "0";
			if(limit_date.equals("0")){
				stmt = conn.prepareStatement(stSQL);
				stmt.setString(1,test_no);
			}else{
				stmt = conn.prepareStatement(stSQL_limitDate);
				stmt.setString(1,test_no);
				stmt.setString(2,limit_date);
			}
			rs = stmt.executeQuery();

			while(rs.next()){
				//�w���ꗗ�Ɋw������ǉ�
				stdata = new HashMap<String,Object>();
				stdata.put("st_no",rs.getString("student.st_no"));
				stdata.put("st_name",rs.getString("student.st_name"));
				stdata.put("class_name",rs.getString("class.class_name"));
				stdata.put("year",rs.getString("student.year"));
				stdata.put("test_perform_date",rs.getString("gettest.test_perform_date"));
				stdata.put("test_get_date",rs.getString("testdate.test_get_date"));
				stlist.add(stdata);
			}
			
			rs.close();
			stmt.close();
			
			//�f�[�^�x�[�X�̌�����{�����Q�Ƃ���
			stmt = conn.prepareStatement(testdateSQL);
			stmt.setString(1,test_no);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				//����ꗗ�Ɍ�����{����ǉ�
				datedata = new HashMap<String,Object>();
				datedata.put("test_perform_date",rs.getString("testdate.test_perform_date"));
				datelist.add(datedata);
			}
			rs.close();
			stmt.close();
			
			//�f�[�^�x�[�X�̌��薼���Q�Ƃ���
			stmt = conn.prepareStatement(testSQL);
			stmt.setString(1,test_no);
			rs = stmt.executeQuery();
			if(rs.next()){
				test_name = rs.getString("test_name");
			}
			rs.close();
			stmt.close();
			
			//����ꗗ�A���{���ꗗ�A���薼�A����ԍ����Z�b�V�����Ɋi�[����
			session.setAttribute("list",stlist);
			session.setAttribute("datelist",datelist);
			session.setAttribute("test_name",test_name);
			session.setAttribute("test_no",test_no);
			
			//�i���ݓ��������N�G�X�g�X�R�[�v�Ɋi�[����
			request.setAttribute("limit_date",limit_date);
			
			//�߂��URL�̐ݒ�
			returnURL = "./manager/mg_testsearchst_get.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_testsearchst_get.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//����ˊw���������ʕ\����ʂ�\��
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