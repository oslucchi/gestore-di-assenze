import java.io.Serializable;
import java.util.Date;

public class StudentsSemesterAbsenceList implements Serializable
{
	private static final long serialVersionUID = 1L;

	private DoubleLinkedList semesterAbsence;
	
	public StudentsSemesterAbsenceList()
	{
		semesterAbsence = new DoubleLinkedList();
	}
	
	public void addAbsence(Person person, Date date)
	{
		PersonAbsenceDates onList = (PersonAbsenceDates) semesterAbsence.first(); 
		while((onList != null) && (onList.getPerson().getId() < person.getId()))
		{
			onList = (PersonAbsenceDates) semesterAbsence.after();
		}
		
		if ((onList == null) || (onList.getPerson().getId() != person.getId()))
		{
			PersonAbsenceDates had = new PersonAbsenceDates(person);
			had.addAbsence(date);
			if(onList == null)
			{
				semesterAbsence.insertTail(had);
			}
			else
			{
				semesterAbsence.insertBefore(had);
			}
		}
		else 
		{
			onList.addAbsence(date);
		}
	}

	public DoubleLinkedList getSemesterAbsence() 
	{
		return semesterAbsence;
	}
}