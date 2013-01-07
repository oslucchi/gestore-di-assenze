import java.io.File;


public class AbesebceTracker 
{
	static Menu mainMenu;
	static Menu stlMenu;

	final static int EXIT_ON_ERROR_FILE_NOT_FOUND = 1;
	
	static private void SubMenuManage(Menu menuMng)
	{
		boolean loop = true;
		while(loop)
		{
			switch(menuMng.getChoice())
			{
			case 1:
				break;
			case 2:
				break;
			default:
				loop = false;
				break;
			}
		}
	}

	public static void main(String[] args) 
	{
		  File f = new File("Data/SchoolCitizens");		  
		  if (!f.exists())
		  {
			  System.out.println("File not found!");
			  System.exit(EXIT_ON_ERROR_FILE_NOT_FOUND);
		  }

		
		mainMenu = new Menu();
		mainMenu.setOptions
		(new String[] 
			{ 
				"Students-Teachers list maintenance",
				"Daily student's absence entry",
				"Print today's student's absences list",
				"Print Quarter absences list",
				"Daily teacher's absence entry",
				"Unjustified teacher absences list"
			}
		);
		mainMenu.setName("Main Menu");

		stlMenu = new Menu();
		stlMenu.setOptions
		(new String[]
			{ 
				"Add",
				"Remove",
				"Search and Update"
			}
		);
		stlMenu.setName("Students-Teachers list maintenance");
		
		boolean loop = true;
		while(loop)
		{
			switch(mainMenu.getChoice())
			{
			case 1:
				SubMenuManage(stlMenu);
				break;
			case 2:
				break;
			default:
				loop = false;
				break;
			}
		}
	}
}
