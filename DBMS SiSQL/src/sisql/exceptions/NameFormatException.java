package sisql.exceptions;

public class NameFormatException extends Exception {
	
	private String validChars;

	public NameFormatException(String validChars, String message){
		super(message);
		this.validChars = validChars;
	}
	
	@Override
	public String getMessage(){
		return super.getMessage() + " Valid characters: " + this.validChars;
	}

	public String getValidChars() {
		return validChars;
	}

	public void setValidChars(String validChars) {
		this.validChars = validChars;
	}
	
}
