import java.io.Serializable;
import java.util.Date;

public class PersonAbsenceDates implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
