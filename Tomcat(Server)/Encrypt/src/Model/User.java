package Model;

public class User {
	private int id=0;
    private String name;
    private String password;
    private boolean isLogin;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    public User(int id,String name){
    	this.id=id;
    	this.name=name;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setIsLogin(boolean state){
    	isLogin=state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
