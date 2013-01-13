public class Student extends Person
{
	private static final long serialVersionUID = 1L;

	private int grade;
	
	public Student()
	{
		super();
	}
	
	public Student(String record)
	{
		super(record);
		role = 'S';
		this.grade = Integer.parseInt(record.substring(82, 84));
	}

	public Student(String lastName, String firstName, int id, String contactNumber, int grade) 
	{
		super(lastName, firstName, id, contactNumber);
		role = 'S';
		this.grade = grade;
	}

	public int getGrade() 
	{
		return grade;
	}
	
	public void setGrade(int grade)
	{
		this.grade = grade;
	}
	
	public String toString()
	{
		String ss = String.format("%-20s%-30s%06d%-15s%c          %02d",
				lastName, firstName, id, contactNumber, role, grade);
		return(ss);
	}
}
