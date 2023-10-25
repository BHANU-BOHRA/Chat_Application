import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Server extends JFrame
{
    private ServerSocket server;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    private final JLabel head = new JLabel("Server");
    private final JTextArea chat = new JTextArea();
    private final JTextField msg = new JTextField();
    private final Font font = new Font("Times new Roman", Font.PLAIN, 20);
    private final JScrollPane scrollBar = new JScrollPane(chat);

    Server()
    {
        try
        {
            server = new ServerSocket(5000);
            System.out.println("\nStarting the Server..");
            System.out.println("Waiting for Client to Connect...");
            client = server.accept();

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream());

            createGUI();
            keyHandler();

            startReading();
        }
        catch (Exception e)
        {
            System.out.println("\nSomething Went Wrong...\n");
            System.exit(2);
        }
    }

    void createGUI()
    {
        setTitle("Server Side");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        head.setFont(new Font("Time new Roman", Font.BOLD, 20));
        head.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        head.setIcon(new ImageIcon("../img/chat.png"));
        head.setHorizontalTextPosition(SwingConstants.CENTER);
        head.setVerticalTextPosition(SwingConstants.BOTTOM);
        head.setHorizontalAlignment(SwingConstants.CENTER);

        chat.setFont(font);
        chat.setEditable(false);

        msg.setFont(font);
        msg.requestFocus();

        setLayout(new BorderLayout());
        add(head, BorderLayout.NORTH);
        add(scrollBar);
        add(msg, BorderLayout.SOUTH);

        setVisible(true);
    }

    void keyHandler()
    {
        class KHandle implements KeyListener
        {
            public void keyTyped(KeyEvent e)
            {
            }

            public void keyPressed(KeyEvent e)
            {
            }

            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == 10 && !msg.getText().isBlank())
                {
                    String content = msg.getText();
                    chat.append("Me : " + content + "\n");
                    chat.setCaretPosition(chat.getDocument().getLength());
                    out.println(content);
                    out.flush();
                    msg.setText("");
                    msg.requestFocus();
                }
            }
        }

        msg.addKeyListener(new KHandle());
    }

    void showAlert()
    {
        try
        {
            JOptionPane.showMessageDialog(Server.this, "Client Terminated the Chat");
            client.close();
            System.exit(0);
        }
        catch (Exception e)
        {
            System.out.println("\nSomething Went Wrong...");
            System.exit(1);
        }
    }


    void startReading()
    {
        class Read implements Runnable
        {
            public void run()
            {
                System.out.println("\nReader has Started");
                try
                {
                    while (true)
                    {
                        String msg = in.readLine();
                        if (msg == null || msg.equalsIgnoreCase("quit")) showAlert();

                        chat.append("Client : " + msg + "\n");
                        chat.setCaretPosition(chat.getDocument().getLength());
                    }
                }
                catch (Exception e)
                {
                    showAlert();
                    System.exit(1);
                }
            }
        }

        new Thread(new Read()).start();
    }


    public static void main(String[] args)
    {
        new Server();
    }
}
