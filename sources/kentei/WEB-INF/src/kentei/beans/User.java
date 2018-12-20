package kentei.beans;

/**
 * ユーザー情報管理.
 * ログインユーザー情報を格納するクラス。各機能から参照する。
 * 
 * @version Release-1.0
 * @author S.Ishikawa
 */

public class User{
	private String id;		//ユーザーID
	private String name;	//ユーザー名
	private int loginKubun;	//ログイン区分(0=学生,1=管理者)

	/**
	 * ユーザー情報初期化
	 * 
	 * @param id ユーザーID
	 * @param name ユーザー名
	 * @param loginKubun ログイン区分
	 */
	public User(String id,String name,int loginKubun){
		this.id=id;
		this.name=name;
		this.loginKubun=loginKubun;
	}
	
	/**
	 * ユーザーIDセッター
	 * 
	 * @param id ユーザーID
	 */
	public void setId(String id){
		this.id=id;
	}
	
	/**
	 * ユーザー名セッター
	 * 
	 * @param name ユーザー名
	 */
	public void setName(String name){
		this.name=name;
	}
	
	/**
	 * ログイン区分セッター
	 * 
	 * @param loginKubun ログイン区分
	 */
	public void setLoginKubun(int loginKubun){
		this.loginKubun=loginKubun;
	}
	
	/**
	 * ユーザーIDゲッター
	 * 
	 * @return id ユーザーID
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * ユーザー名ゲッター
	 * 
	 * @return name ユーザー名
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * ログイン区分ゲッター
	 * 
	 * @return loginKubun ログイン区分
	 */
	public int getLoginKubun(){
		return this.loginKubun;
	}
}