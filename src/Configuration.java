import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class Configuration
{
	private String schoolCitizenPath;
	private String studentSemesterAbsencePath;
	private String studentDailyAbsencePath;
	private String teacherAbsencePath;
	private String dateFormatter;
	public Configuration() throws InvalidFileFormatException, IOException
	{
		Ini ini = new Ini(new File("Data/AbsenceTracker.ini"));
		schoolCitizenPath = ini.get("Path", "schoolCitizenPath");
		studentSemesterAbsencePath = ini.get("Path", "studentSemesterAbsencePath");
		studentDailyAbsencePath = ini.get("Path", "studentDailyAbsencePath");
		teacherAbsencePath = ini.get("Path", "teacherAbsencePath");
		dateFormatter = ini.get("Misc", "dateFormatter");
	}
	public String getSchoolCitizenPath()
	{
		return schoolCitizenPath;
	}
	public String getStudentSemesterAbsencePath()
	{
		return studentSemesterAbsencePath;
	}
	public String getStudentDailyAbsencePath()
	{
		return studentDailyAbsencePath;
	}
	public String getTeacherAbsencePath()
	{
		return teacherAbsencePath;
	}
	public String getDateFormatter()
	{
		return dateFormatter;
	}
}