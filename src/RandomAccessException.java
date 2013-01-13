
public class RandomAccessException extends Exception
{
	final static int RECORD_NOT_FOUND = 1;
	
	
	private static final long serialVersionUID = 1L;

	private int errorCode = 0;
	public RandomAccessException(int errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode()
	{
		return(errorCode);
	}
}
