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
		
		
		
		try {
			Table table = new Table("test");
			find(table);
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
	
	private static void find(Table table){
		List<DBItem> search = new ArrayList<DBItem>();
		search.add(new IntValue("id",32));
		search.add(new StringValue("namn","Simon Lindhén"));
		System.out.println(table.find(search));
	}

}
