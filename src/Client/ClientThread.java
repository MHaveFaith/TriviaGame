package Client;


import javax.swing.*;

/**
 * Created by Keno on 4/12/2018.
 */
public class ClientThread extends Thread {

    //GUI components
    private javax.swing.JRadioButton ansA;
    private javax.swing.JRadioButton ansB;
    private javax.swing.JRadioButton ansC;
    private javax.swing.JRadioButton ansD;
    private javax.swing.JRadioButton ansF;
    private javax.swing.JTextPane messageBox;
    private javax.swing.JLabel questionLabel;
    private javax.swing.JLabel scoreCounter;

    Client client;
    private final String username;
    private int question = 0;


    public ClientThread(Client client, String username, JRadioButton ansA, JRadioButton ansB, JRadioButton ansC,
                        JRadioButton ansD, JRadioButton ansF, JTextPane messageBox, JLabel questionLabel, JLabel scoreCounter) {

        this.client = client;
        this.username = username;
        this.ansA = ansA;
        this.ansB = ansB;
        this.ansC = ansC;
        this.ansD = ansD;
        this.ansF = ansF;
        this.messageBox = messageBox;
        this.questionLabel = questionLabel;
        this.scoreCounter = scoreCounter;
    }

    public void run() {
        while (true) {

            String serverResponse = null;

            if ((serverResponse = client.readFromServer()) != null) {

                System.out.println("Server Response: " + serverResponse);

                if (serverResponse.startsWith("[")) {
                    //Handling user reply
                    handleQnA(serverResponse);
                }
                if (serverResponse.contains("score:")) {
                    //Handling score responses
                    handleScore(serverResponse);

                }

                if(serverResponse.startsWith("QUESTION:")) { // Track question
                    String[] split = serverResponse.split(":");
                    question = Integer.parseInt(split[1]);
                    System.out.println("QUESTION" + question);
                }

                if(serverResponse.startsWith("SCORES:")) { // Track question
                    finalScore(serverResponse);
                }

                if (serverResponse.startsWith("correct:")) {
                    String[] split = serverResponse.split(":");
                    setMessages (split[1] + " Right Answer.");

                    if (split[1].equals(username)) {

                    } else if (Integer.parseInt(split[2]) == question) {
                        client.sendToServer("null");
                      //  handleQnA(client.readFromServer());
                    }
                }
                if (serverResponse.startsWith("WINNER:")) {
                    String[] split = serverResponse.split(":");
                    setMessages(split[1] + " Won the Game.");
                    //   client.sendToServer("IWON:"+username);
                }

                if (serverResponse.startsWith("WAITING")) {
                    setMessages(serverResponse);
                    boolean start = true;
                    while (start) {
                        String newResponse = client.readFromServer();
                        if (newResponse.startsWith("STARTING")) {
                            start = false;
                        }
                    }

                }
                if (serverResponse.startsWith("END:")) {

                    String split[] = serverResponse.split(":");
                    if (username.equals(split[1])){
                        JOptionPane.showMessageDialog(null, split[1] + " Congrats You won,\nYou can Now close the client", "Game Over: You Won!", JOptionPane.INFORMATION_MESSAGE);
                    }else if (!username.equals(split[1])){
                        JOptionPane.showMessageDialog(null, split[1] + " Won this Match,\nPlease play Practice mode to Practice,\nYou can Now close the client", "Game Over: You Lost!", JOptionPane.INFORMATION_MESSAGE);
                    }
                    setMessages("Game Over....Getting Final Scores");
                    setMessages("\nUser: Score");
                }
                try {
                    Thread.sleep(1);
                } catch (Exception e) {

                }

            }else {
                client.closeConnection();
            }

        }
    }

    private void handleQnA(String serverMessage) {

        String replace1 = serverMessage.replace("[", "");
        String replace2 = replace1.replace("]", "");

        String[] split = replace2.split(",");


        questionLabel.setText(split[0].trim());

        ansA.setText(split[1].trim());
        ansB.setText(split[2].trim());
        ansC.setText(split[3].trim());
        ansD.setText(split[4].trim());
        ansF.setText("I don't know".trim());
    }

    private void handleScore(String serverMessage) {
        String[] split = serverMessage.split(":");
        scoreCounter.setText(split[1].trim());
    }

    private void setMessages(String messages) {
        messageBox.setText(messageBox.getText() + messages + "\n");
    }

    private void finalScore(String message) {
        String dsiplayScore = null;
        //Replace the braces "[]" with ""
        String replace1 = message.replace("SCORES:", "");
        String replace2 = replace1.replace("]", "");
        String replace3 = replace2.replace("[", "");

        //Split the string as with ","
        String[] splitComma = replace3.split(",");

        //Display the username's and score's
        setMessages("Final Score:");
        for (String s : splitComma) {
            String[] split = s.split(":");
            setMessages(split[0] + ": " + split[1]);
            dsiplayScore += split[0] + ": " + split[1] + "\n"; //Future use.
        }

    }
}
