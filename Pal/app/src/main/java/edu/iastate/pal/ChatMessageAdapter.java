package edu.iastate.pal;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.iastate.pal.templates.ChatMessage;

/**
 * Created by evanl on 12/6/2016.
 */

public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private List<ChatMessage> messageList = null;

    private static class ViewHolder{
        TextView msg;
        TextView sender;
    };

    public ChatMessageAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messageList = messages;
    }


    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChatMessageAdapter.ViewHolder viewHolder;

        viewHolder = new ChatMessageAdapter.ViewHolder();
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if(messageList.get(position).isSelf()){
            row = inflater.inflate(R.layout.activity_chatmessage_view_right, parent, false);
            viewHolder.msg = (TextView) row.findViewById(R.id.activity_chatmessage_adapter_messageView);
            viewHolder.sender = (TextView) row.findViewById(R.id.activity_chatmessage_adapter_senderView);
        }else{
            row = inflater.inflate(R.layout.activity_chatmessage_view_left, parent, false);
            viewHolder.msg = (TextView) row.findViewById(R.id.activity_chatmessage_adapter_messageView);
            viewHolder.sender = (TextView) row.findViewById(R.id.activity_chatmessage_adapter_senderView);
        }

        row.setTag(viewHolder);

        ChatMessage message = messageList.get(position);

        viewHolder.msg.setText(message.getMsg());
        viewHolder.sender.setText(message.getSender());

        return row;
    }


}
