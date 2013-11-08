package sisql.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import sisql.database.utils.Column;
import sisql.database.utils.DBItem;
import sisql.database.utils.DBRow;
import sisql.database.utils.Result;
import sisql.exceptions.NameFormatException;
import sisql.xmlhandlers.Finder;
import sisql.xmlhandlers.TableLoader;

public class Table {
	
	private String name;
	private Map<String,Column> columns;
	private String indexColumn;
	private int nextIndex;
	
	/**
	 * This constructor is used when a new table should be created
	 * 
	 * <b>NOTE</b> If the table exists it is removed allong with it's rows
	 * 
	 * @param	name					The table name
	 * @param	columns					The columns to store information in
	 * @throws	NameFormatException		This is thrown if the table name was malformed
	 * @throws	Exception				If no columns was specified
	 */
	public Table(String name, List<Column> columns) throws NameFormatException, Exception{
		
		this.name = name;
		this.columns = new HashMap<String, Column>();
		
		if(!checkValidTableName(name)) //must start with a letter and have a minimum of two characters
			throw new NameFormatException("a-z, A-Z, 0-9. The name must also start with a letter", "The name " + name + " is not valid."); 
		if(columns == null || columns.size() == 0)
			throw new Exception("You must specify atleast one column.");
		
		File xml = new File("Databases/" + name + ".xml");
		
		if(xml.exists()){ //empty the file if it exists
		    try {
		    	FileChannel outChan = new FileOutputStream(xml, true).getChannel();
				outChan.truncate(0);
				outChan.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Element rootElement = new Element("table");
		rootElement.setAttribute("name", name);
		rootElement.setAttribute("nextIndex", "1");
		this.nextIndex = 1;
		Document table = new Document(rootElement);
		
		boolean indexFound = false;
		Element columnList = new Element("columns");
		for(Column column : columns){
			this.columns.put(column.getName(), column);
			Element col = new Element("column");
			col.setAttribute("name", column.getName());
			col.setAttribute("type", column.getType().toString());
			if(column.isIndex()){
				if(indexFound)
					throw new Exception("There can't be two indexes.");
				col.setAttribute("index", "index");
				this.indexColumn = column.getName();
			}
			columnList.addContent(col);
		}
		if(!indexFound){ //There must always be an index
			Element indexCol = new Element("column");
			indexCol.setAttribute("name", "index");
			indexCol.setAttribute("index", "index");
			indexCol.setAttribute("type", Column.Type.INT.toString());
			columnList.addContent(indexCol);
			Column indexColItem = new Column("index", Column.Type.INT);
			indexColItem.setIndex(true);
			this.indexColumn = "index";
			this.columns.put("index", indexColItem);
		}
		rootElement.addContent(columnList);
		
		rootElement.addContent(new Element("rows"));
		
		XMLOutputter xmlOutput = new XMLOutputter();
		 
		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(table, new FileWriter(xml));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * This constructor should be used when you want to handle a table that allready exists
	 * 
	 * @param name
	 * @throws Exception
	 */
	public Table(String name) throws Exception{
		
		this.name = name;
		
		File table = new File("Databases/" + name + ".xml");
		if(!table.exists())
			throw new Exception("The table " + name + " doesn't exist.");
		
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			
		TableLoader loader = new TableLoader();
		parser.parse(table, loader);
		columns = loader.getColumns();
		
	}
	
	public Result find(List<DBItem> searchOn){
		
		File xml = new File("Databases/" + name + ".xml");
		boolean findAll = searchOn == null ? true : false;
		
		Finder find = new Finder(searchOn, columns);
		Result res = new Result();
		
		try{
		
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(xml, find);
			res.setRows(find.getMatches());
			res.setSuccess(true);
		
		}catch(Exception e){
			e.printStackTrace();
			res.setSuccess(false);
		}
		
		return res;
		
	}
	
	public int insert(List<DBRow> rows) throws Exception{
		
		File table = new File("Databases/" + name + ".xml");
		if(!table.exists())
			throw new Exception("The table " + name + " doesn't exist.");
		
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try {
	 
			document = (Document) builder.build(table);
			Element rootNode = document.getRootElement();
			this.nextIndex = Integer.parseInt(rootNode.getAttributeValue("nextIndex"));
			
			Element rowsTag = rootNode.getChild("rows");
			Element rowElement = null;
			
			for(DBRow row : rows){
				rowElement = new Element("row");
				for(DBItem item : row.getItems()){
					Column column;
					if((column = columns.get(item.getColumn())) != null){
						if(column.getType().validate(item.getValue())){
							Element value = new Element("value");
							value.setAttribute("column", column.getName());
							value.setText(item.getValue().toString());
							rowElement.addContent(value);
						}else
							System.out.println("Not valid! " + column.getName());
					}
				}
				Element index = new Element("value");
				index.setAttribute("column", this.indexColumn);
				index.setText(nextIndex++ + "");
				rootNode.setAttribute("nextIndex", nextIndex + "");
				rowElement.addContent(index);
				rowsTag.addContent(rowElement);
			}
	 
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		
		XMLOutputter xmlOutput = new XMLOutputter();
		 
		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(document, new FileWriter(table));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nextIndex-1;
		
	}
	
	
	/**
	 * Creates a user-friendly table representation of this table
	 */
	@Override
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("Table: " + name + "\r\n");
		
		int lines = columns.size()*20;
		StringBuilder fillerSpace = new StringBuilder();
		StringBuilder fillerUnderline = new StringBuilder();
		StringBuilder fillerSpaceL = new StringBuilder();
		StringBuilder fillerUnderlineL = new StringBuilder();
		for(int i=0 ; i<lines ; i++){
			fillerSpace.append(" ");
			fillerUnderline.append("_");
			if(i%20 == 0){
				fillerSpaceL.append("|");
				fillerUnderlineL.append("|");
			}else{
				fillerSpaceL.append(" ");
				fillerUnderlineL.append("_");
			}
		}
		sb.append(fillerUnderline + "\r\n");
		sb.append(fillerSpaceL + "|\r\n|");
		
		for(Entry<String,Column> column : columns.entrySet()){
			Column col = column.getValue();
			sb.append(col.getName() + ":" + col.getType());
			for(int j=0 ; j<20-(col.getName().length() + 2 + col.getType().toString().length()) ; j++)
				sb.append(" ");
			sb.append("|");
		}
		sb.append("\r\n" + fillerUnderlineL + "|\r\n");
		
		return sb.toString();
	}
	
	/**
	 * 
	 * Checks if the parameter name starts with a letter and consists of two or more characters
	 * 
	 * @param name
	 * @return if the name has a valid format
	 */
	private boolean checkValidTableName(String name){
		return name.matches("^[a-zA-Z]+([a-zA-Z0-9]+)$");
	}

}
