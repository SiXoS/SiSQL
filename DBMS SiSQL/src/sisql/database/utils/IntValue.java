package sisql.database.utils;

public class IntValue extends DBItem<Integer> {
	
	private int value;

	public IntValue(String column, int value){
		super(column);
		this.value = value;
	}
	
	@Override
	public void setValue(Integer value) {
		this.value = value;
		
	}

	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object compare){
		
		IntValue comp;
		
		if(compare instanceof IntValue)
			comp = (IntValue) compare;
		else
			return false;
					
		if(comp.getValue() == value && comp.getColumn().equals(getColumn()))
			return true;
		return false;
		
	}

	@Override
	public int compareTo(DBItem comp) {
		if(!(comp instanceof IntValue)){
			System.err.println("WARNING: A comparison between two diffrent types of values was made.");
			return 0;
		}else{
			IntValue compInt = (IntValue)comp;
			return this.getValue().intValue()-compInt.getValue().intValue();
		}
	}

}
