import java.util.Date;
public class StudentDailyAbsenceList {

	private DoubleLinkedList absenceList;
	private Date date;
	
	public StudentDailyAbsenceList()
	{
		date = new Date();
		absenceList = new DoubleLinkedList();
	}

	public void setJustified(Person person)
	{
		Absence onList = (Absence) absenceList.first(); 
		while((onList != null) && (onList.getStudent().getId() != person.getId()))
		{
			onList = (Absence) absenceList.after();
		}
		
		if (onList != null)
		{
			onList.setJustified('Y');
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
