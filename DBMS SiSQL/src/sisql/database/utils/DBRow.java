package sisql.database.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class DBRow {
	
	private Map<String, DBItem> items;

	public DBRow(List<DBItem> items){
		
		this.items = new HashMap<String, DBItem>(items.size());
		setItems(items);
	}
	
	public DBRow(){
		this.items = new HashMap<String, DBItem>();
	}
	
	public DBItem getColumn(String name){
		return items.get(name);
	}

	public Collection<DBItem> getItems() {
		return items.values();
	}

	public void setItems(List<DBItem> items) {
		for(DBItem item : items)
			this.items.put(item.getColumn(), item);
	}
	
	public void addItem(DBItem item){
		items.put(item.getColumn(),item);
	}
	
}
