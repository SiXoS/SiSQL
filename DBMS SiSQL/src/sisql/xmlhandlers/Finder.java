package sisql.xmlhandlers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sisql.database.utils.Column;
import sisql.database.utils.DBItem;
import sisql.database.utils.DBRow;

/**
 * 
 * This class is responsible for parsing xml and finding rows matching to the specified criterias
 * It is a subclass of DefaultHandler to be used with a SAXParser.
 * 
 * @author SiXoS
 *
 */
//@SuppressWarnings("rawtypes") FIXME to be used on release, keeping it for debugging. Suppresses that we don't specify DBItems generic class (we can't know it)
public class Finder extends DefaultHandler {
	
	private List<DBItem> searchOn;
	private DBRow row;
	private LinkedList<DBRow> matches;
	private Map<String,Column> columns;
	private String currentColumn = null;
	private StringBuffer value;
	private boolean currentMatching = false;
	
	/**
	 * 
	 * Constructor for creating the Finder class
	 * 
	 * @param searchOn The DBItems to search for, this class will look for the DBItems column containing the DBItems value.
	 * @param columns To improve performance, the columns of the table must be specified
	 */
	public Finder(List<DBItem> searchOn, Map<String,Column> columns){
		this.searchOn = searchOn;
		this.columns = columns;
		matches = new LinkedList<DBRow>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attribute) throws SAXException{
		
		if(qName.equalsIgnoreCase("ROW")){ //If we encounter a <row> tag, then this is a beginning of a new row in the database
			currentMatching = false;       //Now we don't now if this row matches anything
			row = new DBRow();			   //Initialize a new row to store info in
			
		}else if(qName.equalsIgnoreCase("VALUE")){			//If we encounter a <value> tag:
			currentColumn = attribute.getValue("column");	//we must store what column this value represents as this can not be found later
			value = new StringBuffer();						//Prepare for recieving nested text
		}
		super.startElement(uri, localName, qName, attribute);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		if(qName.equalsIgnoreCase("VALUE")){  //If we encounter a </value> tag we know we have all information needed to include it in the current row and check if it matches the search
			
			DBItem thisItem = columns.get(currentColumn).getItem(value.toString()); //This retrieves the correct subclass of DBItem so that we can compare it with the search items
			for(DBItem search : searchOn)
				if(search.equals(thisItem))
					currentMatching = true; //If this field matches the search we mark that this row should be included as a match
					
			currentColumn = null;
			row.addItem(thisItem);
			
		}else if(qName.equalsIgnoreCase("ROW")){
			if(currentMatching)		//If this row had any matching elements,
				matches.add(row); 	//we add it to the list of found rows.
		}
		super.endElement(uri, localName, qName);
		
	}
	
	@Override
	public void characters(char[] chars, int start, int length) throws SAXException{
		if(currentColumn != null)
			value.append(chars, start, length);
		super.characters(chars, start, length);
	}
	
	/**
	 * 
	 * Returns the rows that matched the search
	 * 
	 * @return the rows that matched the search. If no rows matched, this will be empty (i.e. .size()==0)
	 */
	public LinkedList<DBRow> getMatches(){
		return matches;
	}

}
