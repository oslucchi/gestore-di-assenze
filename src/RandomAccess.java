/*
 * Program Name : 
 * Author:
 * Date
 * School
 * Computer Used
 * IDE USed
 * Purpose
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;

public class RandomAccess
{
	final static long recSize = 85;
	public static int SEARCH_BY_ID = 1;
	public static int SEARCH_BY_NAME = 2;
	
	private RandomAccessFile myFile;
	private String fileName;
	private char includeRole;
	private Pair[] lastNameIdx;
	public Pair[] idIdx;
	private Pair[] searchIndex;
	private ArrayList<Pair> tempIndexName;
	private ArrayList<Pair> tempIndexId;
	private DoubleLinkedList searchResult;

	public RandomAccess(String fileName, char includeRole) throws FileNotFoundException
	{
		this.fileName = fileName;
		this.includeRole = includeRole;
		myFile = new RandomAccessFile(fileName, "rw");
		CreateIndex();
	}

	private Pair[] QuickSort(Pair[] inputArray, int last)
	{
		Pair pivot;
		if (last < 1)
			return(inputArray);
		int counterForLess = 0;
		int counterForMore = 0;
		int pivotPos = last / 2;
		pivot = inputArray[pivotPos] ;
		Pair[] lessThanPivotArray = new Pair[last + 1];
		Pair[] greaterThanPivotArray = new Pair[last + 1];

		for (int i = 0; i <= last; i++)
		{
			if (i != pivotPos)
			{
				if (inputArray[i].getFirst().compareTo(pivot.getFirst()) < 0)
				{
					lessThanPivotArray [counterForLess] = inputArray[i];
					counterForLess = counterForLess + 1;
				}
				else 
				{
					greaterThanPivotArray[counterForMore] = inputArray[i];
					counterForMore = counterForMore + 1;
				}
			}
		}
		lessThanPivotArray = QuickSort(lessThanPivotArray, counterForLess - 1);
		greaterThanPivotArray = QuickSort(greaterThanPivotArray, counterForMore - 1);
		for(int i = 0; i < counterForLess; i++)
		{
			inputArray[i] = lessThanPivotArray[i];
		}
		inputArray[counterForLess] = pivot;
		for(int i = 0; i < counterForMore; i++)
		{
			inputArray[counterForLess + 1 + i] = greaterThanPivotArray[i];
		}
		return(inputArray);
	}
	
	public void CreateIndex()
	{
		// TODO: controllare che l'indice per id abbia valori univochi
		String record = null;
		int recordNumber = 0;
		Pair item;
		tempIndexName = new ArrayList<Pair>();
		tempIndexId = new ArrayList<Pair>();
		try
		{
			myFile.seek(0);
			while(((record = myFile.readLine()) != null) && (record.compareTo("") != 0))
			{
				if (record.charAt(71) == includeRole)
				{
					item = new Pair(record.substring(0, 20).trim(), recordNumber);
					tempIndexName.add(item);
					item = new Pair(record.substring(50, 56).trim(), recordNumber);
					tempIndexId.add(item);
				}
				recordNumber ++;
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		idIdx = new Pair[tempIndexId.size()];
		tempIndexId.toArray(idIdx);
		idIdx = QuickSort(idIdx, idIdx.length - 1);
		lastNameIdx = new Pair[tempIndexName.size()];
		tempIndexName.toArray(lastNameIdx);
		lastNameIdx = QuickSort(lastNameIdx, lastNameIdx.length - 1);
	}
	
	public Object getPersonFromFile(int position)
	{
		String record = null;
		try 
		{
			myFile.seek(searchIndex[position].getSecond() * recSize);
			record = myFile.readLine();
		}
		catch (IOException e) {
			// TODO gestire l'eccezione
			e.printStackTrace();
		}
		
		Object item = null;
		if (includeRole == 'S')
		{
			item = new Student(record);
		}
		if (includeRole == 'T')
		{
			item = new Teacher(record);
		}
		if (includeRole == 'A')
		{
			item = new OfficeAdmin(record);
		}
		return item;
	}
	
	private DoubleLinkedList BinarySearch(String searchKey, int first, int last)
	{
		if (last == first)
		{
			if (searchIndex[last].getFirst().compareTo(searchKey) == 0)
			{
				searchResult.insertTail(getPersonFromFile(last));
			}
			return(searchResult);
		}

		int position = first + (last - first) / 2;
		String idxValue = searchIndex[position].getFirst();
		if (idxValue.compareTo(searchKey) == 0)
		{
			while((position > first) && 
					(searchIndex[position - 1].getFirst().compareTo(searchKey) == 0))
				position--;
			while((position < last) &&
				(searchIndex[position].getFirst().compareTo(searchKey) == 0))
			{
				searchResult.insertTail(getPersonFromFile(position));
				position++;
			}
			return(searchResult);
		}
		else if (idxValue.compareTo(searchKey) < 0)
			return(BinarySearch(searchKey, position + 1, last));
		else
		{
			position--;
			return(BinarySearch(searchKey, first, (position < first ? first : position)));
		}
	}

	public void SetIndexToSearch(int whichIdx)
	{
		if (whichIdx == SEARCH_BY_ID)
			searchIndex = idIdx;
		else
			searchIndex = lastNameIdx;
	}
	
	public DoubleLinkedList Search(int whichIdx, String searchKey)
	{
		searchResult = new DoubleLinkedList();
		SetIndexToSearch(whichIdx);
		return(BinarySearch(searchKey, 0, searchIndex.length - 1));
	}

	private int whichRecord(String searchKey, int first, int last)
	{
		if (last == first)
		{
			if (searchIndex[last].getFirst().compareTo(searchKey) == 0)
				return(searchIndex[last].getSecond());
			else
				return(-1);
		}

		int position = first + (last - first) / 2;
		String idxValue = searchIndex[position].getFirst();
		if (idxValue.compareTo(searchKey) == 0)
		{
			return(searchIndex[position].getSecond());
		}
		else if (idxValue.compareTo(searchKey) < 0)
			return(whichRecord(searchKey, position + 1, last));
		else
			return(whichRecord(searchKey, first, position - 1));
	}

	public void addRecord(String record)
	{
		record = record + "\n";
		try
		{
			myFile.seek(myFile.length());
			myFile.writeBytes(record);
			CreateIndex();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void removeRecord(int id) throws RandomAccessException
	{
		searchResult = new DoubleLinkedList();
		searchIndex = idIdx;
		int recNo = whichRecord(String.valueOf(id), 0, searchIndex.length);
		if (recNo == -1)
			throw new RandomAccessException(
					RandomAccessException.RECORD_NOT_FOUND, String.valueOf(id) + " doesn't exists");
		
		RandomAccessFile myFileW = null;
		try 
		{
			myFileW = new RandomAccessFile(fileName, "rw");
		}
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			String record;
			myFileW.seek(recNo * recSize);
			myFile.seek((recNo + 1) * recSize);
			while(((record = myFile.readLine()) != null) && (record.compareTo("") != 0))
			{
				myFileW.writeBytes(record + "\n");
			}
			myFileW.close();
			myFile.setLength(myFile.length() - recSize);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CreateIndex();
	}
	
	public void replaceRecord(int id, String record)
	{
		searchResult = new DoubleLinkedList();
		searchIndex = idIdx;
		int recNo = whichRecord(String.valueOf(id), 0, searchIndex.length);
		if (recNo == -1)
			return;
		try
		{
			myFile.seek(recNo * recSize);
			myFile.writeBytes(record + "\n");
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public int generateId()
	{
		int id;
		int year = (Calendar.getInstance().get(Calendar.YEAR)) - 2000;
		String biggestID = idIdx[idIdx.length - 1].getFirst();
		if(Integer.parseInt(biggestID.substring(0, 2)) < year)
		{
			id = year * 10000;
			if (includeRole == 'T')
			{
				id = id + 9000;
			}
			else if (includeRole == 'A')
			{
				id = id + 9900;
			}
		}
		else
			id = Integer.parseInt(biggestID) + 1;
		return (id);
	}

	public Pair[] getIdIdx()
	{
		return(idIdx);
	}
	
	public Pair[] getLastNameIdx()
	{
		return(lastNameIdx);
	}

	public String getFileName() {
		return fileName;
	}
	
	public DoubleLinkedList getAll()
	{
		DoubleLinkedList dl = new DoubleLinkedList();
		for(int i = 0; i < lastNameIdx.length; i++)
		{
			String record = null;
			try 
			{
				myFile.seek(lastNameIdx[i].getSecond() * recSize);
				record = myFile.readLine();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Object item = null;
			if (includeRole == 'S')
			{
				item = new Student(record);
			}
			if (includeRole == 'T')
			{
				item = new Teacher(record);
			}
			if (includeRole == 'A')
			{
				item = new OfficeAdmin(record);
			}
			dl.insertTail(item);
		}
		return (dl);
	}
}
