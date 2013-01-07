
public class Pair 
{
	private String first;
	private int second;
	public Pair(String first, int second) { this.first = first; this.second = second; }

	public static boolean same(String first, String second) {
		return first == null ? second == null : first.equals(second);
	}

	String getFirst() { return first; }
	int getSecond() { return second; }

	void setFirst(String o) { first = o; }
	void setSecond(int o) { second = o; }
}