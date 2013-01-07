
public class SchoolCitizen 
{
	RandomAcess teachers = null;
	RandomAcess students = null;
	
	public SchoolCitizen()
	{
		teachers = new RandomAcess("Data/SchoolCitizens", 'T');
		students = new RandomAcess("Data/SchoolCitizens", 'S');
	}
	
	public DoubleLinkedList search(char what, int searchBy, String searchKey)
	{
		if (what == 'S')
			return(students.Search(searchBy, searchKey));
		else if (what == 'T')
			return(teachers.Search(searchBy, searchKey));
		else
			return(null);
	}
	
	public void addElement(String lastName, String firstName, int id,
			String contactNumber, char role, int grade)
	{
		String record = null;
		if (role == 'S')
		{
			Student student = new Student(lastName, firstName, id, contactNumber, grade);
			record = student.toString();
		}
		else if (role == 'T')
		{
			Teacher teacher = new Teacher(lastName, firstName, id, contactNumber);			
			record = teacher.toString();
		}
		students.addRecord(record);
	}

	public void removeElement(int id, char role)
	{
		if(role == 'S')
			students.removeRecord(id);
		else if(role == 'T')
			teachers.removeRecord(id);
	}

}
