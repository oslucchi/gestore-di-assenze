import java.io.FileNotFoundException;


public class SchoolCitizen 
{
	RandomAccess teachers = null;
	RandomAccess students = null;
	RandomAccess admins = null;
	
	public SchoolCitizen(String dataPath) throws FileNotFoundException
	{
		teachers = new RandomAccess(dataPath, 'T');
		students = new RandomAccess(dataPath, 'S');
		admins = new RandomAccess(dataPath, 'A');
	}
	
	public DoubleLinkedList search(char what, int searchBy, String searchKey)
	{
		if (what == 'S')
			return(students.Search(searchBy, searchKey));
		else if (what == 'T')
			return(teachers.Search(searchBy, searchKey));
		else  if (what == 'A')
			return(admins.Search(searchBy, searchKey));
		else
			return(null);
	}
	
	public void addElement(char role, String lastName, String firstName, String contactNumber, int grade)
	{
		String record = null;
		if (role == 'S')
		{
			Student student = new Student(lastName, firstName, students.generateId(), contactNumber, grade);
			record = student.toString();
			students.addRecord(record);
		}
		else if (role == 'T')
		{
			Teacher teacher = new Teacher(lastName, firstName, teachers.generateId(), contactNumber);			
			record = teacher.toString();
			teachers.addRecord(record);
		}
		else if (role == 'A')
		{
			OfficeAdmin admin = new OfficeAdmin(lastName, firstName, admins.generateId(), contactNumber);			
			record = admin.toString();
			admins.addRecord(record);
		}
	}

	public void removeElement(int id, char role) throws RandomAccessException
	{
		if(role == 'S')
			students.removeRecord(id);
		else if(role == 'T')
			teachers.removeRecord(id);
		else if(role == 'A')
			admins.removeRecord(id);
	}
	
	public void replaceElement(Person person)
	{
		if(person.getRole() == 'S')
			students.replaceRecord(person.getId(), ((Student) person).toString());
		else if(person.getRole()  == 'T')
			teachers.replaceRecord(person.getId(), ((Teacher) person).toString());
		else if(person.getRole()  == 'A')
			admins.replaceRecord(person.getId(), ((OfficeAdmin) person).toString());
	}

}
