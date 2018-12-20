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
 * �w�����Q�Ƌ@�\.
 * <ul>
 * <li>�w���}�X�^�����e�i���X���(mg_maintestmaster.jsp)����w�肳�ꂽ�������ƂɊw���l�̏����Q�Ƃ���
 * <li>�w���l�����Z�b�V�����Ɋi�[����
 * <li>�w�Ȉꗗ���Z�b�V�����Ɋi�[����
 * <li>�w�����X�V���(mg_maintestmaster_update.jsp)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class Mg_MainteStmaster_get extends HttpServlet{

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
	 * <li>�w���ԍ����w�肳��Ă���ꍇ�A�Y������w���l�̏����f�[�^�x�[�X����Q�Ƃ���
	 * <li>�f�[�^�x�[�X�̊w�ȏ����Q�Ƃ���
	 * <li>�f�[�^�x�[�X���Q�Ƃ����w���l�����Z�b�V�����Ɋi�[����
	 * <li>�f�[�^�x�[�X���Q�Ƃ����w�Ȉꗗ���Z�b�V�����Ɋi�[����
	 * <li>�w�����X�V��ʂ�\������
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
		List<Map<String,Object>> classlist = null;		//�w�Ȉꗗ
		Map<String,Object> class_data = null;			//�w�ȏ��
		Map<String,Object> st_data = null;				//�w�����
		
		String st_no = request.getParameter("st_no");				//���͂��ꂽ�w���ԍ�
		String stSQL = "select * from student where st_no=?";		//�w�����Q��
		String classSQL = "select class_no,class_name from class";	//�w�ȏ��Q��
		
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
			
			if(st_no!=null){
				//�w���ԍ����w�肳��Ă���ꍇ�̂݃f�[�^�x�[�X�̊w���l�����Q�Ƃ���
				stmt = conn.prepareStatement(stSQL);
				stmt.setString(1,st_no);
				rs = stmt.executeQuery();
				
				st_data = new HashMap<String,Object>();
				if(rs.next()){
					//�w�����̕ێ�
					st_data.put("st_no",rs.getString("st_no"));
					st_data.put("st_name",rs.getString("st_name"));
					st_data.put("st_kana",rs.getString("st_kana"));
					st_data.put("class_no",rs.getString("class_no"));
					st_data.put("year",rs.getString("year"));
				}
				rs.close();
				stmt.close();
			}
			
			//�f�[�^�x�[�X�̊w�ȏ����Q�Ƃ���
			stmt = conn.prepareStatement(classSQL);
			rs = stmt.executeQuery();
			classlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//�w�Ȉꗗ�Ɋw�ȏ���ǉ�
				class_data = new HashMap<String,Object>();
				class_data.put("class_no",rs.getString("class_no"));
				class_data.put("class_name",rs.getString("class_name"));
				classlist.add(class_data);
				class_data=null;
			}
			rs.close();
			stmt.close();
			
			//�f�[�^�x�[�X�̊w���l���A�w�Ȉꗗ�����Z�b�V�����Ɋi�[����
			session.setAttribute("st_data",st_data);
			session.setAttribute("classlist",classlist);
			
			//�߂��URL�̐ݒ�
			returnURL = "./manager/mg_maintestmaster_update.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_maintestmaster_update.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//�w�����X�V��ʂ�\������
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