package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Keno on 4/12/2018.
 */
public class Connection implements Runnable {

    private int port = 13337;
    private Thread thread;

    ArrayList<ServerManager> client_list = new ArrayList<>();
    static int active_connections = 0;
    private JTextArea messages;
    ServerManager serverManager;
    ServerSocket echoSocket = null;
    Socket echoClientSocket = null;

    public Connection(JTextArea messages){
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            echoSocket = new ServerSocket(port);
            messages.setText(messages.getText() + "\nWaiting for connection");
            System.out.println("Waiting for connection");

            while (true) {

                echoClientSocket = echoSocket.accept();

                System.out.println(active_connections);
                messages.setText(messages.getText() + "\nConnection with" + echoClientSocket.getInetAddress() +  "has been successful");
                System.out.println("Connection with " + echoClientSocket.getInetAddress() + "  has been successful");
                System.out.println("Waiting for client input(s)");
                messages.setText(messages.getText() + "\nWaiting for client input(s)");
                serverManager = new ServerManager(echoClientSocket, this, client_list, messages);
                serverManager.start();
            }


        } catch (IOException io) {
            System.out.println("Error");
            messages.setText(messages.getText() + "\nError");
        }
    }

    void startServer() {
        thread = new Thread(this);
        thread.start();
    }



}
