package com.websystique.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;



public class readTextFile 
{
    public  void saveJSONResponse(String fullFilePathName, String Response, String preExtention)
    {
    	String newFileName = fullFilePathName + "." + preExtention +".json";

    	try{
    	    PrintWriter writer = new PrintWriter(newFileName, "UTF-8");
    	    writer.write(Response);
    	    writer.close();
    	} catch (IOException e) {
			e.printStackTrace();
    	}
    	
    }
    public static ArrayList<String> getDirectoryFileNames(String directory)
    {
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> returnStringArray = new ArrayList<String>();
		String fullFilePathName = null;
		String fullDirectoryName = null;
		if (!directory.endsWith("/"))
		{
			fullDirectoryName = directory +"/"; 
		}
	
	    for (int i = 0; i < listOfFiles.length; i++) 
	    {
			if (listOfFiles[i].isFile()) 
			{
//			    System.out.println("File " + listOfFiles[i].getName());
			    fullFilePathName = fullDirectoryName + listOfFiles[i].getName();
//			    System.out.println("File " + fullFilePathName);
			    returnStringArray.add(fullFilePathName);
			} else if (listOfFiles[i].isDirectory()) 
			{
//			    System.out.println("Directory " + listOfFiles[i].getName());
			}
	    }
	    return returnStringArray;
    }
    
    public static String fileName(String file)
    {
		InputStream is = null;
		try 
		{
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));

		String line = "";
		try 
		{
			line = buf.readLine();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		        
		while(line != null)
		{
		   sb.append(line).append("\n");
		   try 
		   {
				line = buf.readLine();
		   } 
		   catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	return sb.toString();
	}
}
