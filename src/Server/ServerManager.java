package Server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ServerManager extends Thread {

    private int port = 13337;
    private ArrayList<Question> questions;
    private UserManager userManager;
    private Socket echoClientSocket;
    ArrayList<ServerManager> client_list;
    Connection connection;  // FIXME: 4/14/2018
    private JTextArea messages;

    private PrintWriter output;
    private BufferedReader input;
    public static Map<String, Integer> map = new HashMap<String, Integer>();
    public static Map<String, Integer> map2 = new HashMap<String, Integer>();
    private ArrayList<String> highScores = new ArrayList<>();
    private ArrayList<String> highScores2 = new ArrayList<>();


    public ServerManager(Socket clientSocket, Connection connection, ArrayList client_list, JTextArea messages) {
        this.echoClientSocket = clientSocket;
        this.client_list = client_list;
        this.connection = connection;
        this.messages = messages;
    }


    public void run() {

        try {

            output = new PrintWriter(echoClientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(echoClientSocket.getInputStream()));
            userManager = new UserManager(output, input);


            readInputs(input, output);

        } catch (IOException e) {
            messages.setText(messages.getText() + "\nProblem regarding I/O! - " + e.toString());
            System.out.println("Problem regarding I/O! - " + e.toString());
        }
    }

    /***
     * This method is on a loop for reading a clients input
     * Loads up the question text file to be passed into the game mode chosen.
     * Listens for the client's username and selected game mode.
     */
    public synchronized void readInputs(BufferedReader in, PrintWriter out) throws IOException {
        questions = Question.readQuestions("C:\\Users\\adeku\\Desktop\\project\\Java-swing-app-master\\AstrologyQuiz.txt");

        String username = in.readLine(); // GET USERNAME
        userManager.setUsername(username);
        messages.setText(messages.getText() + "\nUsername: " + username);
        System.out.println("Username: " + username);

        String userInput = in.readLine();
        switch (userInput) {
            case "GAME:PM":   // PM = Practice Mode
                messages.setText(messages.getText() + "\n" + username + " : Playing Practice Mode");
                practiceMode(in, out, userInput);
                break;

            case "GM:TM":  // TM = Tournament Mode
                client_list.add(this);
                messages.setText(messages.getText() + "\n" + username + " : Playing Tournament Mode");
                tournamentMode(in, out, userInput);
                break;

            case "GM:FM": // PM = Friendly Mode
                messages.setText(messages.getText() + "\n" + username + " : Playing Friendly Mode");
                friendlyMode(in, out, userInput);
                break;

            default:
                messages.setText(messages.getText() + "\n" + username + " : Playing Practice Mode");
                practiceMode(in, out, userInput);
                break;
        }


        client_list.remove(this);
        connection.active_connections--;
        for (ServerManager sm : client_list) {
            System.out.println(sm);
        }
        out.close();
        in.close();
    }

    /***
     * Send a message to a client instead of retyping output.println
     * @param message
     */
    public synchronized void sendToClient(String message) {
        output.println(message);
        output.flush();
    }

    /***
     * Sends a message to all clients connected at the same time.
     * Can be used to alert users at client side if someone scores a point
     * @param message
     */
    public synchronized void sendToAllClients(String message) {
        for (int index = 0; index < connection.client_list.size(); index++) {
            ServerManager sh = connection.client_list.get(index);
            sh.output.println(message);
        }

    }

    /***
     * This method sends the winner at the end of the game in tournament mode
     * @return
     */
    private synchronized String getWinner() {
        String winner = null;
        int highest_score = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            highScores.add(entry.getKey() + ":" + entry.getValue());
            if (entry.getValue() > highest_score) {
                winner = entry.getKey();
                highest_score = entry.getValue();
            }
        }
        output.println("SCORES:" + highScores);
        return winner;
    }

    /**
     * This method returns the questions to the client as an Array
     * [Question, answer,answer,answer,answer]
     */
    public ArrayList<String> readQuestions(Question q) {

        ArrayList<String> QnA = new ArrayList<>();
        QnA.add(q.getQuestionText()); // Questions
        for (int i = 0; i < 4; i++) {
            QnA.add(q.getOptions().get(i));   // Gets the options
        }

        return QnA;
    }

    /***
     * This method is responsible for tournament mode. It doesn't startServer until there's more than one
     * client connected.
     */
    private synchronized void tournamentMode(BufferedReader in, PrintWriter out, String userInput) {
        try {
            connection.active_connections++;

            System.out.println(connection.active_connections);
            while (connection.active_connections < 2) {
                out.println("WAITING FOR CONNECTION");  // SEND THIS TO CLIENT SO CLIENT WAITS
            }
            messages.setText(messages.getText() + "\nSTARTING GAME");
            out.println("STARTING GAME");

            boolean play = true;
            while (play) {
                for (int i = 0; i < questions.size(); i++) {
                    System.out.println("ServerManager " + readQuestions(questions.get(i)));
                    out.println(readQuestions(questions.get(i))); // Send the qestions and answers together.
                    messages.setText(messages.getText() + "\nServerManager" + readQuestions(questions.get(i)));
                    out.println("QUESTION:" + i); // Send question to client to keep track

                    /** If answer is right then send to client to increase score and alert all users connected
                     that's on the same question*/
                    if (in.readLine().equals(questions.get(i).getAnswer())) {
                        userManager.increaseScore();

                        out.println("score:" + userManager.getScore()); // Return score to client
                        sendToAllClients(("correct:" + userManager.getUsername() + ":" + i)); // ALERT ALL CLIENTS CONNECTED
                        messages.setText(messages.getText() + "\ncorrect:" + userManager.getUsername() + ":" + i);
                        System.out.println("correct:" + userManager.getUsername() + ":" + i);

                    } else {
                        messages.setText(messages.getText() + "\nIncorrect");

                        System.out.println("Incorrect"); //TODO Maybe send to client?
                    }
                }

                map.put(userManager.getUsername(), userManager.getScore());


                sendToAllClients("END:" + getWinner()); // Send the signal to end the game along with winners username


                messages.setText(messages.getText() + "\nSCORES:" + highScores);
                System.out.println("SCORES:" + highScores);

                highScores.remove(userManager.getUsername() + ":" + userManager.getScore());
                userManager.resetScore();

                break;

            }


        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private synchronized void friendlyMode(BufferedReader in, PrintWriter out, String userInput) {
        try {
            client_list.add(this);

            boolean play = true;
            while (play) {
                for (int i = 0; i < questions.size(); i++) {
                    System.out.println("ServerManager " + readQuestions(questions.get(i)));
                    out.println(readQuestions(questions.get(i))); // Send the qestions and answers together.
                    out.println("QUESTION:" + i); // Send question to client to keep track

                    /** If answer is right then send to client to increase score and alert all users connected
                     that's on the same question*/
                    if (in.readLine().equals(questions.get(i).getAnswer())) {
                        userManager.increaseScore();

                        out.println("score:" + userManager.getScore()); // Return score to client
                        System.out.println("correct:" + userManager.getUsername() + ":" + i);

                    } else {
                        System.out.println("Incorrect"); //TODO Maybe send to client?
                    }
                }

                map2.put(userManager.getUsername(), userManager.getScore());

                for (Map.Entry<String, Integer> entry : map2.entrySet()) {
                    highScores2.add(entry.getKey() + ":" + entry.getValue());
                }

                out.println("SCORES:" + highScores2);
                messages.setText(messages.getText() + "\nSCORES:" + highScores2);
                System.out.println("SCORES:" + highScores2);


                highScores2.remove(userManager.getUsername() + ":" + userManager.getScore());
                userManager.resetScore();
            }


        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Practice mode of the server
     */
    private void practiceMode(BufferedReader in, PrintWriter out, String userInput) {
        try {
            while ((!userInput.equals("Bye."))) {

                for (int i = 0; i < questions.size(); i++) {
                    messages.setText(messages.getText() + "\nServerManager " + readQuestions(questions.get(i)));
                    System.out.println("ServerManager " + readQuestions(questions.get(i)));
                    out.println(readQuestions(questions.get(i))); // Send the qestions and answers together.

                    // Right answer
                    if (in.readLine().equals(questions.get(i).getAnswer())) {
                        messages.setText(messages.getText() + "\nCorrect");
                        userManager.increaseScore();
                        out.println("score:" + userManager.getScore()); // Return score to client
                    } else {
                        System.out.println("Incorrect");
                        messages.setText(messages.getText() + "\nIncorrect");
                    }

                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Closes all connection.
     */

}
