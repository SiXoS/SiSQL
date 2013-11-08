package sisql.database.utils;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class DBRow {
	
	private List<DBItem> items;

	public DBRow(List<DBItem> items){
		this.setItems(items);
	}
	
	public DBRow(){
		this.items = new ArrayList<DBItem>();
	}

	public List<DBItem> getItems() {
		return items;
	}

	public void setItems(List<DBItem> items) {
		this.items = items;
	}
	
	public void addItem(DBItem item){
		items.add(item);
	}
	
}
