import static org.junit.Assert.*;

import org.junit.*;

public class RAtest 
{
	RandomAcess st;
	
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
		DoubleLinkedList result = st.Search(RandomAcess.SEARCH_BY_ID, "120010");
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

	public boolean testAddElement()
	{
		SchoolCitizen xD = new SchoolCitizen();
		
		xD.addElement("wewaglio", "osvaldo", xD.students.generateId(),  "+233423423524", 'S', 11);
		xD.addElement("AddTeacher", "teacher", xD.teachers.generateId(),  "+1234567890", 'T', 99);
		return (true);
	}
	
	@Test
	public void test() 
	{
		st = new RandomAcess("Data/SchoolCitizens", 'S');
		assertTrue(testRandomAccess());
		assertTrue(testAddElement());
		st.removeRecord(130230);
	}
	

}
