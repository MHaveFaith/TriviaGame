package Client;

public class ClientGUI {
    public static void main(String args[]) {
        new ClientGUI().launch();
    }

    private void launch(){
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetUsername().setVisible(true);
            }
        });
    }
}

