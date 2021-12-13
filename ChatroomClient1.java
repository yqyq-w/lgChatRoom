/*
- Realize group communication and file transfer based on TCP protocol
- Each client first sends messages and files to the server, then the server will forward it to all current online clients after receiving it.
@author: Yiqing Wang
 */

import java.io.*;
import java.net.Socket;

// Client1
public class ChatroomClient1 {

    public static void main(String[] args) {
        Socket s = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        BufferedReader br = null;
        PrintStream ps = null;

        File f1 = null;

        String s1 = "";

        try {
            // connect to sever
            s = new Socket("127.0.0.1", 5555);
            System.out.println("Successfully connected to the server!");
            // start the thread in charge of sending
            new ClientSendThread(s).start();

            ps = new PrintStream(s.getOutputStream());
            br =  new BufferedReader(new InputStreamReader(s.getInputStream()));

            bos = new BufferedOutputStream(s.getOutputStream());
            bis = new BufferedInputStream(s.getInputStream());

            while (true) {
                // get the command from server
                s1 = br.readLine();
                // "0" indicates this client will exit
                if ("0".equals(s1)) {
                    break;
                }
                // “1” indicates some client sent a message, prepare to reveive message
                if ("1".equals(s1)) {
                    s1 = br.readLine();
                    System.out.println(s1);
                }
                // “2” indicates some client sent a file, prepare to reveive file
                if ("2".equals(s1)) {
                    // first get the corresponding file name and size
                    System.out.println(br.readLine());
                    String recievedFileName = br.readLine();
//                    System.out.println(recievedFileName);
                    long recievedFileLength = Long.parseLong(br.readLine());
                    f1 = new File("./Client1/" + recievedFileName);
                    // call ClientDownload.getFile() to download the file
                    ClientDownload.getFile(s, f1, recievedFileName, recievedFileLength);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            scnr.close();
            if (null != ps) {
                ps.close();
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bos){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bis){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
