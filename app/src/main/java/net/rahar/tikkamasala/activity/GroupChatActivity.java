package net.rahar.tikkamasala.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import net.rahar.tikkamasala.R;

import java.util.ArrayList;
import java.util.List;

import adapter.GroupChatMessagesListAdapter;
import helpers.C;
import model.Message;
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

    private List<Message> messagesList;

    private TCPClient tcpClient;

    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_group_chat);
//        EventBus.getDefault().register(this);

        messagesList = new ArrayList<>();

        recyclerViewMessages = (RecyclerView) findViewById(R.id.recycler_view_messages_list);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new GroupChatMessagesListAdapter(messagesList);
        recyclerViewMessages.setAdapter(messagesAdapter);

        buttonSend = (Button) findViewById(R.id.button_send);
        buttonSpeak = (ImageButton) findViewById(R.id.button_speak);
        editTextMessage = (EditText) findViewById(R.id.text_message);


        tcpClient = new TCPClient()
                .setListener(message -> {
                    Log.v(TAG, "tcp onmsg: " + message);
                    onMessageReceived(message);
                }).connect();

        userName = getIntent().getStringExtra(C.KEY_USERNAME);


        buttonSend.setOnClickListener(v -> {
            String msg = editTextMessage.getText().toString();
            if (!TextUtils.isEmpty(msg)){
                Message m = new Message(msg);
                m.sender = userName;
                tcpClient.sendMessage(m);
            }
            editTextMessage.setText("");
        });

        buttonSpeak.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, CODE_RECORD_SOUND);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, resultCode + ", " + data.getData());
        if (requestCode == CODE_RECORD_SOUND && resultCode == Activity.RESULT_OK) {

        }
    }

    private void onMessageReceived(Message message) {
        messagesAdapter.addItem(message);
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
