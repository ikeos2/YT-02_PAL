package edu.iastate.pal.network;

/**
 * Storage for the active user's 64-character authenticator token, username, and user type.
 *
 * Create a static instance of this class when logging in through LoginActivity; this will
 * then allow access to the active user's data from subsequent Activities.
 *
 * @author Nathan Cool
 */
public class ActiveUser {

    private String token;
    private String username;
    private String userType;

    public ActiveUser() {
        setToken("");
        setUsername("");
        setUserType("");
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }
}
