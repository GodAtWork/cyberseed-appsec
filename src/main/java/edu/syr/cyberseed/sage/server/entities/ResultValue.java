package edu.syr.cyberseed.sage.server.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResultValue {

	@NotNull
	@Size(min = 1, max = 50)
	private String result;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
