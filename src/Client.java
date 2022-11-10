import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket;

    BufferedReader br;
    // for output streem  (_---- sends to the client)..
    PrintWriter out;


    public  Client(){

        try{
            System.out.println("Sending request to the server");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connection established");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();


        }catch (Exception e1){
            e1.printStackTrace();

        }
    }
    private void startReading() {

        // this thread read the data

        Runnable r1=()->{

            System.out.println("reader started");
            try {

                while (true) {
                  String  msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println(" server :- " + msg);
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
