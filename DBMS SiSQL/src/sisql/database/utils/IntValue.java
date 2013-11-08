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

}
