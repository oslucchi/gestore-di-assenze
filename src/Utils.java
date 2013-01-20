import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils 
{
	final static char ESC = 27;

	public static void getReturn()
	{
		System.out.print("Hit return to continue");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
			br.readLine();
		} 
        catch (IOException e) {
			;
		}
	}

	public static void printError(String error)
	{
		System.out.println(error);
		getReturn();
	}

	public static void clearScreen()
	{
		System.out.print(ESC + "[2J");
		System.out.print(ESC + "[<0>;<0>H");
		System.out.println();
	}

	public static String toProperCase(String s) 
	{
		if (s == null)
			return(null);
		String[] strings = s.split(" ");
		String sCopy = "";
		String space = "";
		for(int i = 0; i < strings.length; i++)
		{
			sCopy += space + strings[i].substring(0, 1).toUpperCase() + 
					 strings[i].substring(1).toLowerCase();
			space = " ";
		}
		return sCopy;
	}
	
	public static boolean getYesNo(String prompt)
	{
		char yn = Character.toUpperCase(IBIO.inputChar(prompt));
		while("YN".indexOf(yn) == -1)
			yn = Character.toUpperCase(IBIO.inputChar("Invalid entry. " + prompt));
		if (yn == 'Y')
			return(true);
		else
			return(false);
	}
	
	public static String dateFormat(String format, Date date)
	{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return(sf.format(date));
	}
	
	public static String getStringInput(String prompt, String defVal, String regExp, String errMsg)
	{
		String input = null;
		String locPrompt = prompt;
		if (defVal.compareTo("") != 0)
			locPrompt = locPrompt + "[" + defVal + "]";
		locPrompt = locPrompt + ": ";
		while(true)
		{
			input = IBIO.input(locPrompt);
			while(!input.matches(regExp) && (input.compareTo("") != 0))
				input = IBIO.input(errMsg + locPrompt);
			if (input.compareTo("") == 0)
			{
				if (defVal.compareTo("") == 0)
				{
					if (!Utils.getYesNo("Abort data entry [y/n]: "))
						continue;
				}
				else
					return(defVal);
			}
			else
				return(input);
		}
	}
}
