package sisql.database.utils;

public abstract class DBItem<T> implements Comparable<DBItem> {
	
	private String column;
	
	public DBItem(String column){
		this.setColumn(column);
	}
	
	abstract public void setValue(T value);
	abstract public T getValue();

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
}
