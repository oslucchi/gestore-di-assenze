import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Menu 
{
	private String[] options;
	private String name;
	public String[] getOptions() {
		return options;
	}
	public void setOptions(String[] options) {
		this.options = options;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private void printMenu()
	{
		//to clean the screen before outputting the new menu
		char esc = 27;
		System.out.print(esc + "[2J");
		System.out.print(esc + "[<0>;<0>H");
		System.out.println();
		String spaces = "";
		
		int titleInTheMiddle = (80 - name.length())/2;
		spaces =  new String(new char[titleInTheMiddle]).replace('\0', ' ');
		System.out.println(spaces + name + "\n");
		System.out.println();
		int optLength = 0;
		for (int i = 0; i< options.length; i++)
		{
			if (optLength < options[i].length())
				optLength = options[i].length();
		}
		optLength = optLength + 3;
		int middleOfScreen = (80 - optLength) / 2;
		spaces =  new String(new char[middleOfScreen]).replace('\0', ' ');
		
		for(int i = 0; i< options.length; i++)
			System.out.println(spaces + (i + 1) +") " + options[i]);
		
		System.out.println();
		System.out.println(spaces + "X) exit\n");
	}
	public int getChoice()
	{
		int choice = -1;
		while(choice == -1)
		{
			printMenu();
			System.out.print("choose one of the above: ");
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        String data = null;
			try 
			{
				data = br.readLine();
			}
			catch (IOException e) 
			{
				// TODO gestire errore
				e.printStackTrace();
			}
			if (data.toUpperCase().compareTo("X") == 0)
			{
				choice = 0;
				break;
			}
			
			int i = -1;
			try
			{
				i = Integer.parseInt(data);
			}
			catch(NumberFormatException e)
			{
				i = -1;
			}
			if ((i < 1) || (i > options.length))
			{
				System.out.println("Wrong choice (" + data + ") please try again. Any key to continue");
				try 
				{
					br.read();
				}
				catch (IOException e) 
				{
					// TODO gestire errore
					e.printStackTrace();
				}
			}
			else
			{
				choice = i;
			}
		}
		
		return (choice);
	}
}
