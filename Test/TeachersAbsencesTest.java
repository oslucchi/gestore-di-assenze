import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TeachersAbsencesTest {
	Date date = new Date();
	DoubleLinkedList dl;
	TeachersAbsences testing = new TeachersAbsences();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

	private void printList()
	{
		System.out.println("\n ****************************\nList Content: \n");
		dl = testing.getAbsenceList();
		TeachersAbsences.TeacherAbsenceDates b = (TeachersAbsences.TeacherAbsenceDates) dl.first();
		while(b != null)
		{
			System.out.println(b.getPerson().lastName + " " + b.getPerson().firstName + " " + b.getMedCert());
			DoubleLinkedList c = b.getAbsenceDates();
			date = (Date) c.first();
			while (date != null)
			{
				formatter = new SimpleDateFormat("yyyy/MM/dd");
				System.out.print(formatter.format(date) + " ");
				date = (Date) c.after();
			}
			System.out.println();
			b = (TeachersAbsences.TeacherAbsenceDates) dl.after();
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
		
		Teacher a = new Teacher("pineruola", "alberto", 120001, "120001");
		testing.addAbsence(a , date);
		a = new Teacher("zodiaco", "ciccio", 120010, "120001");
		testing.addAbsence(a , date);
		testing.addAbsence(a , new Date());
		a = new Teacher("pineruola", "alberto", 120100, "120001");
		testing.addAbsence(a , date);
		printList();
		testing.CleanUpTeacherMC();
		printList();
		testing.CleanUpTeacherMC(new Teacher("zodiaco", "ciccio", 120010, "120001"));
		printList();
	}

}
