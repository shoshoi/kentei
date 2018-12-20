package kentei.beans;

/**
 * ���[�U�[���Ǘ�.
 * ���O�C�����[�U�[�����i�[����N���X�B�e�@�\����Q�Ƃ���B
 * 
 * @version Release-1.0
 * @author S.Ishikawa
 */

public class User{
	private String id;		//���[�U�[ID
	private String name;	//���[�U�[��
	private int loginKubun;	//���O�C���敪(0=�w��,1=�Ǘ���)

	/**
	 * ���[�U�[��񏉊���
	 * 
	 * @param id ���[�U�[ID
	 * @param name ���[�U�[��
	 * @param loginKubun ���O�C���敪
	 */
	public User(String id,String name,int loginKubun){
		this.id=id;
		this.name=name;
		this.loginKubun=loginKubun;
	}
	
	/**
	 * ���[�U�[ID�Z�b�^�[
	 * 
	 * @param id ���[�U�[ID
	 */
	public void setId(String id){
		this.id=id;
	}
	
	/**
	 * ���[�U�[���Z�b�^�[
	 * 
	 * @param name ���[�U�[��
	 */
	public void setName(String name){
		this.name=name;
	}
	
	/**
	 * ���O�C���敪�Z�b�^�[
	 * 
	 * @param loginKubun ���O�C���敪
	 */
	public void setLoginKubun(int loginKubun){
		this.loginKubun=loginKubun;
	}
	
	/**
	 * ���[�U�[ID�Q�b�^�[
	 * 
	 * @return id ���[�U�[ID
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * ���[�U�[���Q�b�^�[
	 * 
	 * @return name ���[�U�[��
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * ���O�C���敪�Q�b�^�[
	 * 
	 * @return loginKubun ���O�C���敪
	 */
	public int getLoginKubun(){
		return this.loginKubun;
	}
}