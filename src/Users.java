/*
 * Program Name : 
 * Author:
 * Date
 * School
 * Computer Used
 * IDE USed
 * Purpose
 */
public class Users 
{
	private Person currentUser = null;
	private SchoolCitizen sc;
	private class User
	{
		int id;
		String password;
		public User(int id, String password)
		{
			this.id = id;
			this.password = password;
		}
	}
	
	DoubleLinkedList users = null;
	public Users(SchoolCitizen sc)
	{
		this.sc = sc;
		Teacher teacher;
		users = new DoubleLinkedList();
		sc.teachers.SetIndexToSearch(RandomAccess.SEARCH_BY_ID);
		for(int i = 0; i < sc.teachers.getIdIdx().length; i++)
		{
			teacher = (Teacher) sc.teachers.getPersonFromFile(i);
			users.insertTail(new User(teacher.getId(), teacher.getPassword().trim()));
		}
		OfficeAdmin admin;
		sc.admins.SetIndexToSearch(RandomAccess.SEARCH_BY_ID);
		for(int i = 0; i < sc.admins.getIdIdx().length; i++)
		{
			admin = (OfficeAdmin) sc.admins.getPersonFromFile(i);
			users.insertTail(new User(admin.getId(), admin.getPassword().trim()));
		}
	}
	
	public boolean authenticate(String password, int id)
	{
		for(User u = (User) users.first(); u != null;)
		{
			if ((u.id == id) && (u.password.compareTo(password.trim()) == 0))
			{
				DoubleLinkedList result = sc.teachers.Search(RandomAccess.SEARCH_BY_ID, Integer.toString(u.id));
				if (result.first() == null)
				{
					currentUser = (Person) sc.admins.Search(RandomAccess.SEARCH_BY_ID, Integer.toString(u.id)).first();
				}
				else
				{
					currentUser = (Person) result.first();
				}
				return true;
			}
			u = (User) users.after();
		}
		return false;
	}
	
	public Person getCurrentUser()
	{
		return currentUser;
	}
}
