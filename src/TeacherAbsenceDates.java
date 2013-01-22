/*
 * Program Name : 
 * Author:
 * Date
 * School
 * Computer Used
 * IDE USed
 * Purpose
 */
import java.io.Serializable;

public class TeacherAbsenceDates extends PersonAbsenceDates implements Serializable
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
