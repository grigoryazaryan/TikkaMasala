package adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.rahar.tikkamasala.R;

import java.util.List;

import model.AudioRecMessage;
import model.ChatMessage;
import model.TextMessage;

/**
 * Created by grigory on 16/01/18.
 */

public class GroupChatMessagesListAdapter extends RecyclerView.Adapter<GroupChatMessagesListAdapter.ViewHolder> {

    private List<ChatMessage> dataSet;
    private Context context;

    public GroupChatMessagesListAdapter(List<ChatMessage> data) {
        this.dataSet = data;
//        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case ChatMessage.TYPE_TEXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text, parent, false);
                return new ViewHolderText(v);
            case ChatMessage.TYPE_AUDIO_REC:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_audio_rec, parent, false);
                return new ViewHolderAudioRec(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindType(dataSet.get(position));
    }

    public void addItem(ChatMessage m) {
        dataSet.add(m);
        notifyItemInserted(dataSet.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        TextView sender, time;

        public ViewHolder(View v) {
            super(v);
            sender = (TextView) v.findViewById(R.id.sender);
        }

        public void bindType(ChatMessage item) {
            sender.setText(item.getSender());
        }
    }

    public static class ViewHolderText extends ViewHolder {
        private final TextView message;

        public ViewHolderText(View v) {
            super(v);
            message = (TextView) v.findViewById(R.id.text);
        }

        public void bindType(ChatMessage item) {
            super.bindType(item);
            message.setText(((TextMessage) item).getMessage());
        }
    }

    public static class ViewHolderAudioRec extends ViewHolder {
        private final ImageButton audioRec;

        public ViewHolderAudioRec(View v) {
            super(v);
            audioRec = (ImageButton) v.findViewById(R.id.audio_rec);
        }

        public void bindType(ChatMessage item) {
            super.bindType(item);
            audioRec.setOnClickListener(v -> {
                try {
                    String url = "data:audio/mp3;base64," + ((AudioRecMessage) item).getDataBase64();
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                }
            });
        }
    }
}
