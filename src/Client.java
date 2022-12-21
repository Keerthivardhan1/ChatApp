package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.Font;

public class Client extends JFrame{

    Socket socket;

    BufferedReader br;
    // for output streem  (_---- sends to the client)..
    PrintWriter out;

    //components

    private JLabel heading = new JLabel("Client");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);




    public  Client(){

        try{
            System.out.println("Sending request to the server");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connection established");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());
             createGUI();
             handelEvents();
            startReading();
//            startWriting();
//

        }catch (Exception e1){
            e1.printStackTrace();

        }
    }

    private void handelEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
              //  System.out.println("key released is" + e.getKeyCode());

                if(e.getKeyCode() == 10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("You : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }

    private void createGUI() {
        this.setTitle("Client");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Components

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
    }

    //start reading
    private void startReading() {

        // this thread read the data

        Runnable r1=()->{

            System.out.println("reader started");
            try {

                while (true) {
                  String  msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this , "Server Terminated" );
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                   // System.out.println(" server :- " + msg);
                    messageArea.append(" Server : " + msg + "\n");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };
        new Thread(r1).start();
    }

    private void startWriting() {

        System.out.println("writer is started");

        Runnable r2=()->{
            try {
                while (true) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                    out.println(content);
                    out.flush();

                }

            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }

        };

        new Thread(r2).start();

    }


    public static void main(String[] args) {

        Client client1=new Client();

    }
}
