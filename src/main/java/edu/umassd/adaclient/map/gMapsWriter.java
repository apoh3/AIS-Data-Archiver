package edu.umassd.adaclient.map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;


public class gMapsWriter {
	

	File temp;
	FileWriter fileWrite;
	BufferedWriter file;
	LinkedList<String> lat;
	LinkedList<String> lon;
	LinkedList<String> info;
	
	private final String infoBoxHeader = "var contentString ='<div id=\"content\"><div id=\"siteNotice\"></div>'+\n" + 
			"            '<h1 id=\"firstHeading\" class=\"firstHeading\">";
	private final String infoBoxBody ="            '<div id=\"bodyContent\">'+";
	private final String infoBoxFooter = "            '</div>'+\n" + 
			"            '</div>';";
	
	public gMapsWriter(LinkedList<String> lt, LinkedList<String> ln, LinkedList<String> info)
	{
		
		lat = lt;
		lon = ln;
		this.info = info;
		
		File check = new File("res/GoogleMaps.html");
		temp = new File("res/gMapsHeader.txt");
		if(!temp.exists())
			try { temp.createNewFile(); } catch(Exception e) { 
				e.printStackTrace(); System.out.println("Working Directory = " +
			              System.getProperty("user.dir"));
				}
		
		temp = new File("res/gMapsFooter.txt");
		if(!temp.exists())
			try { temp.createNewFile(); } catch(Exception e) { e.printStackTrace(); }
		
		if(!check.exists())
		{
			try
			{
				//create file
				check.createNewFile();
				
				//initialize writer objects
				fileWrite = new FileWriter(new File("res/GoogleMaps.html"));
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
				fileWrite = new FileWriter(new File("res/GoogleMaps.html"));
				//file = new BufferedWriter(fileWrite);
			}
			catch(Exception e)
			{
				//WRITE TO ERROR LOG HERE
				e.printStackTrace();
			}
		}

	}
	
	public void write()
	{
		
		
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
			fileWrite.write("];\n\n");
			

			fileWrite.write(infoBoxHeader + info.removeFirst()+"</h1>'+");
			fileWrite.write(infoBoxBody + "'<b>MMSI: " + info.removeFirst() +"<br>Flag: "+ info.removeFirst()+"<br>Lat: "+info.removeFirst()+"<br>Lon: "+info.removeFirst()+"'+");
			fileWrite.write(infoBoxFooter);
			
			
			in = new BufferedReader(new FileReader("res/gMapsFooter.txt"));
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
