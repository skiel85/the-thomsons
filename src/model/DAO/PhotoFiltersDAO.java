package model.DAO;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class PhotoFiltersDAO {

	  public static void main ( String [] args ){
		  
		  ArrayList<String> filters = getFilterNames();
		  
		  Iterator<String> i = filters.iterator();
		  
	      while ( i.hasNext() ) {
	    	  
	    	    System.out.println( "Filter Name: " + i.next() );
	    	  
	      }
		  
	  }
	
	  public static ArrayList<String> getFilterNames ( ){
		  
		  ArrayList<String> filters = new ArrayList<String>();
		  
		    try{

		            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		            Document doc = docBuilder.parse (new File("filters.xml"));

		            // normalize text representation
		            doc.getDocumentElement ().normalize ();
		            NodeList listOfPersons = doc.getElementsByTagName("filter");
		            int totalPersons = listOfPersons.getLength();
		            System.out.println("Total of filters : " + totalPersons);
		            
		            
		            

		            for(int s=0; s<listOfPersons.getLength() ; s++){


		                Node firstPersonNode = listOfPersons.item(s);
		                if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


		                    Element firstPersonElement = (Element)firstPersonNode;

		                    //-------
		                    NodeList firstNameList = firstPersonElement.getElementsByTagName("name");
		                    Element firstNameElement = (Element)firstNameList.item(0);

		                    NodeList textFNList = firstNameElement.getChildNodes();
		                    
		                    System.out.println("Filter Name : " + 
		                           ((Node)textFNList.item(0)).getNodeValue().trim());
		                    
		                    filters.add( 	 ((Node)textFNList.item(0)).getNodeValue().trim()	);

		                }//end of if clause


		            }//end of for loop with s var

		            
		            
		        }catch (SAXParseException err) {
		        System.out.println ("** Parsing error" + ", line " 
		             + err.getLineNumber () + ", uri " + err.getSystemId ());
		        System.out.println(" " + err.getMessage ());

		        }catch (SAXException e) {
		        Exception x = e.getException ();
		        ((x == null) ? e : x).printStackTrace ();

		        }catch (Throwable t) {
		        t.printStackTrace ();
		        }
		        //System.exit (0);

		        return filters;
		        
		    }//end of main

	
}
