import java.io.Serializable;


public class Person implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected int id;
	protected String lastName;
	protected String firstName;
	protected String contactNumber;
	protected char role;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getLastName() 
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName.toUpperCase();
	}
	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = Utils.toProperCase(firstName);
	}
	public String getContactNumber() 
	{
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) 
	{
		this.contactNumber = contactNumber;
	}
	public char getRole() 
	{
		return role;
	}
	public void setRole(char role) 
	{
		this.role = role;
	}
	
	public Person(String record)
	{
		this.lastName = record.substring(0, 20).trim().toUpperCase();
		this.firstName = Utils.toProperCase(record.substring(20, 50).trim());
		this.id = Integer.parseInt(record.substring(50, 56).trim());
		this.contactNumber = record.substring(56, 71).trim();
	}
	
	public Person(String lastName, String firstName, int id, String contactNumber) 
	{
		this.lastName = lastName.toUpperCase();
		this.firstName = Utils.toProperCase(firstName);
		this.id = id;
		this.contactNumber = contactNumber;
	}
	
	public Person() 
	{
		// TODO Auto-generated constructor stub
	}

}
