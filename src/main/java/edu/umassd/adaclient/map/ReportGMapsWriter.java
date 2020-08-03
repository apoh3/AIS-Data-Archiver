package edu.umassd.adaclient.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;

public class ReportGMapsWriter {
	

	File temp;
	FileWriter fileWrite;
	BufferedWriter file;
	LinkedList<String> lat;
	LinkedList<String> lon;
	LinkedList<String> lat2;
	LinkedList<String> lon2;
	
	public ReportGMapsWriter(LinkedList<String> lt, LinkedList<String> ln, LinkedList<String> lt2, LinkedList<String> ln2)
	{
		
		lat = lt;
		lon = ln;
		lat2 = lt2;
		lon2 = ln2;
		
		File check = new File("res/ReportMaps.html");
		temp = new File("res/gMapsHeader.txt");
		if(!temp.exists())
			try { temp.createNewFile(); } catch(Exception e) { 
				e.printStackTrace(); System.out.println("Working Directory = " +
			              System.getProperty("user.dir"));
				}
		
		temp = new File("res/gMapsFooterForReport.txt");
		if(!temp.exists())
			try { temp.createNewFile(); } catch(Exception e) { e.printStackTrace(); }
		
		if(!check.exists())
		{
			try
			{
				//create file
				check.createNewFile();
				
				//initialize writer objects
				fileWrite = new FileWriter(new File("res/ReportMaps.html"));
				file = new BufferedWriter(fileWrite);
				
			}
			catch(Exception e)
			{
				//WRITE TO ERROR LOG HERE
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				check.delete();
				check.createNewFile();
				//initialize writer objects
				fileWrite = new FileWriter(new File("res/ReportMaps.html"));
				//file = new BufferedWriter(fileWrite);
			}
			catch(Exception e)
			{
				//WRITE TO ERROR LOG HERE
				e.printStackTrace();
			}
		}

	}
	
	//returns false if either mmsi list is empty
	public boolean write()
	{
		if(lat.isEmpty())
			return false;
		
		if(lat2.isEmpty())
			return false;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("res/gMapsHeader.txt"));
			String temp;
			while((temp = in.readLine()) != null)
			{
				fileWrite.write(temp + "\n");
			}
			in.close();
			
			boolean i = true;
			while(!lat.isEmpty())
			{
				if(i)
				{
					fileWrite.write("          {lat: "+lat.removeFirst()+", lng: "+lon.removeFirst()+"} \n");
					i = false;
				}
				else
					fileWrite.write(",          {lat: "+lat.removeFirst()+", lng: "+lon.removeFirst()+"} \n");
				
				
			}
			i = true;
			
			fileWrite.write("];\n\n");
			fileWrite.write("var flightPlanCoordinates2 = [");
			
			while(!lat2.isEmpty())
			{
				if(i)
				{
					fileWrite.write("          {lat: "+lat2.removeFirst()+", lng: "+lon2.removeFirst()+"} \n");
					i = false;
				}
				else
					fileWrite.write(",          {lat: "+lat2.removeFirst()+", lng: "+lon2.removeFirst()+"} \n");
				
				
			}
			fileWrite.write("];\n\n");
			
			
			
			in = new BufferedReader(new FileReader("res/gMapsFooterForReport.txt"));
			while((temp = in.readLine()) != null)
			{
				fileWrite.write(temp +"\n");
			}
			in.close();
			fileWrite.flush();
			fileWrite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void clearMap()
	{
		try {
			BufferedReader in = new BufferedReader(new FileReader("res/cleanMap.txt"));
			String temp;
			while((temp = in.readLine()) != null)
			{
				fileWrite.write(temp + "\n");
			}
			in.close();
			fileWrite.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
