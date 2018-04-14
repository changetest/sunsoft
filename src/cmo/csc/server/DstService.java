package cmo.csc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2018/4/14.
 */
public class DstService {
    public static void main(String[] args) {
        try {
            //启动服务监听端口
            ServerSocket ss = new ServerSocket(30000);
            //没有连接这个方法就一直阻塞
            Socket s = ss.accept();
            new Thread(new DstServiceImpl(s)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
