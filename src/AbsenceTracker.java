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
	static SimpleDateFormat formatter = null;
	static FileSaveLoad fileMngr = null;
	static Configuration c = null;

	
	final static int EXIT_ON_ERROR_FILE_NOT_FOUND = 1;
	
	private static void dsaMenuManager()
	{
		Menu menu = new Menu();
		menu.setOptions(
			new String[]
			{ 
				"Add absence",
				"Justify absence",
				"Print today's absence list",
				"Update semester absence",
				"Print semester absences"
			}
		);
		menu.setName("Student's absence management");

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
				DoubleLinkedList dl = sc.search('S', RandomAccess.SEARCH_BY_NAME, lastName.toUpperCase());
				Student student = null;				
				
				// Check if any of the returned student is already marked as absent on the SDAL
				// Do only if we have found a student in our search and the SDAL is not empty
				student = (Student) dl.first();
				if ((student != null) && !sdal.getAbsenceList().isEmpty())
				{
					while(student != null)
					{
						int i;
						Absence testIfThere = (Absence) sdal.getAbsenceList().first();
						for(i = 0; i < sdal.getAbsenceList().size(); i++)
						{
							if (student.getId() == testIfThere.getStudent().getId())
							{
								break;
							}
							testIfThere = (Absence) sdal.getAbsenceList().after();
						}
						if (i < sdal.getAbsenceList().size())
						{
							// The student found is already on the SDAL
							// remove it from the list of student found in order to avoid to present it
							student = (Student) dl.removeCurrent();
						}
						else
						{
							// The student has not been marked absent yet
							// It is a valid entry in the student found list to be presented
							student = (Student) dl.after();
						}
					}
				}
				
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
				}
				sdal.addAbsence(new Absence(student));
				fileMngr.setFileName(c.getStudentDailyAbsencePath());
				fileMngr.Save(sdal);
				Utils.printError("Absence added");
				break;
			
			case 2:
				dl = sdal.getAbsenceList(); 
				Absence absence = (Absence) dl.first();
				int counter = 1;
				boolean innerLoop = true;
				int pageSize = 4;
				while(innerLoop)
				{
					Utils.clearScreen();
					System.out.println("** Justify absence **\n");
					int startFrom = counter;
					for(int i = 1; (i % pageSize != 0) && (absence != null); i++)
					{
						student = absence.getStudent();
						System.out.println(counter + ") " +
										   String.format("%06d %-20s%-30s%-15s%c",
												   		 student.getId(), 
												   		 student.getLastName(), 
												   		 student.getFirstName(), 
												   		 student.getContactNumber(), 
												   		 absence.getJustified()));
						absence = (Absence) dl.after();
						counter++;
					}
					while(true)
					{
						String prompt = "\nChoose a number to act on student or e'X'it";
						if (absence != null)
							prompt += " 'N'ext";
						if (startFrom > 1)
							prompt += " 'P'rev";
						prompt += ": ";
						String whatNow = input(prompt).toUpperCase();
						if (whatNow.compareTo("X") == 0)
						{
							innerLoop = false;
							break;
						}
						
						if ((absence != null) && whatNow.compareTo("N") == 0)
						{
							break;
						}
						
						if ((startFrom > 1) && (whatNow.compareTo("P") == 0))
						{
							int linesOnPage = counter - startFrom;
							for(int i = pageSize + linesOnPage; (i > 1); i--)
							{
								counter--;
								if (absence == null)
									absence = (Absence) dl.last();
								else
									absence = (Absence) dl.before();
							}
							if (absence == null)
							{
								absence = (Absence) dl.first();
								counter = 1;
							}
							break;
						}
						
						if (whatNow.matches("[0-9]+"))
						{
							int studentSelected = Integer.parseInt(whatNow);
							if ((studentSelected >= startFrom) && (studentSelected < counter))
							{
								char justify = inputChar("Enter justification type ('Y'es, 'N'ot, 'L'ate): ");
								while("YNL".indexOf(Character.toUpperCase(justify)) == -1)
									justify = inputChar("Wrong choice. Enter justification type ('Y'es, 'N'ot, 'L'ate): ");
								justify = Character.toUpperCase(justify);
								DoubleLinkedList.DoubleLinkedListElement current = dl.getPointerToCurrent();
								for(int i = counter; i > studentSelected; i--)
								{
									absence = (Absence) dl.before();
								}
								sdal.setJustified(absence.getStudent(), justify);
								dl.setPointerToCurrent(current);
								for(int i = counter; i > startFrom; i--)
								{
									counter--;
									absence = (Absence) dl.before();
								}
								break;
							}
						}
					}
					
					System.out.println("");
				}
				break;
					
			default:
				loop = false;
				break;
			}
		}
	}
	
	private static void stlMenuManager()
	{
		Menu menu = new Menu();
		menu.setOptions(
			new String[]
			{ 
				"Add",
				"Remove",
				"Search and Update"
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
				String firstName = input("First name: ");
				while(!firstName.matches("[a-zA-z ]+"))
					firstName = input("Only alphabetical allowed. First name: ");
				String lastName = input("Last name: ");
				while(!lastName.matches("[a-zA-z ]+"))
					lastName = input("Only alphabetical allowed. Last name: ");
				String contactNumber = input("Contact number: ");
				while(!contactNumber.matches("\\+[1-9][0-9]+"))
					contactNumber = input("International number prefixed by + country code. Contact number: ");
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
				break;
			
			case 3:
				Utils.clearScreen();
				System.out.println("** Search and Update school citizens **\n");
				id = inputInt("ID you are looking for: ");
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
					firstName = input("First name [" + person.getFirstName() + "]: ");
					while(!firstName.matches("[a-zA-z ]+") && (firstName.compareTo("") != 0))
						firstName = input("Only alphabetical allowed. First name [" + 
											person.getFirstName() + "]: ");
					if (firstName.compareTo("") == 0)
						firstName = person.getFirstName();

					lastName = input("Last name [" + person.getLastName() + "]: ");
					while(!lastName.matches("[a-zA-z ]+") && (lastName.compareTo("") != 0))
						lastName = input("Only alphabetical allowed. Last name [" + 
											person.getFirstName() + "]: ");
					if (lastName.compareTo("") == 0)
						lastName = person.getLastName();

					contactNumber = input("Contact number [" + person.getContactNumber() + "]: ");
					while(!contactNumber.matches("\\+[1-9][0-9]+") && (contactNumber.compareTo("") != 0))
						contactNumber = input("International number prefixed by + country code. Contact number [" + 
											person.getContactNumber() + "]: ");
					if (contactNumber.compareTo("") == 0)
						contactNumber = person.getContactNumber();
					
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
			sdal = new StudentDailyAbsenceList(c.getStudentDailyAbsencePath());
			// Save the empty list on file for future restarts
			fileMngr.Save(sdal);
		}

		mainMenu = new Menu();
		mainMenu.setOptions(
			new String[] 
			{ 
				"School citizens maintenance",
				"Student's absence management",
				"Daily teacher's absence entry",
				"Unjustified teacher absences list",
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
					stlMenuManager();
				else
					Utils.printError("You are not allowed to use this choice");
				break;
			
			case 2:
				dsaMenuManager();
				break;
			
			case 3:
				break;
			case 4:
				break;

			case 5:
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
				loop = false;
				break;
			}
		}
		
	}
}
