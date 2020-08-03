package edu.umassd.adaclient.utilities;

public class CheckMMSI {
	public static final int NOT_ENOUGH_DIGITS = 0;
	public static final int NOT_ALL_DIGITS = 1;
	public static final int TRUE = 2;
	
	public static int isMmsiValid(String mmsi)
	{
		if(mmsi.length() != 9)
			return NOT_ENOUGH_DIGITS;
		
		for(int i = 0; i<9; i++)
		{
			if(!Character.isDigit(mmsi.charAt(i)))
			{
				return NOT_ALL_DIGITS;
			}
		}
		
		return TRUE;
		
	}
	
	public static String getErrorMessage(int error)
	{
		if(error == NOT_ENOUGH_DIGITS)
			return "The MMSI entered is invalid because it is not 9 digits.";
		
		if(error == NOT_ALL_DIGITS)
			return "The MMSI entered is invalid. Please be sure to only use digits.";
		
		return "null";
	}
}
