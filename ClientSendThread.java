/*
- Realize group communication and file transfer based on TCP protocol
- Each client first sends messages and files to the server, then the server will forward it to all current online clients after receiving it.
@author: Yiqing Wang
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

// mthread for client(s) to send information or files
public class ClientSendThread extends Thread {
    private Socket s;

    // initialize Socket
    public ClientSendThread(Socket s) {
        this.s = s;
    }

    // Override run()
    @Override
    public void run() {

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        BufferedReader br = null;
        PrintStream ps = null;
        Scanner scnr = new Scanner(System.in);

        File f1 = null;

        try {
            // initialize I/O streams
            ps = new PrintStream(s.getOutputStream());
            br =  new BufferedReader(new InputStreamReader(s.getInputStream()));

            bos = new BufferedOutputStream(s.getOutputStream());
            bis = new BufferedInputStream(s.getInputStream());

            while (true) {
                                                            // or use regx
                System.out.println("==============\nPlease enter: \n1;if you want to send text\n2;if you want to send file path\n==============");
                String s1 = scnr.next();
                // send the entire content entered by the client to the server
                ps.println(s1);

                // the case of sending text
                if (s1.startsWith("1")) {
                    // if the input is "bye", exit the chat
                    if ("bye".equalsIgnoreCase(s1.split(";")[1])) {
                        break;
                    }
                    this.sleep(500);
                }
                // the case of sending file
                if (s1.startsWith("2")) {
                    // create file acc. to file name
                    String fileName = s1.split(";")[1];
//                    System.out.println("file name: " + fileName);
                    f1 = new File(fileName);
//                    ps.println(fileName);
                    // send file size to server
                    long fileLength = f1.length();
                    ps.println(fileLength);
                    // update bis, read the local file and send it
                    bis = new BufferedInputStream(new FileInputStream(f1));
                    // bos = new BufferedOutputStream(s.getOutputStream());

                    int res = 0;
                    while ((res = bis.read()) != -1) {
                        bos.write(res);
                    }
                    bos.flush();
                    System.out.println("File " + fileName + " has been sent!");

                    this.sleep(500);
                }

            }
        } catch (IOException | InterruptedException  e) {
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
