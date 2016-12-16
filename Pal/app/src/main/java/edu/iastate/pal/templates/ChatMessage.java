package edu.iastate.pal.templates;

/**
 * Created by evanl on 12/6/2016.
 */

public class ChatMessage {
    private String msg;
    private String sender;
    private boolean isSelf;

    public ChatMessage(String msg, String sender, boolean isSelf){
        this.msg = msg;
        this.sender = sender;
        this.isSelf = isSelf;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
