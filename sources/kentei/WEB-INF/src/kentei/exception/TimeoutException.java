package kentei.exception;

/**
 * ��O����:�^�C���A�E�g.
 * �Z�b�V�����^�C���A�E�g�̗�O������
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
