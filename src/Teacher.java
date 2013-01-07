
public class Teacher extends Person
{
	private String password;
	public Teacher(String record)
	{
		super(record);
		role = 'T';
		this.password = record.substring(72, 83);
	}
	
	public Teacher(String lastName, String firstName, int id, String contactNumber) 
	{
		super(lastName, firstName, id, contactNumber);
		role = 'T';
		this.password = "**********";
	}	

	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public String toString()
	{
		String ss = String.format("%-20s%-30s%06d%-15s%c%-10s99",
				lastName, firstName, id, contactNumber, role, password);
		return(ss);
	}
}
