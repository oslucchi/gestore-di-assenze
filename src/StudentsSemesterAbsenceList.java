import java.util.Date;

public class StudentsSemesterAbsenceList 
{
	private DoubleLinkedList semesterAbsence;
	
	public StudentsSemesterAbsenceList()
	{
		
	}
	
	@SuppressWarnings("unused")
	public void addAbsence(Person person, Date date)
	{
		PersonAbsenceDates onList = (PersonAbsenceDates) semesterAbsence.first(); 
		while((onList != null) && (onList.getPerson().getId() < person.getId()))
		{
			onList = (PersonAbsenceDates) semesterAbsence.after();
		}
		
		if (onList.getPerson().getId() == person.getId())
		{
			onList.addAbsence(date);
		}
		else 
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
	}

	public DoubleLinkedList getSemesterAbsence() 
	{
		return semesterAbsence;
	}
}