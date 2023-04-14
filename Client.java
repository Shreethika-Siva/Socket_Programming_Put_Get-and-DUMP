import java.io.*;
import java.net.*;
import java.util.*;


public class Client {
    public static void main(String[] args) throws IOException {
        int serverPort = 5200;
        try {

            System.out.println("Connecting to server on port " + serverPort);
            Socket socket = new Socket("localhost", serverPort);
            DataInputStream i = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream o = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            Scanner sc = new Scanner(System.in);
            HashMap<String, String> cache_data = new HashMap<String, String>();
            while (true) {
                System.out.println("SELECT EITHER OF \n ONE:PUT GET \n DUMP \n EXIT TO QUIT");
                String in = sc.nextLine();
                String[] user_input = in.replace('=', ' ').split("\\s+");
                if (user_input[0].equalsIgnoreCase("exit")) {
                    o.writeUTF(user_input[0]);
                    System.out.println("The sockets will close. Session ends here...");
                    socket.close();
                    i.close();
                    o.close();
                    break;
                }
                if (user_input[0].equalsIgnoreCase("get") && cache_data.containsKey(user_input[1])) {
                    System.out.println("Output(From Proxy Server):" + cache_data.get(user_input[1]));
                } else if (user_input[0].equalsIgnoreCase("DUMP")) {
                    o.writeUTF(in);
                    System.out.println("***Request forwarded to server***");
                    o.flush();
                    System.out.println("\n\nOutput:" + i.readUTF());
                    System.out.println("\n****************\n\n");
                } else {
                    o.writeUTF(in);
                    System.out.println("***Request forwarded to server***");
                    o.flush();
                    String fromServer = i.readUTF();
                    if (user_input[0].equalsIgnoreCase("get")) {
                        cache_data.put(user_input[1], fromServer);
                    }
                    System.out.println("Output:" + fromServer);
                }
                if (user_input[0].equalsIgnoreCase("put")) cache_data.remove(user_input[1]);
            }
        } catch (SocketException e) {
            System.out.println("Sockets are completely closed");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
