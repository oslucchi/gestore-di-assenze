import static org.junit.Assert.*;

import org.junit.Test;


public class SDALtest {

	@Test
	public void test() 
	{
		StudentDailyAbsenceList testing = new StudentDailyAbsenceList();
		Absence a = new Absence(new Student("consentino", "alberto", 120001, "120001", 12));
		testing.addAbsence(a);
		a = new Absence(new Student("uhfuf", "rewsff", 120010, "120010", 12));
		testing.addAbsence(a);
		a = new Absence(new Student("prorororo", "brutewr", 120100, "120100", 12));
		testing.addAbsence(a);
		DoubleLinkedList dl = testing.getAbsenceList();
		testing.setJustified(new Student("uhfuf", "rewsff", 120010, "120010", 12));
		a = (Absence)dl.first();
		while(a != null)
		{
			System.out.println(a.getStudent().lastName + " " + a.getStudent().contactNumber + 
					" " + a.getJustified());
			a = (Absence) dl.after();
		}
	}

}
