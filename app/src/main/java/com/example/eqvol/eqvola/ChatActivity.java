package com.example.eqvol.eqvola;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.ActionMode;
import android.view.MenuItem;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText input = null;
    public static int ticket_id;
    public int last_message_id;
    public static List<Message> messages;
    public static ListView lvMessages = null;
    public static boolean isActive = false;

    private static final int REQUEST_GALLERY = 2;

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
        setTitle(messages.get(0).getTicket().getTitle());

        if(ticket_id == -1 || last_message_id == -1) finish();

        checkUsersAvatar();

    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        /*Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivityForResult(myIntent, 0);*/
        return true;
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
        //lvMessages.setSelection(lvMessages.getCount()-1);
        lvMessages.smoothScrollToPosition(lvMessages.getCount() -1);
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

    public void loadGallery(View view) {
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
