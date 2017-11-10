package com.example.eqvol.eqvola.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.ChatActivity;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Attachment;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.SupportChat;

import java.util.HashMap;
import java.util.List;

/**
 * Created by eqvol on 06.11.2017.
 */

public class MessagesAdapter  extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ChatActivity chatActivity;
    List<Message> messages;


    public MessagesAdapter(@NonNull Context context, Activity activity, List<Message> messages) {
        //super(context, R.layout.fragment_support_chat, tickets);
        this.ctx = context;
        this.lInflater = LayoutInflater.from(this.ctx);
        this.chatActivity = (ChatActivity)activity;
        this.messages = messages;
    }

    public void addMessages(List<Message>newMessages)
    {
        messages.addAll(newMessages);
        ChatActivity.lvMessages.setSelection(messages.size()-1);
    }

    public Message getMessageById(int id)
    {
        for(Message m: messages){
            if(m.getId() == id){
                return m;
            }
        }
        return null;
    }

    /*public void setAttachment(int message_id)
    {
        Message message = getMessageById(message_id);
    }*/

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);
        ImageView image = null;
        TextView messageText = null;
        Bitmap bitmap = null;
        byte[] avatar = null;
        User user = null;

        if(message.getUser().getId() == Api.user.getId()) {
            convertView = lInflater.inflate(R.layout.chat_message_right_item, null);
        }
        else{
            convertView = lInflater.inflate(R.layout.chat_message_item, null);
        }
        user = chatActivity.getUserById(message.getUser().getId());
        avatar = user.getAvatar();
        if(avatar != null) {

            if(SupportChat.images.containsKey(user.getId())) {
                bitmap = SupportChat.images.get(user.getId());

            } else{
                byte[] imageBites = avatar;
                bitmap = BitmapFactory.decodeByteArray(imageBites, 0, imageBites.length);
                bitmap = bitmap.createScaledBitmap(bitmap, 70, 70, false);
                SupportChat.images.put(user.getId(), bitmap);
            }

        }

        image = (ImageView)convertView.findViewById(R.id.message_image);
        messageText = ((TextView) convertView.findViewById(R.id.message_text));
        ImageView imageAttachment = (ImageView) convertView.findViewById(R.id.message_attachment);

        String str = message.getMessage();
        if(str.contentEquals("")){

            Attachment attachment = message.getAttachment();
            byte[] attachmentData = attachment.getData();
            if(attachmentData == null) {

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("attachment", attachment);

                AsyncHttpTask sendMessageTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ATTACHMENT, (Activity) this.ctx);
                sendMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else{
                Bitmap bitmapAttachment = null;
                bitmapAttachment = BitmapFactory.decodeByteArray(attachmentData, 0, attachmentData.length);
                imageAttachment.setImageBitmap(bitmapAttachment);
                imageAttachment.setVisibility(View.VISIBLE);
                messageText.setVisibility(View.GONE);
            }
        }
        else {
            messageText.setText(str);
        }
        image.setImageBitmap(bitmap);

        return convertView;
    }
}
