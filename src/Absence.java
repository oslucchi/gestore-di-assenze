public class Absence {
	
	private char justified;
	private Student student;
	public Absence(Student student) 
	{
		this.student = student;
		justified = 'W';
	}

	public char getJustified()
	{
		return justified;
	}

	public void setJustified(char justified)
	{
		this.justified = justified;
	}

	public Student getStudent()
	{
		return student;
	}

	public void setStudent(Student student)
	{
		this.student = student;
	}
}
