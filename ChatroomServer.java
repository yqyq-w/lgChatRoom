/*
- Realize group communication and file transfer based on TCP protocol
- Each client first sends messages and files to the server, then the server will forward it to all current online clients after receiving it.
@author: Yiqing Wang
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatroomServer {

    // socketList stores current online clients
    static ArrayList<Socket> socketList = new ArrayList<Socket>();
    static int socketCnt = 0;

    public static void main(String[] args) {
        ServerSocket ss = null;
        Socket s = null;

        BufferedReader br = null;
        PrintStream ps = null;

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        File f1 = null;

        try {
            ss = new ServerSocket(5555);

            while (true) {
                System.out.println("Waiting for the client's connection request......");
                s = ss.accept();
                // update the socketList
                socketList.add(s);
                System.out.println(socketList);
                System.out.println("Client" + ++socketCnt + "successfully connected!");
                // start server thread
                new ServerChatThread(s).start();          
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != s){
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != ss){
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
