import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        try {
            int port = 5200;
            ServerSocket ss = new ServerSocket(port);
            DataInputStream i = null;
            DataOutputStream o = null;

            System.out.println("Wait for client on port " + ss.getLocalPort() + "...");
            Socket server = ss.accept();
            i = new DataInputStream(new BufferedInputStream(server.getInputStream()));
            o = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
            HashMap<String, String> dict = new HashMap<String, String>();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            while (true) {
                String fromClient = i.readUTF();

                String choice[] = fromClient.replace('=', ' ').split("\\s+");
                switch (choice[0].toLowerCase()) {

                    case "get":
                        if (dict.containsKey(choice[1])) {
                            o.writeUTF(dict.get(choice[1]));
                            o.flush();
                        } else {
                            o.writeUTF("Please give the correct key name in get command as given during PUT. It is case sensitive...");
                            o.flush();
                        }
                        break;
                    case "put":
                        dict.put(choice[1], choice[2]);
                        o.writeUTF(" ");
                        o.flush();
                        break;
                    case "dump":
                        o.writeUTF(String.join(",", dict.keySet()));
                        o.flush();
                        break;
                    case "exit":
                        System.out.println("You are exiting now and this session will close");
                        ss.close();
                        i.close();
                        o.close();
                        break;
                    default:
                        o.writeUTF("Please enter the correct command");
                        o.flush();
                }
            }
        } catch (IOException ex) {
            System.out.println("Session closed ");
        }
    }
}