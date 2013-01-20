import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;


public class StudentSemesterAbsenceListTest {
	Date date = new Date();
	DoubleLinkedList dl;
	StudentsSemesterAbsenceList testing = new StudentsSemesterAbsenceList();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

	private void printList()
	{
		System.out.println("\n ****************************\nList Content: \n");
		dl = testing.getSemesterAbsence();
		PersonAbsenceDates b = (PersonAbsenceDates) dl.first();
		while(b != null)
		{
			System.out.println(b.getPerson().lastName + " " + b.getPerson().firstName + " " + b.getPerson().contactNumber);
			DoubleLinkedList c = b.getAbsenceDates();
			date = (Date) c.first();
			while (date != null)
			{
				formatter = new SimpleDateFormat("yyyy/MM/dd");
				System.out.print(formatter.format(date) + " ");
				date = (Date) c.after();
			}
			System.out.println();
			b = (PersonAbsenceDates) dl.after();
		}
	}
	@Test

	public void test()
	{
		try 
		{
			date = formatter.parse("2013/01/07");
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Student a = new Student("pineruola", "alberto", 120001, "120001", 12);
		testing.addAbsence(a , date);
		a = new Student("zodiaco", "ciccio", 120010, "120010",12);
		testing.addAbsence(a , date);
		testing.addAbsence(a , new Date());
		a = new Student("pineruola", "alberto", 120100, "120100",12);
		testing.addAbsence(a , date);
		printList();
		
	}

}
