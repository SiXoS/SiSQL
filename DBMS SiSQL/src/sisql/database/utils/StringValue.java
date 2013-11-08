package sisql.database.utils;

public class StringValue extends DBItem<String> {

	private String value;
	
	public StringValue(String column, String value) {
		super(column);
		this.value = value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object compare){
		
		StringValue comp;
		
		if(compare instanceof StringValue)
			comp = (StringValue) compare;
		else
			return false;
		
		if(comp.getValue().equals(value) && comp.getColumn().equals(getColumn()))
			return true;
		return false;
		
	}

	@Override
	public int compareTo(DBItem comp) {
		if(!(comp instanceof StringValue)){
			System.err.println("WARNING: A comparison between two diffrent types of values was made.");
			return 0;
		}else{
			StringValue compString = (StringValue)comp;
			return this.getValue().compareTo(compString.getValue());
		}
	}

}
