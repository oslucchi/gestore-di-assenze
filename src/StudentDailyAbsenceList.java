import java.io.Serializable;
import java.util.Date;

public class StudentDailyAbsenceList implements Serializable
{
	private static final long serialVersionUID = 1L;

	private DoubleLinkedList absenceList;
	private Date date;
	
	public StudentDailyAbsenceList(String fileName)
	{
		date = new Date();
		absenceList = new DoubleLinkedList();
	}
	
	public void setJustified(Person person, char justified)
	{
		Absence onList = (Absence) absenceList.first(); 
		while((onList != null) && (onList.getStudent().getId() != person.getId()))
		{
			onList = (Absence) absenceList.after();
		}
		
		if (onList != null)
		{
			onList.setJustified(justified);
		}
	}
	
	public void addAbsence(Absence absence)
	{
		this.absenceList.insertTail(absence);
	}

	public DoubleLinkedList getAbsenceList() 
	{
		return absenceList;
	}

	public Date getDate() 
	{
		return date;
	}
}
