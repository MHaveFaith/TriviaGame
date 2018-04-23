package Server;

/**
 * Created by Keno on 4/12/2018.
 */
public class StartServerGUI {

    public static void main(String[] args){

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               new StartServerGUI().startServer();
            }
        });

    }


    private void startServer(){
        ServerGUI serverGUI = new ServerGUI();

    }
}
