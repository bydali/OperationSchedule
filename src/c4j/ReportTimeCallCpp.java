package c4j;

public class ReportTimeCallCpp {
	static 
	{
		System.loadLibrary("C4J");
	}
	
	public native static int myadd(int a , int b);
}
