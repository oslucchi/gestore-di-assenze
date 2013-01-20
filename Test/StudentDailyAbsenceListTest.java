import org.junit.*;

public class StudentDailyAbsenceListTest 
{
	@Test
	public void test() {
		StudentDailyAbsenceList testing = null;
		FileSaveLoad fileMngr = new FileSaveLoad("Test/Data/studentDailyAbsence");
		if ((testing = (StudentDailyAbsenceList) fileMngr.Load()) == null)
		{
			fileMngr = new FileSaveLoad("Test/Data/studentDailyAbsence");
			testing = (StudentDailyAbsenceList) fileMngr.Load(); 
		}
		
		Absence a = new Absence(new Student("consentino", "alberto", 120001, "120001", 12));
		testing.addAbsence(a);
		a = new Absence(new Student("uhfuf", "rewsff", 120010, "120010", 12));
		testing.addAbsence(a);
		a = new Absence(new Student("prorororo", "brutewr", 120100, "120100", 12));
		testing.addAbsence(a);
		DoubleLinkedList dl = testing.getAbsenceList();
		testing.setJustified(new Student("uhfuf", "rewsff", 120010, "120010", 12), 'Y');
		a = (Absence)dl.first();
		while(a != null)
		{
			System.out.println(a.getStudent().lastName + " " + a.getStudent().contactNumber + 
					" " + a.getJustified());
			a = (Absence) dl.after();
		}
		
		fileMngr.Save(testing);
		if ((testing = (StudentDailyAbsenceList) fileMngr.Load()) == null)
		{
			testing = (StudentDailyAbsenceList) fileMngr.Load(); 
		}
		dl = testing.getAbsenceList();
		a = (Absence)dl.first();
		while(a != null)
		{
			System.out.println(a.getStudent().lastName + " " + a.getStudent().contactNumber + 
					" " + a.getJustified());
			a = (Absence) dl.after();
		}
	}

}
