/*
- Realize group communication and file transfer based on TCP protocol
- Each client first sends messages and files to the server, then the server will forward it to all current online clients after receiving it.
@author: Yiqing Wang
 */

import java.io.*;
import java.net.Socket;

// 客户端3号
// Client3/CharTypeCnt.java
public class ChatroomClient3 {

    public static void main(String[] args) {
        Socket s = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        BufferedReader br = null;
        PrintStream ps = null;

        File f1 = null;

        String s1 = "";

        try {
            s = new Socket("127.0.0.1", 5555);

            System.out.println("Successfully connected to the server!");

            new ClientSendThread(s).start();

            ps = new PrintStream(s.getOutputStream());
            br =  new BufferedReader(new InputStreamReader(s.getInputStream()));

            bos = new BufferedOutputStream(s.getOutputStream());
            bis = new BufferedInputStream(s.getInputStream());

            while (true) {
                s1 = br.readLine();

                if ("0".equals(s1)) {
                    break;
                }

                if ("1".equals(s1)) {
                    s1 = br.readLine();
                    System.out.println(s1);
                }

                if ("2".equals(s1)) {
                    //
                    System.out.println(br.readLine());
                    String recievedFileName = br.readLine();
                    long recievedFileLength = Long.parseLong(br.readLine());
                    f1 = new File("./Client3/" + recievedFileName);

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
