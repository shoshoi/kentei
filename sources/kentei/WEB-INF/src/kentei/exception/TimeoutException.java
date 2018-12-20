package kentei.exception;

/**
 * 例外処理:タイムアウト.
 * セッションタイムアウトの例外を示す
 * 
 * @version Release-1.0
 * @author S.Ishikawa
 */
@SuppressWarnings("serial")
public class TimeoutException extends Exception {
	public TimeoutException(String message) {
	    super(message);
	  }
}
