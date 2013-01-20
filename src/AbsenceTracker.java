import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbsenceTracker extends IBIO
{
	static Menu mainMenu;
	static Users users = null;
	static SchoolCitizen sc = null;
	static StudentDailyAbsenceList sdal = null;
	static StudentsSemesterAbsenceList ssal = null;
	static TeachersAbsences ta = null;
	static SimpleDateFormat formatter = null;
	static FileSaveLoad fileMngr = null;
	static Configuration c = null;
	final static int EXIT_ON_ERROR_FILE_NOT_FOUND = 1;

	public static Object showPages(DoubleLinkedList dl, boolean useNumbers)
	{
		char objectType = ' ';
		Person person = null;
		PersonAbsenceDates pad = null;
		Absence abs = null;
		TeacherAbsenceDates tad = null;
		
		dl.initPageManager(4);
		DoubleLinkedList page = null;
		page = dl.getNextPage();
		while (true)
		{
			char justified = ' ';
			Utils.clearScreen();
			Object dataObject = page.first();
			for (int i = 0; i< page.size(); i++)
			{
				if (dataObject instanceof TeacherAbsenceDates)
				{
					tad = (TeacherAbsenceDates) dataObject;
					person = tad.getPerson();
					objectType = 'D';
				}
				else if (dataObject instanceof Absence)
				{
					abs = (Absence) dataObject;
					person = abs.getStudent();
					objectType = 'B';
				}
				else if (dataObject instanceof Student)
				{
					person = (Student) dataObject;
					objectType = 'S';
				}
				else if (dataObject instanceof Teacher)
				{
					person = (Teacher) dataObject;
					objectType = 'T';
				}
				else if (dataObject instanceof OfficeAdmin)
				{
					person = (OfficeAdmin) dataObject;
					objectType = 'A';
				}
				else if (dataObject instanceof PersonAbsenceDates)
				{
					pad = (PersonAbsenceDates) dataObject;
					person = pad.getPerson();
					objectType = 'P';
				} 

				switch(objectType)
				{
				case 'B':
					justified = abs.getJustified();
					break;
				case 'D':
					justified = tad.getMedCert();
					break;
				case 'P':
				case 'S':
				case 'T':
				case 'A':
					justified = ' ';
					break;
				}
				System.out.println(
						String.format("%02d)  %06d %-20s%-30s%-15s %c",
								i + 1,
								person.getId(), 
								person.getLastName(), 
								person.getFirstName(), 
								person.getContactNumber(),
								justified));
				dataObject = page.after();
			}
			
			String prompt = "\nChoose e'X'it";
			
			if(useNumbers)
				prompt += " | number to act on student";
			if (dl.hasNextPage())
				prompt += " | 'N'ext";
			if (dl.hasPrevPage())
				prompt += " | 'P'rev";
			prompt += ": ";
			String whatNow = input(prompt).toUpperCase();

			// Exit if the option selected is X
			if (whatNow.compareTo("X") == 0)
			{
				return (null);
			}

			// Check if we are not on the last page (the current absence is not null)
			// and implement the page forward function
			if (dl.hasNextPage() && whatNow.compareTo("N") == 0)
			{
				page = dl.getNextPage();
			}

			// Check if we are not on the first page (startFrom should be greater than 1)
			// and implement the page backward function
			if (dl.hasPrevPage() && (whatNow.compareTo("P") == 0))
			{
				page = dl.getPrevPage();
			}

			// If the choice is a number check if it is related to one of the currently
			// presented options. In that case proceed asking which justification to apply
			// and apply it.
			if(useNumbers)
			{
				if (whatNow.matches("[0-9]+"))
				{
					return(page.get(Integer.parseInt(whatNow) - 1));
				}
			}
		}
	}
	
	public static void tamMenuManager()
	{

		Menu menu = new Menu();
		menu.setOptions(
				new String[]
						{ 
						"Add absence",
						"Provide Teacher MC",
						"Clean up list",
						"Print teacher absence list"
						}
				);
		menu.setName("Teacher's absence management");

		boolean loop = true;
		while(loop)
		{
			switch(menu.getChoice())
			{
			case 1:
				Utils.clearScreen();
				System.out.println("** Add absence **\n");
				String lastName = input("Last name: ");
				while(!lastName.matches("[a-zA-z ]+") && (lastName.compareTo("") != 0))
					lastName = input("Only alphabetical allowed. Last name: ");
				if (lastName.compareTo("") == 0)
				{
					break;
				}
				DoubleLinkedList dl = sc.search('T', RandomAccess.SEARCH_BY_NAME, lastName.toUpperCase());
				if (dl.isEmpty())
				{
					Utils.printError("No teacher found");
					break;
				}
				else
				{
					Teacher teacher = (Teacher) showPages(dl, true);
					if (teacher == null)
						break;
					ta.addAbsence(teacher, new Date());
					fileMngr.setFileName(c.getTeacherAbsencePath());
					fileMngr.Save(ta);
					Utils.printError("Absence added");
				}
				break;
			
			case 2:
				Utils.clearScreen();
				System.out.println("** Provide Teacher MC **\n");
				dl = ta.getAbsenceList(); 
				if (dl.isEmpty())
				{
					Utils.printError("no absences recorded today");
					break;
				}
				TeacherAbsenceDates tad = (TeacherAbsenceDates) showPages(dl, true);
				if (tad == null)
					break;
				if(Utils.getYesNo("Comfirm MC for: " + tad.getPerson().getLastName() + " " +
						tad.getPerson().getFirstName() + "? "))
				{
					ta.CleanUpTeacherMC(tad.getPerson());
					Utils.printError("Teacher Justified");
				}
				break;
			case 3:
				Utils.clearScreen();
				System.out.println("**Clean up list**\n");
				if(Utils.getYesNo("Comfirm cleaning up the MC list? :"))
				{
					ta.CleanUpTeacherMC();
					Utils.printError("MC list cleaned");
				}
				break;
				
			case 4:
				Utils.clearScreen();
				System.out.println("** Print teacher absence list **\n");
				dl = ta.getAbsenceList();
				if (dl.isEmpty())
				{
					Utils.printError("no absences in the teacher absence list");
					break;
				}
				tad = (TeacherAbsenceDates) showPages(dl, true);
				if (tad == null)
					break;
				Utils.clearScreen();
				System.out.println(
						String.format("Absences for teacher: %06d %s %s  %s\n",
								tad.getPerson().getId(), 
								tad.getPerson().getLastName(), 
								tad.getPerson().getFirstName(), 
								tad.getPerson().getContactNumber()));
				DoubleLinkedList dateList = tad.getAbsenceDates();
				Date absenceDate = (Date) dateList.first();
				for(int i = 0; i < dateList.size(); i++)
				{
					System.out.println((i+1) + ") " + 
							Utils.dateFormat(c.getDateFormatter(), absenceDate));
					absenceDate = (Date) dateList.after();
				}
				Utils.getReturn();
				break;
				
			default:
				loop = false;
				break;
			}
		}
	}
	
	private static void dsaMenuManager()
	{
		Menu menu = new Menu();
		menu.setOptions(
				new String[]
						{ 
						"Add absence",
						"Justify or print todays absence",
						"Update semester absence",
						"Print semester absences"
						}
				);
		menu.setName("Student's absence management");

		boolean loop = true;
		while(loop)
		{
			Absence absence;
			switch(menu.getChoice())
			{
			case 1:
				Student student;
				Utils.clearScreen();
				System.out.println("** Add absence **\n");
				String lastName = input("Last name: ");
				while(!lastName.matches("[a-zA-z ]+") && (lastName.compareTo("") != 0))
					lastName = input("Only alphabetical allowed. Last name: ");
				if (lastName.compareTo("") == 0)
				{
					break;
				}
				DoubleLinkedList dl = sc.search('S', RandomAccess.SEARCH_BY_NAME, lastName.toUpperCase());
				if (dl.isEmpty())
				{
					Utils.printError("No student found");
					break;
				}
				else
				{
					student = (Student) showPages(dl, true);
					sdal.addAbsence(new Absence(student));
					fileMngr.setFileName(c.getStudentDailyAbsencePath());
					fileMngr.Save(sdal);
					Utils.printError("Absence added");
				}
				break;

			case 2:
				dl = sdal.getAbsenceList(); 
				if (dl.isEmpty())
				{
					Utils.printError("no absences recorded today");
					break;
				}
				absence = (Absence) showPages(dl, true);
				if (absence == null)
					break;
				char justify = inputChar("Enter justification type ('Y'es, 'N'ot, 'L'ate): ");
				while("YNL".indexOf(Character.toUpperCase(justify)) == -1)
					justify = inputChar("Wrong choice. Enter justification type ('Y'es, 'N'ot, 'L'ate): ");
				justify = Character.toUpperCase(justify);

				// mark the justified char in the absence for the referred student
				sdal.setJustified(absence.getStudent(), justify);
				break;

			case 3:
				Utils.clearScreen();
				System.out.println("**Updated semester absence**");
				if (!Utils.getYesNo("Confirm to add today's absences to the semester list (Y/N)? "))
					break;

				dl = sdal.getAbsenceList();
				absence = (Absence) dl.first();
				while(absence != null)
				{
					ssal.addAbsence(absence.getStudent(), sdal.getDate());
					absence = (Absence) dl.after();
				}
				fileMngr.setFileName(c.getStudentSemesterAbsencePath());
				fileMngr.Save(ssal);
				sdal = new StudentDailyAbsenceList();
				fileMngr.setFileName(c.getStudentDailyAbsencePath());
				fileMngr.Save(sdal);
				break;

			case 4:
				Utils.clearScreen();
				System.out.println("**Print semester absence**");
				dl = ssal.getSemesterAbsence();
				if (dl.isEmpty())
				{
					Utils.printError("no absences recorded for this semester");
					break;
				}
				PersonAbsenceDates pad = (PersonAbsenceDates) showPages(dl, true);
				if (pad == null)
					break;
				Utils.clearScreen();
				System.out.println(
						String.format("Absences for student: %06d %s %s  %s\n",
								pad.getPerson().getId(), 
								pad.getPerson().getLastName(), 
								pad.getPerson().getFirstName(), 
								pad.getPerson().getContactNumber()));
				DoubleLinkedList dateList = pad.getAbsenceDates();
				Date absenceDate = (Date) dateList.first();
				for(int i = 0; i < dateList.size(); i++)
				{
					System.out.println((i+1) + ") " + 
							Utils.dateFormat(c.getDateFormatter(), absenceDate));
					absenceDate = (Date) dateList.after();
				}
				Utils.getReturn();
				break;

			default:
				loop = false;
				break;
			}
		}
	}

	private static void stlMenuManager() throws RandomAccessException
	{
		Menu menu = new Menu();
		menu.setOptions(
				new String[]
						{ 
						"Add",
						"Remove",
						"Search and Update",
						"Print school citizen list"
						}
				);
		menu.setName("School citizens maintenance");

		boolean loop = true;
		while(loop)
		{
			switch(menu.getChoice())
			{
			case 1:
				Utils.clearScreen();
				System.out.println("** Add new school citizen **\n");
				String firstName = Utils.getStringInput("First name: ", "", "[a-zA-Z]{0,30}",
									"Only alphabetical allowed with a max of 30 characters.\n");
				if (firstName.compareTo("") == 0)
				{
					break;
				}
				String lastName = Utils.getStringInput("Last name: ", "", "[a-zA-Z]{0,20}",
						"Only alphabetical allowed with a max of 20 characters.\n");
				if (lastName.compareTo("") == 0)
				{
					break;
				}
				
				String contactNumber = Utils.getStringInput("Contact number: ", "", "\\+[1-9][0-9]{1,13}",
						"International number prefixed by + country code. Max 15 characters.\n");
				if (contactNumber.compareTo("") == 0)
				{
					break;
				}
			
				char role = inputChar("Role (T or S or A): ");
				while("STA".indexOf(Character.toUpperCase(role)) == -1)
					role = inputChar("Role (T or S or A): ");
				role = Character.toUpperCase(role);
				int grade = 0;
				if (role == 'S')
				{
					grade = inputInt("Grade (6-8): ");
					while((grade < 6) || (grade > 8))
						grade = inputInt("Only numbers between 6 and 8. Grade: ");
				}
				sc.addElement(role, lastName, firstName, contactNumber, grade);
				break;

			case 2:
				Utils.clearScreen();
				System.out.println("** Remove school citizen **\n");
				char lookingFor = inputChar("Search by i'D' or last'N'ame (enter D or N): ");
				while("ND".indexOf(Character.toUpperCase(lookingFor)) == -1)
					lookingFor = inputChar(" Error wrong character.(enter D for id or N for last name): ");
				lookingFor = Character.toUpperCase(lookingFor);
				if (lookingFor == 'N')
				{
					lastName = input("Last name: ");
					while(!lastName.matches("[a-zA-z ]+") && (lastName.compareTo("") != 0))
						lastName = input("Only alphabetical allowed. Last name: ");
					if (lastName.compareTo("") == 0)
					{
						break;
					}
					DoubleLinkedList dl = sc.search('S', RandomAccess.SEARCH_BY_NAME, lastName.toUpperCase());
					Student student = null;
					if (dl.isEmpty())
					{
						Utils.printError("No students found");
						break;
					}
					else
					{
						int counter = 1;
						student = (Student) dl.first();
						while(student != null)
						{
							System.out.println(counter + ") " + student.getId() + " " + student.getLastName() +
									" " + student.getFirstName());
							counter++;
							student = (Student) dl.after();
						}
						int choice = inputInt("Enter student number (Return to exit): ");
						while ((choice < 0) || (choice > dl.size()))
							choice = inputInt("invalid choice try again. Enter student number (Return to exit): ");
						if (choice == 0)
							break;
						student = (Student) dl.first();
						for(int i = 1; i< choice; i++)
							student = (Student) dl.after();
						System.out.println("Please confirm you want to remove:");
						System.out.println(student.getId() + " " + student.getLastName() + 
								" " + student.getFirstName() + "\n");
						if (!Utils.getYesNo("Confirm deletion (Y/N)?: "))
							break;
						sc.removeElement(student.getId(), 'S');
					}
				}
				else
				{
					int id = inputInt("ID you are looking for: ");
					role = inputChar("Role (T or S or A): ");
					while("STA".indexOf(Character.toUpperCase(role)) == -1)
						role = inputChar("Role (T or S or A): ");
					role = Character.toUpperCase(role);
					try
					{
						DoubleLinkedList dl = sc.search(role, RandomAccess.SEARCH_BY_ID, String.valueOf(id));
						Person person = (Person) dl.first();
						if (person == null)
						{
							Utils.printError("The ID entered doesn't exist");
							break;
						}
						else
						{
							System.out.println("Please confirm you want to remove:");
							System.out.println(person.getId() + " " + person.getLastName() + 
									" " + person.getFirstName() + "\n");
							if (!Utils.getYesNo("Confirm deletion (Y/N)?: "))
								break;
							sc.removeElement(id, role);
						}
					}
					catch (RandomAccessException e) 
					{
						Utils.printError("Error removing element: " + e.getMessage());
						break;
					}
					Utils.printError("the ID " + String.valueOf(id) + " has been removed");
				}
				break;

			case 3:
				Utils.clearScreen();
				System.out.println("** Search and Update school citizens **\n");
				int id = inputInt("ID you are looking for: ");
				role = inputChar("Role (T or S or A): ");
				while("STA".indexOf(Character.toUpperCase(role)) == -1)
					role = inputChar("Role (T or S or A): ");
				role = Character.toUpperCase(role);
				DoubleLinkedList dl = sc.search(role, RandomAccess.SEARCH_BY_ID, String.valueOf(id));
				Person person = (Person) dl.first();
				if (person == null)
				{
					Utils.printError("The ID entered doesn't exist");
					break;
				}
				else
				{
					firstName = Utils.getStringInput("First name: ", person.getFirstName(), "[a-zA-Z]{0,30}",
							"Only alphabetical allowed with a max of 30 characters.\n");
					if (firstName.compareTo("") == 0)
					{
						break;
					}
					lastName = Utils.getStringInput("Last name: ", person.getLastName(), "[a-zA-Z]{0,20}",
							"Only alphabetical allowed with a max of 20 characters.\n");
					if (lastName.compareTo("") == 0)
					{
						break;
					}
					
					contactNumber = Utils.getStringInput("Contact number: ", person.getContactNumber(), "\\+[1-9][0-9]{1,13}",
							"International number prefixed by + country code. Max 15 characters.\n");
					if (contactNumber.compareTo("") == 0)
					{
						break;
					}

					grade = 0;
					if (role == 'S')
					{
						grade = inputInt("Grade (6-8) [" + ((Student) person).getGrade() + "]: ");
						while(((grade < 6) || (grade > 8)) && (grade != 0))
							grade = inputInt("Only numbers between 6 and 8. Grade [" + 
									((Student) person).getGrade() + "]: ");
						if (grade == 0)
							grade = ((Student) person).getGrade();
						((Student) person).setGrade(grade);
					}

					if (Utils.getYesNo("Confirm to update with current data (Y/N)? "))
					{
						person.setFirstName(firstName);
						person.setLastName(lastName);
						person.setContactNumber(contactNumber);
						sc.replaceElement(person);
						Utils.printError("the ID " + String.valueOf(id) + " has been updated");
					}
				}
				break;

			case 4:
				Utils.clearScreen();
				System.out.println("** Print school citizen list **\n");
				role = inputChar("Role looking for (T or S or A): ");
				while("STA".indexOf(Character.toUpperCase(role)) == -1)
					role = inputChar("Role (T or S or A): ");
				role = Character.toUpperCase(role);
				dl = sc.getAll(role);
				showPages(dl, false);
				break;
				
			default:
				loop = false;
				break;
			}
		}
	}

	private static String getPassword(String prompt)
	{
		String password = "";
		Console console = null;
		console = System.console();
		if (console == null) 
		{
			// ECLIPSE doesn't have a System.console available.
			// This will read a string from System.in instead to allow testing the application in ECLISPE
			System.out.print(prompt);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			try 
			{
				password = bufferedReader.readLine();
			}
			catch (IOException e) 
			{
				password = "@@@@@@@@@@@";
			}
		}
		else
		{
			char[] passwordArray = console.readPassword(prompt);
			password = new String(passwordArray);
		}
		return password;
	}

	private static boolean authenticate()
	{
		String password = "";
		int id = 0;
		for(int i = 0; i < 3; i++)
		{
			Utils.clearScreen();

			int tempId = IBIO.inputInt("Enter your ID " + (id == 0 ? ": " : "[" + id + "]: "));
			if (tempId != 0)
				id = tempId;

			password = getPassword("Enter your password: ");

			if (users.authenticate(password, id))
				return(true);
			System.out.println("\n\nSomething is wrong with either your id or password, try again...");
			Utils.getReturn();
		}
		return(false);
	}

	public static void main(String[] args) 
	{
		// Get configuration from the INI file
		try {
			c = new Configuration();
		}
		catch (Exception e)
		{
			System.out.println("error opening the configuration file:");
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		// Data format from configuration file
		formatter = new SimpleDateFormat(c.getDateFormatter());

		/*
		 * Initialization
		 * All the main objects needed by the application will be initialized from 
		 * data previously saved on file or as new in case they are not anymore relevant
		 */

		// Initialize the SchoolCitizen object with all teachers, students and admins
		// list and indexes
		sc = null;
		try
		{
			sc = new SchoolCitizen(c.getSchoolCitizenPath());
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("error opening the school citizen file:");
			System.out.println(e.getMessage());
			System.exit(-1);
		}


		// Initialize users list for authentication based on SchoolCitizen object
		users = new Users(sc);
		//Authenticate the user to start working
		if ((args.length > 0) && (args[0].compareTo("auto") == 0))
			users.authenticate("admin", 129900);
		else
			if (!authenticate())
				System.exit(-1);


		// Initialize StudentsDailyAbsenceList by loading the currently saved data.
		// If the absence list is not dated today then drop the object and initialize an empty one.
		fileMngr = new FileSaveLoad(c.getStudentDailyAbsencePath());
		if (((sdal = (StudentDailyAbsenceList) fileMngr.Load()) == null) ||
				(formatter.format(sdal.getDate()).compareTo(formatter.format(new Date())) != 0))
		{
			sdal = new StudentDailyAbsenceList();
			// Save the empty list on file for future restarts
			fileMngr.Save(sdal);
		}
		
		fileMngr.setFileName(c.getTeacherAbsencePath());
		if ((ta = (TeachersAbsences) fileMngr.Load()) == null)
		{
			ta = new TeachersAbsences();
		}
		
		fileMngr.setFileName(c.getStudentSemesterAbsencePath());
		if ((ssal = (StudentsSemesterAbsenceList) fileMngr.Load()) == null)
		{
			ssal = new StudentsSemesterAbsenceList();
		}
	
		mainMenu = new Menu();
		mainMenu.setOptions(
				new String[] 
						{ 
						"School citizens maintenance",
						"Student's absence management",
						"Teacher's absence management",
						"Change password"
						}
				);
		mainMenu.setName("Main Menu");		
		boolean loop = true;
		while(loop)
		{
			switch(mainMenu.getChoice())
			{
			case 1:
				if (users.getCurrentUser().getRole() == 'A')
				{
					try 
					{
						stlMenuManager();
					}
					catch (RandomAccessException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					Utils.printError("You are not allowed to use this choice");
				break;

			case 2:
				dsaMenuManager();
				break;

			case 3:
				tamMenuManager();
				break;
				
			case 4:
				Utils.clearScreen();
				System.out.println("** Change password **\n");
				String password = getPassword("Enter current password: ");
				if (users.authenticate(password, users.getCurrentUser().getId()))
				{
					int id = users.getCurrentUser().getId();
					password = getPassword("Enter new password: ");
					if (getPassword("re-enter your new password for verification: ").compareTo(password) == 0)
					{
						if (users.getCurrentUser().getRole() == 'A')
						{
							OfficeAdmin user = (OfficeAdmin) users.getCurrentUser();
							user.setPassword(password);
							sc.admins.replaceRecord(id, user.toString());	
						}
						else if (users.getCurrentUser().getRole() == 'T')
						{
							Teacher user = (Teacher) users.getCurrentUser();
							user.setPassword(password);
							sc.teachers.replaceRecord(id, user.toString());	
						}
						// Re-create users object to make sure it captures the new entered password
						users = new Users(sc);
						// Re-authenticate based on the new password to correctly set the current user
						// object including the new password
						users.authenticate(password, id);
					}
					else
						Utils.printError("Passwords entered do not match. Try again.");
				}
				else
					Utils.printError("You entered a wrong password");
				break;

			default:
				if (Utils.getYesNo("Save data [y/n]: "))
				{
					fileMngr.setFileName(c.getTeacherAbsencePath());
					fileMngr.Save(ta);
					fileMngr.setFileName(c.getStudentDailyAbsencePath());
					fileMngr.Save(sdal);
					fileMngr.setFileName(c.getStudentSemesterAbsencePath());
					fileMngr.Save(ssal);
				}
				loop = false;
				break;
			}
		}
	}
}
