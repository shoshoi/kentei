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
 * �w���}�X�^�����e�i���X�@�\.
 * <ul>
 * <li>�w���A�w�Ȃ̏����Q�Ƃ���
 * <li>�w���ꗗ�A�w�Ȉꗗ���Z�b�V�����Ɋi�[����
 * <li>�w���}�X�^�����e�i���X���(/mg_maintestmaster)�֑J�ڂ���
 * </ul>
 *
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class Mg_MainteStmaster extends HttpServlet{
	
	/**
	 * ������ @doGet �Ɉڍs����
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
	 * <li>�f�[�^�x�[�X�̊w���̈ꗗ���Q�Ƃ���
	 * <li>�i���݊w�Ȕԍ��A�i���݊w�N�ԍ����w�肳��Ă���ꍇ�i���݂��s��
	 * <li>�f�[�^�x�[�X����w�Ȃ̈ꗗ���擾����
	 * <li>�w���ꗗ�A�w�Ȉꗗ���Z�b�V�����Ɋi�[����
	 * <li>�i���݊w�Ȕԍ��A�i���݊w�N�����N�G�X�g�X�R�[�v�Ɋi�[����
	 * <li>�w���}�X�^�����e�i���X��ʂ�\��
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
		String returnURL ="/";		//�߂��URL

		List<Map<String,Object>> stlist = null;			//�w���ꗗ
		List<Map<String,Object>> classlist = null;		//�w�Ȉꗗ
		Map<String,Object> strecord = null;				//�w�����
		Map<String,Object> classrecord = null;			//�w�ȏ��
		String limit_class = request.getParameter("class");		//���͂��ꂽ�i���݊w�Ȕԍ�
		String limit_year = request.getParameter("year");		//���͂��ꂽ�i���݊w�N�ԍ�
		
		/*�@�w���Q��SQL
		 * stSQL : �i���݂Ȃ�
		 * stSQL_limitClass : �w�ȍi����
		 * stSQL_limitYear : �w�N�i����
		 * stSQL_limitClassYear : �w�ȁE�w�N�i����
		 */
		String stSQL = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no order by class.class_no ASC,student.st_no+0 asc";
		String stSQL_limitClass = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no where student.class_no = ? order by class.class_no ASC,student.st_no+0 asc";
		String stSQL_limitYear = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no where student.year = ? order by class.class_no ASC,student.st_no+0 asc";
		String stSQL_limitClassYear = "select student.st_no as st_no, class.class_name as class_name,student.year as year,student.st_name as st_name from student join class on student.class_no = class.class_no where student.class_no = ? AND student.year = ? order by class.class_no ASC,student.st_no+0 asc";
		
		String classSQL = "select class_no,class_name from class";		//�w�Ȉꗗ�擾SQL

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

			//�f�[�^�x�[�X�̊w���̈ꗗ���Q�Ƃ���
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/MySQL");
			conn = ds.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			//�i���ݎw�肪�Ȃ��ꍇ�i�荞�݂Ȃ��Ƃ݂Ȃ�
			if(limit_class == null) limit_class = "0";
			if(limit_year == null) limit_year = "0";
			
			if(limit_class.equals("0") && limit_year.equals("0")){
				//�i�荞�݂Ȃ�
				stmt = conn.prepareStatement(stSQL);
			}else if(!limit_class.equals("0") && limit_year.equals("0")){
				//�w�Ȃ̂ݍi����
				stmt = conn.prepareStatement(stSQL_limitClass);
				stmt.setString(1,limit_class);
			}else if(limit_class.equals("0") && !limit_year.equals("0")){
				//�w�N�̂ݍi����
				stmt = conn.prepareStatement(stSQL_limitYear);
				stmt.setString(1,limit_year);
			}else{
				//�w�ȁE�w�N�i�荞��
				stmt = conn.prepareStatement(stSQL_limitClassYear);
				stmt.setString(1,limit_class);
				stmt.setString(2,limit_year);
			}
			rs = stmt.executeQuery();
			
			stlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//�w���ꗗ�Ɋw������ǉ�
				strecord = new HashMap<String,Object>();
				strecord.put("st_no",rs.getString("st_no"));
				strecord.put("class_name",rs.getString("class_name"));
				strecord.put("year",rs.getString("year"));
				strecord.put("st_name",rs.getString("st_name"));
				stlist.add(strecord);
			}
			rs.close();
			stmt.close();
			
			//�f�[�^�x�[�X����w�Ȃ̈ꗗ���擾����
			stmt = conn.prepareStatement(classSQL);
			rs = stmt.executeQuery();
			
			classlist = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				//�w�Ȉꗗ�Ɋw�ȏ���ǉ�
				classrecord = new HashMap<String,Object>();
				classrecord.put("class_no",rs.getString("class_no"));
				classrecord.put("class_name",rs.getString("class_name"));
				classlist.add(classrecord);
			}
			rs.close();
			stmt.close();
			
			//�w���ꗗ�A�w�Ȉꗗ���Z�b�V�����Ɋi�[����
			session.setAttribute("list",stlist);
			session.setAttribute("classlist",classlist);
			
			//�i���݊w�Ȕԍ��A�i���݊w�N�����N�G�X�g�X�R�[�v�Ɋi�[����
			request.setAttribute("limit_class",limit_class);
			request.setAttribute("limit_year",limit_year);
			
			//�߂��URL�̎w��
			returnURL = "./manager/mg_maintestmaster.jsp";
		}catch(TimeoutException e){
			request.setAttribute("msg", e.getMessage());
			returnURL = "/index.jsp";
		}catch(SQLException e){
			request.setAttribute("msg","�f�[�^����Ɏ��s���܂����B");
			returnURL = "./manager/mg_maintestmaster.jsp";
		}catch(Exception e){
			//����̃G���[�y�[�W��\��
			throw new ServletException(e);
		}finally{
			//�w���}�X�^�����e�i���X��ʂ�\������
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