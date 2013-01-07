
public class OfficeAdmin extends Person
{
	private String password;

	public OfficeAdmin(String record)
	{
		super(record);
		role = 'A';
		this.password = record.substring(72, 83);
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
}
