package sisql.database.utils;

public class Column {
	
	private String name;
	private Type type;
	private boolean index = false;
	
	public Column(String name, Type type){
		this.setName(name);
		this.setType(type);
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public DBItem getItem(String value){
		switch(type){
			case INT:
				return new IntValue(name, Integer.parseInt(value));
			case VARCHAR:
				return new StringValue(name, value);
			default:
				return null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIndex() {
		return index;
	}

	public void setIndex(boolean index) {
		this.index = index;
	}

	public enum Type{
		INT,
		VARCHAR,
		UNDEFINED;
		
		public static Type fromString(String name){
			switch(name){
				case "INT":
					return INT;
				case "VARCHAR":
					return VARCHAR;
				default:
					return UNDEFINED;
			}
		}
		
		public boolean validate(Object toCheck){
			switch(this){
				case INT:
					return toCheck instanceof Integer;
				case VARCHAR:
					return toCheck instanceof String;
				case UNDEFINED:
					return true;
				default:
					return false;
			}
		}
	}

}
