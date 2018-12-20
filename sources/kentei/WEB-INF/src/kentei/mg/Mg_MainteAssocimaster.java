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
 * ���{�c�̏�񃁃��e�i���X�@�\.
 * <ul>
 * <li>���{�c�̏����Q�Ƃ���
 * <li>���{�c�̏������N�G�X�g�X�R�[�v�Ɋi�[����
 * <li>���{�c�̃����e�i���X���(mg_mainteassocimaster.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */

@SuppressWarnings("serial")
public class Mg_MainteAssocimaster extends HttpServlet{

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
	 * <li>�f�[�^�x�[�X�̎��{�c�̏����Q�Ƃ���
	 * <li>���b�Z�[�W�Ƌ��Ɏ��{�c�̃����e�i���X��ʂ�\������
	 * </ul>
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
		List<Map<String,Object>> associlist = null;	//���{�c�̈ꗗ
		Map<String,Object> associdata = null;		//���{�c�̏��
		
		String associSQL = "select associ_no,associ_name from association";	//������Q��
		
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

			//�f�[�^�x�[�X�̎��{�c�̏����Q�Ƃ���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
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
			
			//�f�[�^�x�[�X�̎��{�c�̈ꗗ�����N�G�X�g�X�R�[�v�Ɋi�[����
			request.setAttribute("associlist",associlist);
			
			//�߂��URL�̐ݒ�
			returnURL = "./manager/mg_mainteassocimaster.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_mainteassocimaster.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//���{�c�̃����e�i���X��ʂ�\������
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