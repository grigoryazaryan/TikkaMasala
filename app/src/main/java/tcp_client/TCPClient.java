package tcp_client;

import android.os.AsyncTask;

import model.Message;

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
                connection = new TCPConnection(new TCPConnection.TCPConnectionListener() {
                    @Override
                    public void onSocketMessage(String message) {
                        //may do some stuff like parse message and share it to listeners
                        //EventBus.getDefault().post(new EventBusMessage(new Message(message)));
                        publishProgress(message);
                    }
                });
                connection.connect();
                return null;
            }


            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (tcpClientListener != null)
                    tcpClientListener.onMessage(new Message(values[0]));
            }
        };
    }

    public TCPClient setListener(TCPClientListener listener) {
        tcpClientListener = listener;
        return this;
    }

    public void sendMessage(String message) {
        if (connection != null) connection.writeToSocket(message);
    }

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
         * @param message message object
         */
        void onMessage(Message message);
        default void onConnect(){}
    }

}
