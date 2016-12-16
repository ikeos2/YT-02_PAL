package edu.iastate.pal;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by evanl on 11/8/2016.
 */

public class ChatMessageSender extends Handler{
    private static final int CLASS_CONNECTION = 1;
    private static final int CONNECTION_CONNECT = 0;

    private boolean isConnected;
    private Socket client;
    private PrintWriter printWriter;
    private Message message;
    private Context context;

    public interface SocketReader{
        void sendSocketToReader(Socket socket);
    }

    private  SocketReader listener;

    public ChatMessageSender(Looper looper, Context context, Activity activity) {
        super(looper);
        isConnected = false;
        this.context = context;
        listener = (SocketReader) activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        message = msg;
        if(message.what != CLASS_CONNECTION && isConnected){
            sendMessageToServer();
        }else{
            handleConnection();
        }
    }

    private void handleConnection() {
        if(message.arg1 == CONNECTION_CONNECT) {
            makeConnection();
        }else {
            closeConnection();
        }
    }

    private void closeConnection() {
        printWriter.println("quit!");
        printWriter.close();

        try {
            client.close();
            isConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        quit();
    }

    private void makeConnection() {
        try {
            client = new Socket("proj-309-yt-02.cs.iastate.edu", 9001);
            Log.d("ConnectionInfo", client.toString());

            final Handler mainHandler = new Handler(context.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.sendSocketToReader(client);
                }
            });

            printWriter = new PrintWriter(client.getOutputStream(), true);
            isConnected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToServer() {
        if(isConnected){
            String messageToServer = (String) message.obj;
            printWriter.println(messageToServer);
        }
    }

    private void quit() {
        getLooper().quit();
    }
}
