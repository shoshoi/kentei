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
 * ������Q�Ƌ@�\.
 * <ul>
 * <li>�f�[�^�x�[�X�̌���̏����Q�Ƃ���
 * <li>�Z�b�V�����Ɍ���̏����i�[����
 * <li>������X�V���(mg_maintetestmaster_update.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteTestmaster_get extends HttpServlet{
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
	 * <li>�w���}�X�^�����e�i���X��ʂ�\������
	 * <li>�f�[�^�x�[�X������{�c�̂̏����Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̌�����A���{�c�̈ꗗ���Z�b�V�����Ɋi�[����
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
		String returnURL = "/"; 	//�߂��URL

		List<Map<String,Object>> associlist = null;	//���{�c�̈ꗗ
		Map<String,Object> associ_data = null;		//���{�c�̏��
		Map<String,Object> test_data = null;		//������
		String test_no = request.getParameter("test_no");		//���͂��ꂽ����ԍ�
		String newflg = request.getParameter("newflg");			//���͂��ꂽ�V�K�o�^�t���O
		
		String testSQL = "select * from test where test_no=?";	//������Q��
		String associSQL = "select * from association";			//���{�c�̏��Q��
		
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
				//����ԍ����w�肳��Ă���ꍇ�̂݃f�[�^�x�[�X�̌�������Q�Ƃ���
				stmt = conn.prepareStatement(testSQL);
				stmt.setString(1,test_no);
				rs = stmt.executeQuery();
				if(rs.next()){
					//����ꗗ�Ɍ������ǉ�
					test_data = new HashMap<String,Object>();
					test_data.put("test_no",rs.getString("test_no"));
					test_data.put("test_name",rs.getString("test_name"));
					test_data.put("associ_no",rs.getString("associ_no"));
				}
				rs.close();
				stmt.close();
			}
			
			//�f�[�^�x�[�X������{�c�̂̏����Q�Ƃ���
			stmt = conn.prepareStatement(associSQL);
			rs = stmt.executeQuery();
			associlist = new ArrayList<Map<String,Object>>();
			
			while(rs.next()){
				//���{�c�̈ꗗ�Ɏ��{�c�̏���ǉ�
				associ_data = new HashMap<String,Object>();
				associ_data.put("associ_no",rs.getString("associ_no"));
				associ_data.put("associ_name",rs.getString("associ_name"));
				associ_data.put("associ_kana",rs.getString("associ_kana"));
				associlist.add(associ_data);
				associ_data=null;
			}
			rs.close();
			stmt.close();
			
			//�V�K�o�^�t���O�����N�G�X�g�X�R�[�v�Ɋi�[����
			request.setAttribute("newflg", newflg);
			
			//�f�[�^�x�[�X�̌�����A���{�c�̈ꗗ���Z�b�V�����Ɋi�[����
			session.setAttribute("test_data",test_data);
			session.setAttribute("associlist",associlist);
			
			//�߂��URL�̐ݒ�
			returnURL = "./manager/mg_maintetestmaster_update.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_maintetestmaster_update.jsp";
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