package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Keno on 4/11/2018.
 */
public class Client {
    //Connection Details
    private String IP = "127.0.0.1";
    private int port = 13337;

    //Connection Socket
    Socket socket = null;

    //Connection Reader & Writer
    private BufferedReader sIn = null;
    private PrintWriter sOut = null;


    private String errorMessage = null;

    public void connect() {
        try {
            socket = new Socket(IP, port);
            if (socket.isConnected()){

                sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                sOut = new PrintWriter(socket.getOutputStream(), true);

                System.out.println("Welcome to the echo Client ServerManager service, if you wish to exit, please enter 'Bye.' ");
                System.out.println("Note, entering nothing will result in a response of nothing!");

            }else {
                System.exit(0);
            }

        } catch (UnknownHostException e) {
            System.out.println("Sorry! There was a problem with finding the host! - " + e.toString());
            errorMessage = "Sorry! There was a problem with finding the host server! - " + e.toString();
            System.out.println("Closing Program");
        } catch (IOException e) {
            System.out.println("Sorry! There was a problem regarding I/O! - " + e.toString());
            errorMessage = "Sorry! There was a problem regarding I/O! - " + e.toString();
            System.out.println("Closing Program");
        }
    }

    public synchronized void sendToServer(String userReply) {

                sOut.println(userReply);
                System.out.println("User Reply is: " + userReply);
                sOut.flush();

        }


    public String readFromServer() {

        String response = null;
        try {
            response = sIn.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
            closeConnection();
        }
        return response;
    }

    public void closeConnection() {
        try {
            socket.close();
            sIn.close();
            sOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
