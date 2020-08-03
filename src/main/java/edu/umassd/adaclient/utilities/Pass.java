package edu.umassd.adaclient.utilities;
import java.util.concurrent.Semaphore;

public class Pass {
	
	private static Semaphore s = new Semaphore(1);
	private static int wait = 0;
	
	public static void update(int a)
	{
		try {
		s.acquire();
		wait = a;
		s.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static int getValue()
	{
		try {
		s.acquire();
		s.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return wait;
	}

}
