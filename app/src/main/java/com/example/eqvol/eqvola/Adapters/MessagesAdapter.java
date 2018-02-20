package com.example.eqvol.eqvola.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if(messages != null) {
            for (Message m : messages) {
                if (m.getId() == id) {
                    return m;
                }
            }
        }
        return null;
    }
    @Override
    public int getCount() {
        if(messages == null)
            return 0;
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        if(messages == null)
            return null;
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(messages == null)
            return 0;
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        Message message = getItem(position);
        ImageView image = null;
        TextView messageText = null;
        TextView messageTime = null;
        Bitmap bitmap = null;
        byte[] avatar = null;
        User user = null;
        if(message == null)
        {
            return null;
        }
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
        messageTime = ((TextView) convertView.findViewById(R.id.message_time));
        ImageView imageAttachment = (ImageView) convertView.findViewById(R.id.message_attachment);

        String str = message.getMessage();
        if(str.contentEquals("")){

            Attachment attachment = message.getAttachment();
            if(attachment!=null) {
                byte[] attachmentData = attachment.getData();
                if (attachmentData == null) {

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("token", Api.getToken());
                    params.put("attachment", attachment);

                    AsyncHttpTask sendMessageTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ATTACHMENT, (Activity) this.ctx);
                    sendMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Bitmap bitmapAttachment = null;
                    bitmapAttachment = BitmapFactory.decodeByteArray(attachmentData, 0, attachmentData.length);
                    imageAttachment.setImageBitmap(bitmapAttachment);
                    imageAttachment.setVisibility(View.VISIBLE);
                    messageText.setVisibility(View.GONE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {


                            ImageView mImageView = (ImageView) v.findViewById(R.id.message_attachment);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Bitmap bmp = drawableToBitmap(mImageView.getDrawable());
                            Uri uri = getImageUri(v.getContext(), bmp);
                            intent.setDataAndType(uri, "image/*");
                            parent.getContext().startActivity(intent);
                        } catch(Exception ex){}
                    }
                });
            }
        }
        else {
            messageText.setText(str);
        }
        image.setImageBitmap(bitmap);

        //set time
        String time = "";
        SimpleDateFormat formatter = new SimpleDateFormat(ctx.getString(R.string.date_format));
        Date date = null;
        DateFormat dateFormat = null;
        try {
            date = formatter.parse(message.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date != null) {
            long messageDays = timestampToDays(date.getTime());
            long currentDays = timestampToDays(new Date().getTime());
            if (currentDays - messageDays == 0) {
                dateFormat = new SimpleDateFormat("h:mm a");
            } else if (currentDays - messageDays < 365) {
                dateFormat = new SimpleDateFormat("MMM dd");
            } else {
                dateFormat = new SimpleDateFormat("yyyy MMM");
            }
            time = dateFormat.format(date.getTime());
        }
        messageTime.setText(time);

        return convertView;
    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private long timestampToDays(long timestamp)
    {
        long seconds = timestamp / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }
}
