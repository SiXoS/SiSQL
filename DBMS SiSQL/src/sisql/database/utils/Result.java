package sisql.database.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Result {
	
	private boolean success;
	private LinkedList<DBRow> rows;
	
	public Result(LinkedList<DBRow> rows){
		this.rows = rows;
	}
	
	public Result(){
		rows = new LinkedList<DBRow>();
	}
	
	public void addRow(DBRow row){
		rows.add(row);
	}
	
	public DBRow getLatestRow(){
		return rows.getLast();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public LinkedList<DBRow> getRows() {
		return rows;
	}

	public void setRows(LinkedList<DBRow> rows) {
		this.rows = rows;
	}

}
