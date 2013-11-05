package sisql.database.utils;

public class FieldColumnPair {
	
	private String columnName;
	private DBItem value;
	
	public FieldColumnPair(String columnName, DBItem value){
		this.columnName = columnName;
		this.value = value;
	}

}
