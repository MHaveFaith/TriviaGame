package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Created by Keno on 4/12/2018.
 * This class will be used to manage all the connected clients -
 * Manages the scores so far.
 * TODO Name to be added
 *
 */
public class UserManager {

    private PrintWriter out;
    private BufferedReader in;
    private int score;
    private String username;

    public UserManager(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;

    }

    public void increaseScore() {
        score++;
    }

    public int getScore() { // Maybe change this to send directly to client eventually.
        return score;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void resetScore() {
        score = 0;
    }
}
