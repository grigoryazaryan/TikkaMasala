package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rahar.tikkamasala.R;

import java.util.List;

import model.Message;

/**
 * Created by grigory on 16/01/18.
 */

public class GroupChatMessagesListAdapter extends RecyclerView.Adapter<GroupChatMessagesListAdapter.ViewHolder> {

    private List<Message> dataSet;


    public GroupChatMessagesListAdapter(List<Message> data){
        dataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(dataSet.get(position).getMessage());
    }

    public void addItem(Message m){
        dataSet.add(m);
        notifyItemInserted(dataSet.size() - 1);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sender, message;

        public ViewHolder(View v) {
            super(v);
            message = (TextView) v.findViewById(R.id.text);
        }
    }
}
