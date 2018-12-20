package kentei.st;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.*;
import javax.naming.*;

import kentei.beans.*;
import kentei.exception.TimeoutException;

/**
 * �擾�\���{���Q�Ƌ@�\.
 * <ul>
 * <li>�擾����X�V���(st_addtest.jsp)����󂯎�����������ƂɎ擾�\�Ȏ��{�����Q�Ƃ���
 * <li>������{���ꗗ���Z�b�V�����ɒǉ�����
 * <li>������{���I�����(st_addtest_insert.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class St_addTest_get extends HttpServlet{
	
	/**
	 * <ul>
	 * <li>����ԍ����󂯎��
	 * <li>���͂��ꂽ����ԍ������ƂɃf�[�^�x�[�X�̌�������Q�Ƃ���
	 * <li>�Y�����錟�肪���݂���ꍇ�A�f�[�^�x�[�X�̎擾�ς݌�������Q�Ƃ���
	 * <li>�擾�ς݂łȂ��ꍇ������{�����Q�Ƃ���
	 * <li>������{���ꗗ���Z�b�V�����ɒǉ�����
	 * <li>������{���I����ʂ�\������
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
		String returnURL = "/";			//�߂��URL
		String test_no = request.getParameter("test_no");	//���͂��ꂽ����ԍ�
		List<Map<String,Object>> datelist = null;			//������{���ꗗ
		Map<String,Object> test_date = null;				//������{�����
		
		String testdateSQL = "select * from testdate where test_no=? ORDER BY test_perform_date DESC";	//������{���Q��
		String gettestSQL = "select * from gettest where st_no=? AND test_no = ?";						//�擾�ς݌���Q��
		String testSQL = "select * from test where test_no = ?";		//������Q��
		
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
			ResultSet rs = null;
			
			//�f�[�^�x�[�X�̌�������Q�Ƃ���
			stmt = conn.prepareStatement(testSQL);
			stmt.setString(1,test_no);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				//�Y�����錟�肪���݂���ꍇ�A�f�[�^�x�[�X�̎擾�ς݌�������Q�Ƃ���
				rs.close();
				stmt.close();	
				stmt = conn.prepareStatement(gettestSQL);
				stmt.setString(1,user.getId());
				stmt.setString(2,test_no);
				rs = stmt.executeQuery();
				
				if(!rs.next()){
					//�擾�ς݂łȂ��ꍇ������{�����Q�Ƃ���
					rs.close();
					stmt.close();
					stmt = conn.prepareStatement(testdateSQL);
					stmt.setString(1,test_no);
					rs = stmt.executeQuery();
					
					datelist = new ArrayList<Map<String,Object>>();
					while(rs.next()){
						//������{���ꗗ�Ɍ�����{������ǉ�����
						test_date = new HashMap<String,Object>();
						test_date.put("test_perform_date",rs.getString("test_perform_date"));
						test_date.put("test_get_date",rs.getString("test_get_date"));
						datelist.add(test_date);
						test_date = null;
					}
				}else{
					//�擾�ς݂̏ꍇ���b�Z�[�W�\��
					request.setAttribute("msg","�擾�ς݂ł��B");
				}
				rs.close();
				stmt.close();
			}else{
				request.setAttribute("msg","���݂��Ȃ�����ԍ��ł��B");
			}
			rs.close();
			stmt.close();	
			
			//������{���ꗗ���Z�b�V�����ɒǉ�����
			session.setAttribute("datelist",datelist);
			
			//�߂��URL
			returnURL = "./student/st_addtest_insert.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			dispatcher = request.getRequestDispatcher("/");
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			dispatcher = request.getRequestDispatcher(returnURL);
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//������{���I����ʂ�\������
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