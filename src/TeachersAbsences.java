import java.text.SimpleDateFormat;
import java.util.Date;

public class TeachersAbsences 
{
	@SuppressWarnings("unused")
	private class TeacherAbsenceDates extends PersonAbsenceDates
	{
		private char medCert;		
		public TeacherAbsenceDates(Person person)
		{
			super(person);
			medCert = ' ';
		}
		private char getMedCert() {
			return medCert;
		}
		private void setMedCert(char medCert) {
			this.medCert = medCert;
		}
	}
	DoubleLinkedList absenceList;
	public TeachersAbsences()
	{
		absenceList = new DoubleLinkedList();
	}
	
	@SuppressWarnings("unused")
	public void addAbsence(Person person, Date date)
	{
		TeacherAbsenceDates onList = (TeacherAbsenceDates) absenceList.first(); 
		while((onList != null) && (onList.getPerson().getId() < person.getId()))
		{
			onList = (TeacherAbsenceDates) absenceList.after();
		}
		
		if (onList.getPerson().getId() == person.getId())
		{
			onList.addAbsence(date);
		}
		else 
		{
			TeacherAbsenceDates had = new TeacherAbsenceDates(person);
			had.addAbsence(date);
			if(onList == null)
			{
				absenceList.insertTail(had);
			}
			else
			{
				absenceList.insertBefore(had);
			}
		}
	}

	public void CleanUpTeacherMC()
	{
		Date today = new Date();
		TeacherAbsenceDates item = (TeacherAbsenceDates) absenceList.first(); 
		while(item != null)
		{
			if(item.medCert != 'X')
			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				if(formatter.format(item.getAbsenceDates().last()).compareTo(formatter.format(today)) == 0)
				{
					if(item.getAbsenceDates().size() > 1)
					{
						item.medCert = 'X';
					}
				}
				else
				{
					absenceList.removeCurrent();
				}
			}
			item = (TeacherAbsenceDates) absenceList.after();
		}
	}
	
	public void CleanUpTeacherMC(Person person)
	{
		TeacherAbsenceDates onList = (TeacherAbsenceDates) absenceList.first(); 
		while((onList != null) && (onList.getPerson().getId() == person.getId()))
		{
			onList = (TeacherAbsenceDates) absenceList.after();
		}
		if (onList != null)
		{
			absenceList.removeCurrent();
		}
	}
}