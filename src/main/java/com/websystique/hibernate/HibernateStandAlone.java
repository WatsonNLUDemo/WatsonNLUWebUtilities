package com.websystique.hibernate;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import com.websystique.hibernate.model.NLU;

public class HibernateStandAlone {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

    	System.out.println("Hello, World");	
//		public Album(String filename, String item, String itemtext, long relevance, long count)
    	

		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2016",2016);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2015",2015);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2014",2014);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2013",2013);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2012",2012);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2011",2011);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2010",2010);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2009",2009);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2008",2008);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2007",2007);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2006",2006);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2005",2005);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2004",2004);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2003",2003);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2002",2002);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2001",2001);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/2000",2000);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/1999",1999);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/1998",1998);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/1997",1997);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/1996",1996);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/1994",1994);
		mainNLU("/home/jdellaria/Desktop/UNGA Speeches/1976",1976);

	}
	
    public static void mainNLU1(String folder, int year) {
    	
		System.out.printf("Opening File: %s, year: %d\n", folder, year);
    }
    
	public static void mainNLU(String folder, int year) {
    	readTextFile myFiles = new readTextFile();

        JSONParser parser = new JSONParser();
    	System.out.println("Hello, World");
		Session session = HibernateUtil.getSessionFactory().openSession();
       
//		

    	ArrayList<String> textArray = com.websystique.hibernate.readTextFile.getDirectoryFileNames(folder);
//		Album Album1 = new Album();
    	for(String fileName :textArray)
    	{
            try {  
//	    		String text = readTextFile.fileName(fileName);

				System.out.println("Opening File: " + fileName);
	            Object obj = parser.parse(new FileReader(fileName));
	            
	            JSONObject jsonObject = (JSONObject) obj;
    		
      
	            System.out.println("CONCEPTS");
	            JSONArray msgCONCEPTS = (JSONArray) jsonObject.get("concepts");
	//            Iterator<String> iterator = msg.iterator();
	            Iterator<org.json.simple.JSONObject> iteratorCONCEPTS = msgCONCEPTS.iterator();
	    		session.beginTransaction(); 
	            while (iteratorCONCEPTS.hasNext()) {
	            	Object objCONCEPTS = iteratorCONCEPTS.next();
	//                System.out.println(objCONCEPTS);
	                JSONObject jsonCONCEPTSObject = (JSONObject) objCONCEPTS;
	                String text = (String) jsonCONCEPTSObject.get("text");
	              	System.out.printf("text: %s", text);

	              	Double relevance = (Double) jsonCONCEPTSObject.get("relevance");
	            	NLU nlu = new NLU(fileName, "Concepts", text, null/* type*/, relevance/*relevance*/, 0/*count*/, year/*year*/, 0.0/*sentiment*/,0.0 /*anger*/,0.0 /*disgust*/,0.0 /*fear*/,0.0 /*joy*/,0.0 /*sadness*/);
	        		session.persist(nlu);

	              	System.out.printf(" - relevance: %s\n", relevance.toString());
	            }
	            
        		session.getTransaction().commit();	            
	            // loop array
	            System.out.println("CATEGORIES");
	            JSONArray msgCATEGORIES = (JSONArray) jsonObject.get("categories");
//	            Iterator<String> iterator = msg.iterator();
	            Iterator<org.json.simple.JSONObject> iteratorCATEGORIES = msgCATEGORIES.iterator();
	    		session.beginTransaction(); 
	            while (iteratorCATEGORIES.hasNext()) {
	            	Object objCATEGORIES = iteratorCATEGORIES.next();
//	                System.out.println(objCATEGORIES);
	                JSONObject jsonCATEGORIESObject = (JSONObject) objCATEGORIES;
	                String text = (String) jsonCATEGORIESObject.get("label");
	              	System.out.printf("label: %s", text);
	              	Double relevance = (Double) jsonCATEGORIESObject.get("score");
	              	System.out.printf(" - score: %s\n", relevance.toString());
	              	NLU nlu = new NLU(fileName, "Categories", text, null, relevance/*relevance*/, 0/*count*/, year/*year*/, 0.0/*sentiment*/,0.0 /*anger*/,0.0 /*disgust*/,0.0 /*fear*/,0.0 /*joy*/,0.0 /*sadness*/);

	        		session.persist(nlu);

	            }
            
        		session.getTransaction().commit();         
	            
	            // loop array
	            System.out.println("ENTITIES");
	            JSONArray msgENTITIES = (JSONArray) jsonObject.get("entities");
//	            Iterator<String> iterator = msg.iterator();
	            Iterator<org.json.simple.JSONObject> iteratorENTITIES = msgENTITIES.iterator();
	    		session.beginTransaction(); 
	            while (iteratorENTITIES.hasNext()) {
//	                System.out.println(iteratorENTITIES.next());
	                
	            	Object objENTITIES = iteratorENTITIES.next();
//	              System.out.println(objCATEGORIES);
	            	JSONObject jsonENTITIESObject = (JSONObject) objENTITIES;
	                String text = (String) jsonENTITIESObject.get("text");
	              	System.out.printf("text: %s", text);
	            	Long count = (Long) jsonENTITIESObject.get("count");
	            	System.out.printf(" - count: %s", count);

	            	String type = (String) jsonENTITIESObject.get("type");
	            	System.out.printf(" - type: %s", type);

	            	Double relevance = (Double) jsonENTITIESObject.get("relevance");
	            	System.out.printf(" - relevance: %s", relevance.toString());
	           
		            Double sentiment = 0.0;
	            	Double anger = 0.0;         
	            	Double fear = 0.0;
	            	Double disgust = 0.0;
	            	Double joy = 0.0;
	            	Double sadness = 0.0;
		            JSONObject jsonSENTIMENTObject;
		            try
		            {
		            	jsonSENTIMENTObject = (JSONObject) jsonENTITIESObject.get("sentiment");
		            	sentiment = (Double) jsonSENTIMENTObject.get("score");
		            	JSONObject jsonEMOTIONSObject  = (JSONObject) jsonENTITIESObject.get("emotion");
		            	anger = (Double) jsonEMOTIONSObject.get("anger");          
		            	fear = (Double) jsonEMOTIONSObject.get("fear"); 
		            	disgust = (Double) jsonEMOTIONSObject.get("disgust"); 
		            	joy = (Double) jsonEMOTIONSObject.get("joy"); 
		            	sadness = (Double) jsonEMOTIONSObject.get("sadness"); 
		            	System.out.printf(" - sentiment: %s", sentiment.toString());
		            	System.out.printf(" - anger: %s", anger.toString());
		            	System.out.printf(" - fear: %s", fear.toString());
		            	System.out.printf(" - disgust: %s", disgust.toString());
		            	System.out.printf(" - joy: %s", joy.toString());
		            	System.out.printf(" - sadness: %s\n", sadness.toString());
		            }
		            catch(NullPointerException e ){
		            	sentiment = 0.0;
		            	anger = 0.0;
		            	fear = 0.0;
		            	disgust = 0.0;
		            	joy = 0.0;
		            	sadness = 0.0;
		            }
		            NLU nlu = new NLU(fileName, "Entities", text, type, relevance/*relevance*/, 0/*count*/, year/*year*/, sentiment, anger, disgust, fear, joy, sadness);

	        		session.persist(nlu);

	            }
        		session.getTransaction().commit();            
	            // loop array
	            System.out.println("KEYWORDS");
	            JSONArray msgKEYWORDS = (JSONArray) jsonObject.get("keywords");
//	            Iterator<String> iterator = msg.iterator();
	            Iterator<org.json.simple.JSONObject> iteratorKEYWORDS = msgKEYWORDS.iterator();
	    		session.beginTransaction(); 
	            while (iteratorKEYWORDS.hasNext()) {
//	                System.out.println(iteratorKEYWORDS.next());
	                
	            	Object objENTITIES = iteratorKEYWORDS.next();
//	              System.out.println(objCATEGORIES);
	            	JSONObject jsonKEYWORDSObject = (JSONObject) objENTITIES;
	                String text = (String) jsonKEYWORDSObject.get("text");
	              	System.out.printf("text: %s", text);
	            	Double relevance = (Double) jsonKEYWORDSObject.get("relevance");
	            	System.out.printf(" - relevance: %s", relevance.toString());
	            		            	
		            Double sentiment = 0.0;
	            	Double anger = 0.0;         
	            	Double fear = 0.0;
	            	Double disgust = 0.0;
	            	Double joy = 0.0;
	            	Double sadness = 0.0;
		            JSONObject jsonSENTIMENTObject;
		            try
		            {
		            	jsonSENTIMENTObject = (JSONObject) jsonKEYWORDSObject.get("sentiment");
		            	sentiment = (Double) jsonSENTIMENTObject.get("score");
		            	JSONObject jsonEMOTIONSObject  = (JSONObject) jsonKEYWORDSObject.get("emotion");
		            	anger = (Double) jsonEMOTIONSObject.get("anger");          
		            	fear = (Double) jsonEMOTIONSObject.get("fear"); 
		            	disgust = (Double) jsonEMOTIONSObject.get("disgust"); 
		            	joy = (Double) jsonEMOTIONSObject.get("joy"); 
		            	sadness = (Double) jsonEMOTIONSObject.get("sadness"); 
		            	System.out.printf(" - sentiment: %s", sentiment.toString());
		            	System.out.printf(" - anger: %s", anger.toString());
		            	System.out.printf(" - fear: %s", fear.toString());
		            	System.out.printf(" - disgust: %s", disgust.toString());
		            	System.out.printf(" - joy: %s", joy.toString());
		            	System.out.printf(" - sadness: %s\n", sadness.toString());
		            
		            }
		            catch(NullPointerException e ){
		            	sentiment = 0.0;
		            }
	            	System.out.printf(" - sentiment: %s\n", sentiment.toString());	            
		            
/*		            JSONArray msgSENTIMENT = (JSONArray) jsonObject.get("sentiment");
		            Iterator<org.json.simple.JSONObject> iteratorSENTIMENT= msgSENTIMENT.iterator();
		            Double sentiment = 0.0;
		            while (iteratorSENTIMENT.hasNext()) {
		              	Object objSENTIMENT = iteratorSENTIMENT.next();
		            	sentiment = (Double) jsonKEYWORDSObject.get("score");
		            	System.out.printf(" - sentiment: %s\n", sentiment.toString());
		            }
		            */
	            	NLU nlu = new NLU(fileName, "Keyword", text, null, relevance/*relevance*/, 0/*count*/, year/*year*/, sentiment, anger, disgust, fear, joy, sadness);

	        		session.persist(nlu);

	            }
        		session.getTransaction().commit();	

	            
            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

        		System.out.println("Writing Response File: " + fileName);

    	}	

		session.close();
    	System.out.println("All Done");
    }
    
    
    public static void mainJson(String[] args) {

        JSONParser parser = new JSONParser();

        try {

//            Object obj = parser.parse(new FileReader("/home/jdellaria/Desktop/JSON/test.json"));
            Object obj = parser.parse(new FileReader("/home/jdellaria/Desktop/JSON/AFG_71.txt.nlu.json"));
            
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);


            // loop array
            System.out.println("CONCEPTS");
            JSONArray msgCONCEPTS = (JSONArray) jsonObject.get("concepts");
//            Iterator<String> iterator = msg.iterator();
            Iterator<org.json.simple.JSONObject> iteratorCONCEPTS = msgCONCEPTS.iterator();
            while (iteratorCONCEPTS.hasNext()) {
            	Object objCONCEPTS = iteratorCONCEPTS.next();
//                System.out.println(objCONCEPTS);
                JSONObject jsonCONCEPTSObject = (JSONObject) objCONCEPTS;
                String text = (String) jsonCONCEPTSObject.get("text");
              	System.out.printf("text: %s", text);
              	Double relevance = (Double) jsonCONCEPTSObject.get("relevance");
              	System.out.printf(" - relevance: %s\n", relevance.toString());
            }

            
            // loop array
            System.out.println("CATEGORIES");
            JSONArray msgCATEGORIES = (JSONArray) jsonObject.get("categories");
//            Iterator<String> iterator = msg.iterator();
            Iterator<org.json.simple.JSONObject> iteratorCATEGORIES = msgCATEGORIES.iterator();
            while (iteratorCATEGORIES.hasNext()) {
            	Object objCATEGORIES = iteratorCATEGORIES.next();
//                System.out.println(objCATEGORIES);
                JSONObject jsonCATEGORIESObject = (JSONObject) objCATEGORIES;
                String text = (String) jsonCATEGORIESObject.get("label");
              	System.out.printf("label: %s", text);
              	Double relevance = (Double) jsonCATEGORIESObject.get("score");
              	System.out.printf(" - score: %s\n", relevance.toString());
            }
            
            // loop array
            System.out.println("ENTITIES");
            JSONArray msgENTITIES = (JSONArray) jsonObject.get("entities");
//            Iterator<String> iterator = msg.iterator();
            Iterator<org.json.simple.JSONObject> iteratorENTITIES = msgENTITIES.iterator();
            while (iteratorENTITIES.hasNext()) {
//                System.out.println(iteratorENTITIES.next());
                
            	Object objENTITIES = iteratorENTITIES.next();
//              System.out.println(objCATEGORIES);
            	JSONObject jsonENTITIESObject = (JSONObject) objENTITIES;
                String text = (String) jsonENTITIESObject.get("text");
              	System.out.printf("text: %s", text);
            	Long count = (Long) jsonENTITIESObject.get("count");
            	System.out.printf(" - count: %s", count);

            	String type = (String) jsonENTITIESObject.get("type");
            	System.out.printf(" - type: %s", type);

            	Double relevance = (Double) jsonENTITIESObject.get("relevance");
            	System.out.printf(" - relevance: %s\n", relevance.toString());
            }
            
            
            // loop array
            System.out.println("KEYWORDS");
            JSONArray msgKEYWORDS = (JSONArray) jsonObject.get("keywords");
//            Iterator<String> iterator = msg.iterator();
            Iterator<org.json.simple.JSONObject> iteratorKEYWORDS = msgKEYWORDS.iterator();
            while (iteratorKEYWORDS.hasNext()) {
//                System.out.println(iteratorKEYWORDS.next());
                
            	Object objENTITIES = iteratorKEYWORDS.next();
//              System.out.println(objCATEGORIES);
            	JSONObject jsonKEYWORDSObject = (JSONObject) objENTITIES;
                String text = (String) jsonKEYWORDSObject.get("text");
              	System.out.printf("text: %s", text);
            	Double relevance = (Double) jsonKEYWORDSObject.get("relevance");
            	System.out.printf(" - relevance: %s\n", relevance.toString());
            }
   
            
            

            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
	
