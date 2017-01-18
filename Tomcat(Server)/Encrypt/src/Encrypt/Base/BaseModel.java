package Encrypt.Base;

public class BaseModel {
	private String desKey;
	
	public BaseModel(String desKey){
		this.desKey=desKey;
	}
	

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	public String getDesKey() {
		return desKey;
	}
}
