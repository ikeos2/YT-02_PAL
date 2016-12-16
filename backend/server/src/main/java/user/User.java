package user;

public class User {
    private long uid;
    private String username;
    private String password;
    private String user_type;
    private String user_active;
    private boolean loggedin;
    private String user_email;

    public User(long id, String uname, String pw, String type, String active, boolean loggedin, String email) {
        this.uid = id;
        this.username = uname;
        this.user_type = type;
        this.user_active = active;
        this.loggedin = loggedin;
        this.user_email = email;
        setPassword(pw);
    }

    public long getId() {
        return uid;
    }
    
    public String getUsername(){
    	return username;
    }
    
    public String getPassword(){
    	return password;
    }
    
    public void setPassword(String newpw){
    	password = newpw;
    }
    
    public String getUserType(){
    	return user_type;
    }
    
    public String getUserActive(){
    	return user_active;
    }
    
    public boolean getLoggedin(){
    	return loggedin;
    }
    
    public String getEmail(){
    	return user_email;
    }
}
