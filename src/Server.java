import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


public class Server {

    ServerSocket server;
    Socket socket;

    // for Reading Input streem
    BufferedReader br;
    // for output streem  (_---- sends to the client)..
    PrintWriter out;



    //Constructor
    public Server() throws IOException {
        try {
            server = new ServerSocket(7777);
            System.out.println("server is ready to accept the connection");
            System.out.println("waiting... ");
            socket=server.accept();


            /*getInputStream will return th

            the bit data which is came from the getInputStream() is converted in to charecter
            and BufferedReader will creat the buffer object

             */

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            /*

             */

            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // We have to read and write simultaneously, so we use the concept of multithreading
    private void startReading() {

        // this thread read the data

        Runnable r1=()->{

            System.out.println("reader started");

            while (true) {
                String msg;
                try {
                    msg = br.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (msg.equals("exit")) {
                    System.out.println("Clint terminated the chat");
                    break;
                }

                System.out.println(" Client :- "+msg);
            }

        };
        new Thread(r1).start();
    }

    private void startWriting() {

        System.out.println("writer is started");

        Runnable r2=()->{

            while (true) {

                try {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }

        };

        new Thread(r2).start();

    }


    public static void main(String[] args) throws IOException {

        System.out.println("Server is created");
        Server server1=new Server();
    }
}