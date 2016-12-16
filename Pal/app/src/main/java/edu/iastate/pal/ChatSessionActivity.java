package edu.iastate.pal;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import edu.iastate.pal.templates.ChatMessage;


public class ChatSessionActivity extends AppCompatActivity implements ChatMessageReceiver.MessageReceivedListener, ChatMessageSender.SocketReader{
    private static final int CLASS_CONNECTION = 1;
    private static final int CONNECTION_CONNECT = 0;
    private static final int SEND_MESSAGE = -1;
    private static final int CONNECTION_DISCONNECT = -2;

    private ChatMessageSender sendMessagesHandler;
    private HandlerThread sendMessageThread;
    private Message message;

    private ChatMessageAdapter messageAdapter;
    private ArrayList<ChatMessage> messages;
    private Toolbar actionBar;
    private EditText textInput;
    private Button sendButton;

    private String editTextString;

    private boolean StopThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextString = textInput.getText().toString();

                message = Message.obtain( sendMessagesHandler, SEND_MESSAGE, CONNECTION_CONNECT, 0, editTextString);
                sendMessagesHandler.sendMessage(message);

                textInput.setText("");
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        message = Message.obtain( sendMessagesHandler, CLASS_CONNECTION, CONNECTION_DISCONNECT, 0);
        sendMessagesHandler.sendMessage(message);
        StopThread = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sendMessageThread.quit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        sendMessageThread = new HandlerThread("SendMessages");
        sendMessageThread.start();

        sendMessagesHandler = new ChatMessageSender(sendMessageThread.getLooper(), this, this);

        //Setting up the socket
        message = Message.obtain(sendMessagesHandler, CLASS_CONNECTION, CONNECTION_CONNECT);
        sendMessagesHandler.sendMessage(message);

        StopThread = false;
    }

    private void initUI() {
        setContentView(R.layout.activity_chat_session);
        textInput = (EditText)findViewById(R.id.activity_chatSession_textInput);
        sendButton = (Button)findViewById(R.id.activity_chatSession_sendButton);
        ListView messageList = (ListView) findViewById(R.id.activity_chatSession_chatListView);
        messages = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(this, messages);
        messageList.setAdapter(messageAdapter);

        actionBar = (Toolbar) findViewById(R.id.activity_chatSession_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.s_generic_word_chat));
    }

    @Override
    public void onMessageReceived(String msg) {
        String[] splitStr = msg.split(": ");
        if(!msg.equals("") && !splitStr[0].equals("Please enter your user ID:") && !splitStr[0].equals(" null")) {

            boolean isSelf = false;
            if(splitStr[0].equals(" " + LoginActivity.activeUser.getUsername())){
                isSelf = true;
            }

            if(splitStr[1].equals(" quit!") && !isSelf){
                Toast.makeText(this, splitStr[0] + " left group chat", Toast.LENGTH_SHORT).show();
            }else{
                ChatMessage message = new ChatMessage(splitStr[1], splitStr[0], isSelf);
                messages.add(message);
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void sendSocketToReader(Socket socket) {
        final ChatMessageReceiver receiver = new ChatMessageReceiver(socket, this, this);
        Thread receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!StopThread){
                        receiver.receiveMessageFromServer();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiverThread.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                message = Message.obtain(sendMessagesHandler, SEND_MESSAGE, CONNECTION_CONNECT, 0, LoginActivity.activeUser.getUsername());
                sendMessagesHandler.sendMessage(message);
            }
        }, 100);

    }
}
