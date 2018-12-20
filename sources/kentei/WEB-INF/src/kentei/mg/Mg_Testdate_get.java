package kentei.mg;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import java.util.*;

import kentei.beans.User;
import kentei.exception.*;

/**
 * ������{���Q�Ƌ@�\.
 * <ul>
 * <li>������{�������e�i���X���(mg_testdate.jsp)����̏������ƂɌ�����Q�ƁA������{�����Q�Ƃ���
 * <li>������{���ꗗ�A����������N�G�X�g�X�R�[�v�Ɋi�[����
 * <li>������{���o�^�E�폜���(mg_testdate_get)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_Testdate_get extends HttpServlet{
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
	 * <li>����ԍ��A������{�����󂯎��
	 * <li>�f�[�^�x�[�X����Y�����錟����{�����Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̌�������Q�Ƃ���
	 * <li>������{���ꗗ�A����������N�G�X�g�X�R�[�v�Ɋi�[����
	 * <li>������{���o�^�E�폜��ʂ�\������
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
		String returnURL="/";		//�߂��URL
		List<Map<String,Object>> datelist = null;		//������{���ꗗ
		Map<String,Object> test_performdate = null;		//������{��
		Map<String,Object> test_data = null;			//����擾��
		String test_no = request.getParameter("test_no");	//���͂��ꂽ����ԍ�
		
		String testdateSQL = "select * from testdate where test_no=? order by test_perform_date desc";	//������{���ꗗ�Q��
		//������Q��
		String testSQL ="select test.test_name,association.associ_name from test join association on test.associ_no = association.associ_no where test.test_no=?";

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
			
			if(test_no != null){
				//����ԍ����w�肳��Ă���ꍇ�A�f�[�^�x�[�X�̌�����{�����Q�Ƃ���
				stmt = conn.prepareStatement(testdateSQL);
				stmt.setString(1,test_no);
				rs = stmt.executeQuery();

				datelist = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					//������{���ꗗ�Ɍ�����{����ǉ�
					test_performdate = new HashMap<String,Object>();
					test_performdate.put("test_perform_date",rs.getString("test_perform_date"));
					test_performdate.put("test_get_date",rs.getString("test_get_date"));
					datelist.add(test_performdate);
				}
				rs.close();
				stmt.close();
				
				//�f�[�^�x�[�X�̌�������Q�Ƃ���
				stmt = conn.prepareStatement(testSQL);
				stmt.setString(1,test_no);
				rs = stmt.executeQuery();
				if(rs.next()){
					//�������ێ�
					test_data = new HashMap<String,Object>();
					test_data.put("test_name",rs.getString("test.test_name"));
					test_data.put("associ_name",rs.getString("association.associ_name"));
				}
				rs.close();
				stmt.close();
			}
			
			//������{���ꗗ�A����������N�G�X�g�X�R�[�v�Ɋi�[����
			request.setAttribute("datelist",datelist);
			request.setAttribute("test_data",test_data);
			
			//�߂��URL�̎w��
			returnURL = "./manager/mg_testdate_insert.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_testdate_insert.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//������{���o�^�E�폜��ʂ�\������
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