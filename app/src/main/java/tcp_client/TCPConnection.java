package tcp_client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Message;

/**
 * Created by grigory on 16/01/18.
 */

public class TCPConnection {
    private final String TAG = "TCPConnection";

    private String SERVER_HOST = "tikkamasala.rahar.net";
    private int SERVER_PORT = 31337;

    private final long RECONNECT_INTERVAL_MILLIS = 1000;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;

    private TCPConnectionListener listener;

    public TCPConnection(TCPConnectionListener listener) {
        this.listener = listener;
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            connectionState = ConnectionState.CONNECTED;
            listener.onConnectionStateChange(connectionState);
            while (connectionState == ConnectionState.CONNECTED) {
                String msg = in.readLine();
                if (msg != null) {
                    Log.v(TAG, "received: " + msg);
                    listener.onSocketMessage(msg);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
//            reconnect();
        } catch (IOException e) {
            e.printStackTrace();
//            reconnect();
        }
    }

    public void reconnect() {
        disconnect();
        connectionState = ConnectionState.RECONNECTING;
        listener.onConnectionStateChange(connectionState);
        try {
            Thread.sleep(RECONNECT_INTERVAL_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connect();
    }

    private void freeOutResources(){
        try {
        out.flush();
        out.close();
        in.close();
        socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
            connectionState = ConnectionState.DISCONNECTED;
            Log.v(TAG, "disco");
            listener.onConnectionStateChange(connectionState);

    }

    public TCPConnection writeToSocket(Message msg) {
        if (out != null && !out.checkError()) {
            out.println(msg);
            out.flush();
        }
        return this;
    }

    public interface TCPConnectionListener {
        String TAG = "TCPConnectionListener";

        void onSocketMessage(String message);

        default void onConnectionStateChange(ConnectionState state) {
            Log.v(TAG, "state changed -> " + state);
        }
    }

    public enum ConnectionState {
        DISCONNECTED, CONNECTED, RECONNECTING
//        ConnectionState(String s) {
//            name();
//        }
    }
}
