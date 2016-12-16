package edu.iastate.pal;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by evanl on 12/6/2016.
 */

public class ChatMessageReceiver {
    private Socket connection;
    private Context context;

    private BufferedReader reader;


    public interface MessageReceivedListener{
        void onMessageReceived(String msg);
    }

    private MessageReceivedListener listener;


    public ChatMessageReceiver(Socket client, Context context, Activity activity){
        connection = client;
        this.context = context;
        listener = (MessageReceivedListener) activity;
    }

    public void receiveMessageFromServer() throws IOException {
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while(connection != null){
            try {
                final String message = reader.readLine();
                final Handler mainHandler = new Handler(context.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onMessageReceived(message);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


