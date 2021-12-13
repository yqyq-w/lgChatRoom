/*
- Realize group communication and file transfer based on TCP protocol
- Each client first sends messages and files to the server, then the server will forward it to all current online clients after receiving it.
@author: Yiqing Wang
 */

import java.io.*;
import java.net.Socket;

// client downloads the file from the server
public class ClientDownload {
    // bis, bos as I/O stream
    public static BufferedOutputStream bos = null;
    public static BufferedInputStream bis = null;

    // 关闭流
    public static void closeStream() {
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
    }

    // download the file on the server with the socket, file, file name and file size
    public static void getFile(Socket s, File f1, String name, long fileLength) {

        try {
             bos = new BufferedOutputStream(new FileOutputStream(f1));
             bis = new BufferedInputStream(s.getInputStream());

            System.out.println("Ready to download " + name
                    + "from server, size is " + fileLength);
            int res = 0;
            while (f1.length() < fileLength) {
                res = bis.read();
                bos.write(res);
                bos.flush();
//                        System.out.println("还在下载...");
            }
            System.out.println("Downloaded " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
