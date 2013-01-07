import java.util.Date;

public class PersonAbsenceDates
{
	private Person person;
	private DoubleLinkedList absenceDates;
	public PersonAbsenceDates(Person person)
	{
		this.person = person;
		absenceDates = new DoubleLinkedList();
	}
	
	public void addAbsence(Date absenceDate)
	{
		absenceDates.insertTail(absenceDate);
	}
	
	public DoubleLinkedList getAbsenceDates()
	{
		return (absenceDates);
	}
	
	public Person getPerson()
	{
		return (person);
	}
}
