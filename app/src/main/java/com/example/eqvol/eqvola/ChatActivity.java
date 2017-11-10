package com.example.eqvol.eqvola;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.ActionMode;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;

import com.example.eqvol.eqvola.Adapters.MessagesAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Attachment;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText input = null;
    public static int ticket_id;
    public int last_message_id;
    //public List<User> users;
    public static List<Message> messages;
    public static ListView lvMessages = null;
    public static boolean isActive = false;

    private static final int REQUEST_GALLERY = 2;

    //public Map<Integer, Bitmap> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        input = (EditText)findViewById(R.id.chat_message_text);

        Intent intent = getIntent();
        ticket_id = intent.getIntExtra("ticket_id", 0);
        last_message_id = intent.getIntExtra("last_message_id", 0);

        SupportChat.images = new HashMap<Integer, Bitmap>();

        messages = Api.currentChatMessages;

        if(ticket_id == 0 || last_message_id == 0) finish();

        SupportChat.users = new ArrayList<User>();
        SupportChat.users.add(Api.user);

        checkUsersAvatar();
    }

    public void checkUsersAvatar()
    {
        for(Message m: messages){
            if(!isUserContains(m.getUser().getId())){
                User user = m.getUser();
                SupportChat.users.add(user);

                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("user", user);

                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, this);
                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        AsyncHttpTask getUserTask = new AsyncHttpTask(null, AsyncMethodNames.WAIT_AVATAR_LOADING, this);
        getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isUserContains(int id){
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

    public static void addNewMessages(List<Message> newMessages)
    {
        messages.addAll(newMessages);
        ((MessagesAdapter)lvMessages.getAdapter()).notifyDataSetChanged();
    }

    //обработчик нажатия кнопки Attach file для отправки сообщения
    public void attachFile(View view) {

    }
    //обработчик нажатия кнопки Send для отправки сообщения
    public void sendMessage(View view) {

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
    ///

    public void loadGallery(View view) {
        Intent intent = new Intent();//Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);// ошибка в этой строке
        //(пробовал менять на context.startActivityForResult(intent, REQUEST_GALLERY) но подсвечивает изначально как ошибка)
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case REQUEST_CAMERA:

                break;*/
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        try {


                            //TODO contentresolver android
                            ContentResolver contentResolver = getContentResolver();
                            InputStream istream = contentResolver.openInputStream(imageUri);
                            byte[] fileData = getBytes(istream);

                            /////////////////////////
                            String str123 = imageUri.toString();

                            istream.read(fileData, 0, fileData.length);
                            istream.close();
                            String encodedData = Base64.encodeToString(fileData, Base64.DEFAULT);

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("data:");//
                            stringBuilder.append(getMimeType(imageUri));// image/jpeg;base64,");
                            stringBuilder.append(";base64,");
                            stringBuilder.append(encodedData);

                            Attachment attach = new Attachment(encodedData, getMimeType(imageUri));
                            List<Attachment> attachments = new ArrayList<Attachment>();
                            attachments.add(attach);
                            sendAttachment(attachments);

                            /*Map<String, byte[]> attachment = new HashMap<String, byte[]>();
                            attachment.put("attachment", fileData);//Base64.encodeToString(fileData, Base64.DEFAULT));
                            attachment.put("type", data.getType());*/
                            //List<Map<String, byte[]>> files = new ArrayList<Map<String, byte[]>>();
                            //files.add(attachment);
                            //sendAttachment(attachments);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.getMessage();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
