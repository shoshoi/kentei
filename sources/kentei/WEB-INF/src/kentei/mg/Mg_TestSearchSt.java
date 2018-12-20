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
 * ����ꗗ�Q�Ƌ@�\.
 * <ul>
 * <li>������A���{�c�̏����Q�Ƃ���
 * <li>�Z�b�V�����Ɍ���ꗗ�Ǝ��{�c�̈ꗗ���i�[����
 * <li>����ˊw���������(mg_testsearchst.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_TestSearchSt extends HttpServlet{

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
	 * <li>�f�[�^�x�[�X�̌�������Q�Ƃ���
	 * <li>�i���ݎ��{�c�̔ԍ����w�肳��Ă���ꍇ�i���݂��s��
	 * <li>�f�[�^�x�[�X�̎��{�c�̏����Q�Ƃ���
	 * <li>����ꗗ�Ǝ��{�c�̈ꗗ���Z�b�V�����i�[����
	 * <li>������{�������e�i���X��ʂ�\������
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

		List<Map<String,Object>> list = null;	//����ꗗ
		List<Map<String,Object>> associlist = new ArrayList<Map<String,Object>>();	//���{�c�̈ꗗ
		Map<String,Object> testdata = null;		//������
		Map<String,Object> associdata = null;	//���{�c�̏��
		String limit_associ = request.getParameter("associ");		//���͂��ꂽ�i���ݎ��{�c�̔ԍ�(0�̏ꍇ�i���݂Ȃ�)

		//������Q��
		String testSQL = "select test.test_no,test.test_name,association.associ_name from test join association on test.associ_no = association.associ_no order by test.test_no asc";
		//������Q�Ɓi���{�c�̍i���݁j
		String testSQL_limitAssoci = "select test.test_no,test.test_name,association.associ_name from test join association on test.associ_no = association.associ_no where association.associ_no = ? order by association.associ_no asc,test.test_no asc";
		//���{�c�̎Q��
		String associSQL = "select associ_no,associ_name from association";
		
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
			
			//�f�[�^�x�[�X���猟������Q��
			if(limit_associ == null) limit_associ = "0";	//�i���ݎw�肪�Ȃ���΍i���݂Ȃ��Ƃ݂Ȃ�			
			if(limit_associ.equals("0")){
				//�i�荞�݂Ȃ�
				stmt = conn.prepareStatement(testSQL);
			}else{
				//�w�ȍi����
				stmt = conn.prepareStatement(testSQL_limitAssoci);
				stmt.setString(1,limit_associ);
			}
			rs = stmt.executeQuery();
			
			list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//����ꗗ�Ɍ������ǉ�
				testdata = new HashMap<String,Object>();
				testdata.put("test_no",rs.getString("test.test_no"));
				testdata.put("test_name",rs.getString("test.test_name"));
				testdata.put("associ_name",rs.getString("association.associ_name"));
				list.add(testdata);
			}
			rs.close();
			stmt.close();
			
			//�f�[�^�x�[�X������{�c�̏����Q��
			stmt = conn.prepareStatement(associSQL);
			rs = stmt.executeQuery();
			
			associlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//���{�c�̈ꗗ�Ɏ��{�c�̏���ǉ�
				associdata = new HashMap<String,Object>();
				associdata.put("associ_no",rs.getString("associ_no"));
				associdata.put("associ_name",rs.getString("associ_name"));
				associlist.add(associdata);
			}
			rs.close();
			stmt.close();
			
			//����ꗗ�A���{�c�̈ꗗ���Z�b�V�����Ɋi�[
			session.setAttribute("list",list);
			session.setAttribute("associlist",associlist);
			
			//�i���ݎ��{�c�̔ԍ������N�G�X�g�X�R�[�v�Ɋi�[
			request.setAttribute("limit_associ",limit_associ);
			
			returnURL = "./manager/mg_testsearchst.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_testsearchst.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//����ˊw��������ʂ�\��
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