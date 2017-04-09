/*
 * Peer to Peer chat room for CS 465
 * Created by Hayden Aupperle, Andy Salazar, Colter Hooker
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * GUI class
 * set the jframe for the gui layout
 */
public class GUI {
    BufferedReader in;
    PrintWriter out;
    PrintWriter join;
    JFrame guiFrame = new JFrame("Cool Dude Peer to Peer Chat");
    JTextField text = new JTextField(40);
    JTextArea ChatBox = new JTextArea(8, 40);
    public static String ip;
    public static Socket socket;

    /*
     * GUI enable, clear and set the frames to
     */
    public GUI() {

        //GUI
        text.setEditable(false);
        ChatBox.setEditable(false);
        guiFrame.getContentPane().add(text, "North");
        guiFrame.getContentPane().add(new JScrollPane(ChatBox), "Center");
        guiFrame.pack();

        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(text.getText());
                text.setText("");
            }
        });
    }
    // jframe to ask the user to enter a ip to connect to
    public String getServerAddress() {
        return JOptionPane.showInputDialog(guiFrame,"Enter An IP Address: ");
      }
    // Next frame to ask the user to input a user name
    public String getName() {
        return JOptionPane.showInputDialog(guiFrame,"Enter A User Name");
      }
    /*
     * run the GUI
     * Check to see if we are taking any input from he user
     *
     */
    public void run() throws IOException {
        if(socket == null)
        System.out.println("Socket is open");
        String serverAddress = getServerAddress();
        socket = new Socket(serverAddress, 6969);
        if(socket != null){
        System.out.println("Socket is closed");

        }
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            if (line.startsWith("username")) {
                out.println(getName());
            } else if (line.startsWith("acceptusername")) {
                text.setEditable(true);
                out.println("Has joined the chat:");
            } else if (line.startsWith("message")) {
                CheckSystemMessage(line);
                ChatBox.append(line.substring(8) + "\n");
            }
        }
    }
    /*
     *
     */
    public int CheckSystemMessage(String line){
      char check = 'z';
      int i = 8;
      while(check != ':'){
          check = line.charAt(i);
          i++;
      }
      i+=1;
      if(line.charAt(i) == '/'){
        if(line.substring(i+1).startsWith("left"))
          System.exit(0);
        else
          //out.println("Has left the chat. \n");
          ChatBox.append("User Left Chat \n");
          //out.println("Has left the chat. \n");
        }
        return 1;
    }
    /*
     * Run the
     */
    public static void main(String[] args) throws Exception {
        GUI client = new GUI();
          try {
              ip = InetAddress.getLocalHost().getHostAddress();
              System.out.println("Your current IP address : " + ip);
          } catch (UnknownHostException e) {
              e.printStackTrace();
          }
        client.guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.guiFrame.setVisible(true);
        client.run();
    }
}
