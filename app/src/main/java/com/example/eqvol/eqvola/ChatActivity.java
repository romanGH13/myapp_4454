package com.example.eqvol.eqvola;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.eqvol.eqvola.Adapters.MessagesAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Message;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.chat_message_text);

                //TODO добавить отправку сообщения
                // Clear the input
                input.setText("");
            }
        });

        setMessages();

    }

    private void setMessages()
    {
        List<Message> messages = Api.currentChatMessages;
        ListView lvMessages = (ListView)findViewById(R.id.list_of_messages);
        MessagesAdapter adapter = new MessagesAdapter(this, messages);
        lvMessages.setAdapter(adapter);
    }
}
