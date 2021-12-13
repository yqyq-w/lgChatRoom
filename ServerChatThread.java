/*
- Realize group communication and file transfer based on TCP protocol
- Each client first sends messages and files to the server, then the server will forward it to all current online clients after receiving it.
@author: Yiqing Wang
 */

import java.io.*;
import java.net.Socket;


public class ServerChatThread extends Thread  /*implements Runnable*/ {
    private Socket s;
    private int cnt;

    // initialize socket
    public ServerChatThread(Socket s) {
        this.s = s;
        this.cnt = ChatroomServer.socketCnt;
    }

    // Override run()
    @Override
    public void run() {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        BufferedReader br = null;
        PrintStream ps = null;

        File f1 = null;

        try {

            br =  new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());

            bis = new BufferedInputStream(s.getInputStream());
            bos = new BufferedOutputStream(s.getOutputStream());


            while (true) {
                // parse client input
                String str1 = br.readLine();
                String[] sArr = str1.split(";");
//                System.out.println(str1);
//                System.out.println(Arrays.toString(sArr));

                // exit if the client sends "1;bye"
                if ("1;bye".equals(str1)) {
                    // response “0” to indicate client exited
                    ps = new PrintStream(s.getOutputStream());
                    ps.println("0");
                    // remove the exitedthe client from socketList
                    System.out.println(ChatroomServer.socketList);
                    ChatroomServer.socketList.remove(ChatroomServer.socketList.indexOf(s));
                    System.out.println("Client exited the chat!");
                    // inform clients that are still online
                    for (Socket skt : ChatroomServer.socketList) {
                        ps = new PrintStream(skt.getOutputStream());
                        // response “1” to indicate message receiving
                        ps.println("1");
                        ps.println("Client" + cnt + " has exited the chat!");
                    }
                    System.out.println(ChatroomServer.socketList);
                    break;
                // if the client sends "1;.....", send the message to all clients
                } else if ("1".equals(sArr[0])) {

                    for (Socket skt : ChatroomServer.socketList) {
                        ps = new PrintStream(skt.getOutputStream());
                        // response “1” to indicate message receiving
                        ps.println("1");
                        ps.println("Client" + cnt + " says: " + sArr[1]);

                        System.out.println("Informed client" + skt.getInetAddress()
                                + ": Client" + cnt + "says: " +  sArr[1]);
                    }
                // if the client sends "1;.....", get the file from client. then send it to all clients
                } else if ("2".equals(sArr[0])) {
                    // file name & size
                    String recievedFileName = sArr[1];
                    long recievedFileLength = Long.parseLong(br.readLine());
                    System.out.println("File name: " + recievedFileName
                            + ", size is: " + recievedFileLength);
                    // update bos
                    f1 = new File("./Server/" + recievedFileName);
                    bos = new BufferedOutputStream(new FileOutputStream(f1));
                    bis = new BufferedInputStream(s.getInputStream());
                    System.out.println("Ready to receive " + recievedFileName + " from client" + cnt
                            + ", size is: " + recievedFileLength);
                    
                    int res = 0;
                    while (f1.length() < recievedFileLength) {
                        res = bis.read();
                        bos.write(res);
                        bos.flush();
//                        System.out.println("receiving...");
//                           System.out.println(f1.length());
                    }
                    System.out.println("Received" + recievedFileName + "from client" + cnt);

                    // send to all clients online
                    for (Socket skt : ChatroomServer.socketList) {
                        System.out.println(skt);
                        // send file locally from the server
                        bis = new BufferedInputStream(new FileInputStream(f1));
                        ps = new PrintStream(skt.getOutputStream());
                        bos = new BufferedOutputStream(skt.getOutputStream());
                        // response “2” to indicate file receiving
                        ps.println("2");
                        ps.println("Client" + cnt + "uploaded " + recievedFileName + "，size is " + recievedFileLength);
                        ps.println(recievedFileName);
                        ps.println(recievedFileLength);
                        // send file
                        while ((res = bis.read()) != -1) {
                            bos.write(res);
                        }
                        bos.flush();
               
                        System.out.println("File " + f1.getName() + " sent to client" + skt.getInetAddress());
                        ps.println("File " + f1.getName() + " is sent!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            if (null != ps) {
//                ps.close();
//            }
//            if (null != br) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (null != bos){
//                try {
//                    bos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (null != bis){
//                try {
//                    bis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            if (null != s){
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


