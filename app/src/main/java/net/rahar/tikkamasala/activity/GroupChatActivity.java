package net.rahar.tikkamasala.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import net.rahar.tikkamasala.R;

import java.util.ArrayList;
import java.util.List;

import adapter.GroupChatMessagesListAdapter;
import helpers.C;
import helpers.RandomString;
import model.Message;
import tcp_client.TCPClient;

/**
 * Created by grigory on 16/01/18.
 */

public class GroupChatActivity extends AppCompatActivity {
    private final String TAG = "GroupChatActivity";

    private RecyclerView recyclerViewMessages;
    private GroupChatMessagesListAdapter messagesAdapter;
    private ImageButton buttonSpeak;
    private Button buttonSend;
    private EditText editTextMessage;

    private List<Message> messagesList;

    private TCPClient tcpClient;

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
                .setListener(new TCPClient.TCPClientListener() {
                    @Override
                    public void onMessage(Message message) {
                        Log.v(TAG, "tcp onmsg: " + message);
                        onMessage(message);
                    }
                }).connect();

        ((TextView) findViewById(R.id.text_username)).setText(getIntent().getStringExtra(C.KEY_USERNAME));

        buttonSend.setOnClickListener(v -> {
            String msg = editTextMessage.getText().toString();
            if (!TextUtils.isEmpty(msg))
                tcpClient.sendMessage(msg);
        });

        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
//                EventBus.getDefault().post(new EventBusMessage(new Message("hehehhey")));
                String generatedString = new RandomString().nextString();
                Log.v(TAG, generatedString);
                onMessage(new Message(generatedString));
                h.postDelayed(this, 600);
            }
        });

    }

    private void onMessage(Message message) {
        messagesList.add(message);
        Log.v(TAG, ", " + message.getMessage() + ", " + messagesList.size());
//        messagesAdapter.notifyItemInserted(messagesList.size() - 1);
//        messagesAdapter.addItem(message);
        messagesAdapter.notifyDataSetChanged();
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
