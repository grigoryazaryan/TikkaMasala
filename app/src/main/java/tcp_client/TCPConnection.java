package tcp_client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by grigory on 16/01/18.
 */

public class TCPConnection {
    private final String TAG = "TCPConnection";

    private String SERVER_HOST = "tikkamasala.rahar.net";
    private int SERVER_PORT = 31337;


    private BufferedWriter out;
    private BufferedReader in;
    private Socket socket;

    private TCPConnectionListener listener;

    public TCPConnection(TCPConnectionListener listener) {
        this.listener = listener;
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //here may be sent connection status "Connected" to listener

            while (true) {
                String msg = in.readLine();
                if (msg != null) {
                    Log.v(TAG, "received: " + msg);
                    listener.onSocketMessage(msg);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // free up resources
        }
    }

    public void disconnect() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TCPConnection writeToSocket(String msg) {
        try {
            out.write(msg);
            out.flush();
        } catch (Exception e) {
            Log.v(TAG, "error");
            e.printStackTrace();
        }
        return this;
    }

    public interface TCPConnectionListener{
        void onSocketMessage(String message);
    }
}
