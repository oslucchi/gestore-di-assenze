import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeachersAbsences implements Serializable
{
	private static final long serialVersionUID = 1L;
	
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
			if (Utils.dateFormat("yyyy/MM/dd", (Date) onList.getAbsenceDates().last()).compareTo(
					Utils.dateFormat("yyyy/MM/dd", new Date())) != 0)
				onList.addAbsence(date);
		}
	}

	public void CleanUpTeacherMC()
	{
		Date today = new Date();
		TeacherAbsenceDates item = (TeacherAbsenceDates) absenceList.first(); 
		while(item != null)
		{
			if(item.getMedCert() != 'X')
			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				if(formatter.format(item.getAbsenceDates().last()).compareTo(formatter.format(today)) == 0)
				{
					if(item.getAbsenceDates().size() > 1)
					{
						item.setMedCert('X');
					}
					item = (TeacherAbsenceDates) absenceList.after();
				}
				else
					item = (TeacherAbsenceDates) absenceList.removeCurrent();
			}
			else
				item = (TeacherAbsenceDates) absenceList.after();
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