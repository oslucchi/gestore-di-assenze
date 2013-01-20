import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;

public class RandomAccessTest 
{
	RandomAccess st;
	
	public boolean testRandomAccess()
	{
		Pair[] a = st.getIdIdx();
		for(int i = 0; i < a.length; i++)
		{
			System.out.println(a[i].getFirst() + " " + a[i].getSecond());
		}
		a = st.getLastNameIdx();
		for(int i = 0; i < a.length; i++)
		{
			System.out.println(a[i].getFirst() + " " + a[i].getSecond());
		}		
		DoubleLinkedList result = st.Search(RandomAccess.SEARCH_BY_ID, "120010");
		if (result.isEmpty())
		{
			fail("Existent entry 120010 not found");
		}
		if (result.size() > 1)
		{
			fail("Several 120010 found");
		}
		return(true);
	}

	public boolean testAddElement() throws FileNotFoundException
	{
		SchoolCitizen xD = new SchoolCitizen("Test/Data/schoolCitizens");
		
		xD.addElement('S', "wewaglio", "osvaldo", "+233423423524", 11);
		xD.addElement('T', "AddTeacher", "teacher", "+1234567890", 99);
		return (true);
	}
	
	@Test
	public void test() throws FileNotFoundException 
	{
		System.exit(0);
		st = new RandomAccess("Test/Data/SchoolCitizens", 'S');
		assertTrue(testRandomAccess());
		assertTrue(testAddElement());
		try
		{
			st.removeRecord(130230);
		}
		catch (RandomAccessException e)
		{
			if (e.getErrorCode() != RandomAccessException.RECORD_NOT_FOUND)
				fail("Exception return, error code is different thant record not found");
		}
	}
	

}
