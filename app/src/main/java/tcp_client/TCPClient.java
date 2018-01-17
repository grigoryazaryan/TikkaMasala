package tcp_client;

import android.os.AsyncTask;

/**
 * Created by grigory on 16/01/18.
 */

public class TCPClient {

    private TCPConnection connection;
    private TCPClientListener tcpClientListener;
    private AsyncTask<String, String, String> taskRunner;

    public TCPClient() {
        taskRunner = new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {
                connection = new TCPConnection(message -> publishProgress(message));
                connection.connect();
                return null;
            }


            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (tcpClientListener != null)
                    tcpClientListener.onMessage(values[0]);
            }
        };
    }

    public TCPClient setListener(TCPClientListener listener) {
        tcpClientListener = listener;
        return this;
    }

    public void sendMessage(String chatMessage) {
        if (connection != null) connection.writeToSocket(chatMessage);
    }

//    public void sendAudioRec(AudioRecMessage audioRecMessage) {
//        if (connection != null) connection.writeToSocket(audioRecMessage);
//    }

    public TCPClient connect() {
        taskRunner.execute();
        return this;
    }

    public void disconnect() {
        if (connection != null) connection.disconnect();
        taskRunner.cancel(true);
    }


    public interface TCPClientListener {
        /**
         * will be called on UI thread
         *
         * @param textMessage message object
         */
        void onMessage(String textMessage);
        default void onConnect(){}
    }

}
