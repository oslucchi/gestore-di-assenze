
import java.io.*;

public class FileSaveLoad
{
	private String fileName;
	
	public FileSaveLoad(String fileName)
	{
		this.fileName = fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public void Save(Object obj)
	{
		try
		{
			// Catch errors in I/O if necessary.
			// Open a file to write to, named SavedObj.sav.
			FileOutputStream saveFile = new FileOutputStream(fileName);

			// Create an ObjectOutputStream to put objects into save file.
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			
			// Write the object serialized on file
			save.writeObject(obj);
			
			// Close the file.
			save.close();
		}
		catch(Exception e)
		{
			e.printStackTrace(); // If there was an error, print the info.
		}
	}
	
	public Object Load()
	{
		Object retVal = null;
		try
		{
			// Open file to read from
			FileInputStream loadFile = new FileInputStream(fileName);

			// Create an ObjectInputStream to get objects from save file.
			ObjectInputStream load = new ObjectInputStream(loadFile);

			// Save the object serialized
			retVal = load.readObject();

			// Close the file.
			load.close();
		}
		catch(EOFException e1)
		{
			return(null);
		}
		catch(Exception e2)
		{
			e2.printStackTrace(); // If there was an error, print the info.
		}
		return(retVal);
	}
}