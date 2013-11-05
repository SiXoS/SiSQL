package sisql;

import java.util.ArrayList;
import java.util.List;

import sisql.database.Table;
import sisql.database.utils.Column;
import sisql.database.utils.DBItem;
import sisql.database.utils.DBRow;
import sisql.database.utils.IntValue;
import sisql.database.utils.StringValue;
import sisql.exceptions.NameFormatException;

public class Main {

	public static void main(String[] args) {
		
		/*if(args.length != 1){
			help();
			return;
		}*/
		
		//String sql = args[0];
		
		ArrayList<DBItem> items = new ArrayList<DBItem>();
		//items.add(new StringValue("namn", "Simon"));
		items.add(new IntValue("index", 12));
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column("namn", Column.Type.VARCHAR));
		cols.add(new Column("telefon", Column.Type.VARCHAR));
		Column testIndex = new Column("nyckel", Column.Type.INT);
		testIndex.setIndex(true);
		cols.add(testIndex);
		
		try {
			new Table("test2",cols);
			Table test = new Table("test");
			for(DBRow rows : test.find(items).getRows()){
				for(DBItem item : rows.getItems())
					System.out.println(item.getColumn() + ":" + item.getValue() + " ");
				System.out.println();
			}
		} catch (NameFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void help(){
		System.out.println("Usage: enter your SQL as parameter");
	}

}
