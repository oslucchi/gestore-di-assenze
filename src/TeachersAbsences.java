import java.text.SimpleDateFormat;
import java.util.Date;

public class TeachersAbsences 
{
	public class TeacherAbsenceDates extends PersonAbsenceDates
	{
		private static final long serialVersionUID = 1L;
		private char medCert;		
		public TeacherAbsenceDates(Person person)
		{
			super(person);
			medCert = ' ';
		}
		public char getMedCert() {
			return medCert;
		}
		public void setMedCert(char medCert) {
			this.medCert = medCert;
		}
	}
	DoubleLinkedList absenceList;
	public TeachersAbsences()
	{
		absenceList = new DoubleLinkedList();
	}
	
	public void addAbsence(Person person, Date date)
	{
		TeacherAbsenceDates onList = (TeacherAbsenceDates) absenceList.first(); 
		while((onList != null) && (onList.getPerson().getId() < person.getId()))
		{
			onList = (TeacherAbsenceDates) absenceList.after();
		}
		
		if ((onList == null) || (onList.getPerson().getId() != person.getId()))
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
		else 
		{
			onList.addAbsence(date);
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
						item = (TeacherAbsenceDates) absenceList.after();
					}
				}
				else
				{
					item = (TeacherAbsenceDates) absenceList.removeCurrent();
				}
			}
		}
	}
	
	public void CleanUpTeacherMC(Person person)
	{
		TeacherAbsenceDates onList = (TeacherAbsenceDates) absenceList.first(); 
		while((onList != null) && (onList.getPerson().getId() != person.getId()))
		{
			onList = (TeacherAbsenceDates) absenceList.after();
		}
		if (onList != null)
		{
			absenceList.removeCurrent();
		}
	}

	public DoubleLinkedList getAbsenceList() 
	{
		return absenceList;
	}
	
}