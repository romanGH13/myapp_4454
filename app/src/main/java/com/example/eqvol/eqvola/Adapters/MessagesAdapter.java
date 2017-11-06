package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eqvol on 06.11.2017.
 */

public class MessagesAdapter  extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    List<Message> messages;
    Map<Integer, Bitmap> images;

    public MessagesAdapter(@NonNull Context context, List<Message> messages) {
        //super(context, R.layout.fragment_support_chat, tickets);
        this.ctx = context;
        this.messages = messages;
        this.lInflater = LayoutInflater.from(this.ctx);// ctx.getSystemService(Context.);
        this.images = new HashMap<Integer, Bitmap>();
    }

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

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.chat_message_item, null);
        }

        ImageView image = (ImageView)convertView.findViewById(R.id.message_image);
        TextView messageText = ((TextView) convertView.findViewById(R.id.message_text));

        if(message.getUser().getId() == Api.user.getId()) {
            User user = Api.user;

            byte[] avatar = user.getAvatar();
            if(avatar != null) {
                Bitmap bitmap;
                if(this.images.containsKey(user.getId())) {
                    bitmap = this.images.get(user.getId());

                } else{
                    byte[] imageBites = avatar;
                    bitmap = BitmapFactory.decodeByteArray(imageBites, 0, imageBites.length);
                    bitmap = bitmap.createScaledBitmap(bitmap, 50, 50, false);
                    this.images.put(user.getId(), bitmap);
                }
                image.setImageBitmap(bitmap);
            }
        }
        else{
            
        }
        messageText.setText(message.getMessage());

        /*String messageText = message.getMessage();
        String userName = message.getUser().getName();
        String data = message.getDate();

        /*int userId = message.getMessage().getUser().getId();
        User user = ticket.getMessage().getUser();

        for(User u: Api.users)
        {
            if(u.getId() == userId)
            {
                user = u;
                break;
            }
        }

        String userName = user.getName();
        byte[] avatar = user.getAvatar();
        if(avatar != null) {
            Bitmap bitmap;
            if(this.images.containsKey(user.getId())) {
                bitmap = this.images.get(user.getId());

            } else{
                byte[] image = avatar;
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                bitmap = bitmap.createScaledBitmap(bitmap, 50, 50, false);
                this.images.put(user.getId(), bitmap);
            }
            img.setImageBitmap(bitmap);
        }
        tvName.setText(userName);
        tvTitle.setText(title);

        //tv.setText("1:"+name);
        /*if(Api.user.getCountry() == country.getName())
        {
            parent.dispatchSetSelected(true);
        }*/
        return convertView;
    }
}
