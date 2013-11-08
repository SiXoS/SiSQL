package sisql.xmlhandlers;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import sisql.database.utils.Column;


/**
 * 
 * Responsible for loading the configuration of a table, the information can be retrieved via @see getColumns
 * 
 * @author SiXoS
 *
 */
public class TableLoader extends DefaultHandler {
	
	private Map<String,Column> columns;
	
	public TableLoader(){
		columns = new HashMap<String, Column>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes){
		
		if(qName.equalsIgnoreCase("COLUMN")){
			Column col = new Column(attributes.getValue("name"), Column.Type.fromString(attributes.getValue("type")));
			if(attributes.getValue("index") != null && attributes.getValue("index").equalsIgnoreCase("INDEX"))
				col.setIndex(true);
			columns.put(attributes.getValue("name"), col);
		}
		
	}
	
	/**
	 * 
	 * @return The columns holding information about the table. The key is the column name.
	 */
	public Map<String,Column> getColumns(){ return columns; }

}
