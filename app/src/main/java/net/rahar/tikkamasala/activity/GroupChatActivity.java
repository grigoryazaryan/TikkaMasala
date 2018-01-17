package net.rahar.tikkamasala.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.JsonSyntaxException;

import net.rahar.tikkamasala.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.GroupChatMessagesListAdapter;
import helpers.C;
import helpers.Parser;
import helpers.Utils;
import model.AudioRecMessage;
import model.ChatMessage;
import model.TextMessage;
import tcp_client.TCPClient;

/**
 * Created by grigory on 16/01/18.
 */

public class GroupChatActivity extends AppCompatActivity {
    private final String TAG = "GroupChatActivity";
    private final int CODE_RECORD_SOUND = 3202;

    private RecyclerView recyclerViewMessages;
    private GroupChatMessagesListAdapter messagesAdapter;
    private ImageButton buttonSpeak;
    private Button buttonSend;
    private EditText editTextMessage;

    private List<ChatMessage> messagesList;

    private TCPClient tcpClient;

    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_group_chat);
//        EventBus.getDefault().register(this);

        messagesList = new ArrayList<>();

        userName = getIntent().getStringExtra(C.KEY_USERNAME);

        recyclerViewMessages = (RecyclerView) findViewById(R.id.recycler_view_messages_list);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new GroupChatMessagesListAdapter(messagesList);
        recyclerViewMessages.setAdapter(messagesAdapter);

        buttonSend = (Button) findViewById(R.id.button_send);
        buttonSpeak = (ImageButton) findViewById(R.id.button_speak);
        editTextMessage = (EditText) findViewById(R.id.text_message);

        tcpClient = new TCPClient()
                .setListener(message -> {
//                    Log.v(TAG, "tcp onmsg: " + message);
                    try {
                        ChatMessage msg = Parser.parseChatMessageFromGson(message);
                        onMessageReceived(msg);
                    } catch (JsonSyntaxException e) {
                        TextMessage tm = new TextMessage(message);
                        tm.setSender("anonymous");
                        onMessageReceived(tm);
                    }
                }).connect();


        buttonSend.setOnClickListener(v -> {
            String msg = editTextMessage.getText().toString();
            if (!TextUtils.isEmpty(msg)) {
                TextMessage m = new TextMessage(msg);
                sendMessage(m);
            }
            editTextMessage.setText("");
        });

        buttonSpeak.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, CODE_RECORD_SOUND);
        });

    }

    private void sendMessage(ChatMessage message) {
        message.setSender(userName);
        tcpClient.sendMessage(Parser.stringifyJsonChatMessage(message));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_RECORD_SOUND && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
//            MediaPlayer mediaPlayer = MediaPlayer.create(GroupChatActivity.this, uri);
//            mediaPlayer.start();
//            if(true) return;
            try {
                sendMessage(new AudioRecMessage(Utils.encodeFileToBase64Binary(uri, GroupChatActivity.this)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onMessageReceived(ChatMessage chatMessage) {
        messagesAdapter.addItem(chatMessage);
        recyclerViewMessages.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);
    }

//    @Subscribe
//    public void onMessage(EventBusMessage eventBusMessage) {
//        Log.v(TAG, eventBusMessage.getMessage().toString());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tcpClient.disconnect();
//        EventBus.getDefault().unregister(this);
    }
}
