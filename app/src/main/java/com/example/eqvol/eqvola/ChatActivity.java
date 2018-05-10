package com.example.eqvol.eqvola;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.MessagesAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Attachment;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static Activity activity;
    private static EditText input = null;
    public static int ticket_id;
    //public int last_message_id;
    public String title;
    public static Ticket ticket;
    public static List<Message> messages;
    public static ListView lvMessages = null;
    public static boolean isActive = false;

    private static final int REQUEST_GALLERY = 2;

    public ConstraintLayout headerLayout;
    public TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        input = (EditText)findViewById(R.id.chat_message_text);
        activity = this;
        Intent intent = getIntent();
        ticket_id = intent.getIntExtra("ticket_id", 0);
        title = intent.getStringExtra("title");
        //last_message_id = intent.getIntExtra("last_message_id", 0);
        ticket = Api.ticket;

        SupportChat.images = new HashMap<Integer, Bitmap>();
        messages = Api.currentChatMessages;
        setTitle(title);


        checkUsersAvatar();

        headerLayout = (ConstraintLayout) findViewById(R.id.header);
        mTitleView = (TextView) headerLayout.findViewById(R.id.title);
        mTitleView.setText(title);

        if(ticket != null && ticket.getStatus() == 1)
        {
            setClosed(ticket_id);
        }
        else
        {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeTicket();
                }
            };
            ((ImageView)findViewById(R.id.imageCross)).setOnClickListener(listener);
            ((TextView)findViewById(R.id.close)).setOnClickListener(listener);
        }

        ((LinearLayout)findViewById(R.id.layoutBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public static void load()
    {
        messages = Api.currentChatMessages;
        checkUsersAvatar();
    }

    private void closeTicket()
    {
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("id", ticket_id);
        mapUserId.put("status", 1);


        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mapUserId);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("data", json);

        AsyncHttpTask closeTicketTask = new AsyncHttpTask(params, AsyncMethodNames.CLOSE_TICKET, this);
        closeTicketTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void setClosed(int ticket_id)
    {
        if(ChatActivity.ticket.getId() == ticket_id) {
            input.setFocusable(false);
            input.setOnLongClickListener(null);
            input.setOnTouchListener(null);
            input.setOnClickListener(null);
            input.setOnFocusChangeListener(null);
            Activity activity = (Activity)input.getContext();

            ((ImageView) activity.findViewById(R.id.imageCross)).setVisibility(View.INVISIBLE);
            ((TextView) activity.findViewById(R.id.close)).setText("Closed");
            ((TextView) activity.findViewById(R.id.close)).setTextColor(activity.getResources().getColor(R.color.colorRed));

            ((ImageView) activity.findViewById(R.id.imageCross)).setOnClickListener(null);
            ((TextView) activity.findViewById(R.id.close)).setOnClickListener(null);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    public static void checkUsersAvatar()
    {
        if(messages == null)
        {
            User user = ticket.getUser();
            if(!isUserContains(user.getId())) {
                SupportChat.users.add(user);
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("user", user);

                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, activity);
                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        else {
            for (Message m : messages) {
                if (!isUserContains(m.getUser().getId())) {
                    User user = m.getUser();
                    SupportChat.users.add(user);

                    HashMap<String, Object> parametrs = new HashMap<String, Object>();
                    parametrs.put("user", user);

                    AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, activity);
                    getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }
        AsyncHttpTask getUserTask = new AsyncHttpTask(null, AsyncMethodNames.WAIT_AVATAR_LOADING, activity);
        getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static boolean isUserContains(int id){
        for(User u: SupportChat.users){
            if(u.getId() == id)
            {
                return true;
            }
        }
        return false;
    }


    public User getUserById(int id)
    {
        for(User u: SupportChat.users){
            if(u.getId() == id)
                return u;
        }
        return null;
    }


    public void setMessages()
    {

        lvMessages = (ListView)findViewById(R.id.list_of_messages);
        MessagesAdapter adapter = new MessagesAdapter(this, this, messages);
        lvMessages.setAdapter(adapter);
        isActive = true;
        lvMessages.setSelection(lvMessages.getCount()-1);
    }

    public static boolean isMessageContains(Message message)
    {
        if(messages == null)
            return false;
        for(Message m: messages)
        {
            if(m.getId() == message.getId())
            {
                return true;
            }
        }
        return false;
    }

    public static void addNewMessages(List<Message> newMessages)
    {
        messages.addAll(newMessages);
        ((MessagesAdapter)lvMessages.getAdapter()).notifyDataSetChanged();
        lvMessages.smoothScrollToPosition(lvMessages.getCount() -1);
    }

    //обработчик нажатия кнопки Send для отправки сообщения
    public void sendMessage(View view) {

        if(ticket != null && ticket.getStatus() == 1)
            return;
        String message = input.getText().toString();
        if(message.contentEquals("")) return;

        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("user_id", Api.user.getId());
        mapUserId.put("ticket_id", ticket_id);
        mapUserId.put("message", message);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mapUserId);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("data", json);

        AsyncHttpTask sendMessageTask = new AsyncHttpTask(params, AsyncMethodNames.SEND_MESSAGE, this);
        sendMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Clear the input
        input.setText("");
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        isActive = false;
        super.onActionModeFinished(mode);
    }

    public void loadGallery(View view) {
        if(ticket != null && ticket.getStatus() == 1)
            return;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        // Получаем изображение из потока в виде байтов
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case REQUEST_CAMERA:

                break;*/
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String encodedData = imageToString(bitmap);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("data:");//
                        stringBuilder.append("image/png");//getMimeType(imageUri));// image/jpeg;base64,");
                        stringBuilder.append(";base64,");
                        stringBuilder.append(encodedData);

                        Attachment attach = new Attachment(stringBuilder.toString(), getMimeType(imageUri));
                        List<Attachment> attachments = new ArrayList<Attachment>();
                        attachments.add(attach);
                        sendAttachment(attachments);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendAttachment(List<Attachment> files)
    {
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("ticket_id", ticket_id);
        mapUserId.put("attachments", files);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mapUserId);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("data", json);

        AsyncHttpTask sendMessageTask = new AsyncHttpTask(params, AsyncMethodNames.SET_ATTACHMENT, this);
        sendMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
}
