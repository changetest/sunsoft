package cmo.csc.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2018/4/14.
 */
public class DstServiceImpl implements Runnable {
    Socket socket = null;
    public DstServiceImpl(Socket s) {

        this.socket = s;
    }

    @Override
    public void run() {
                try {

        while (true){
        int index = 1;
            //5秒后中断连接
            if(index>10){

                    socket.close();
                    System.out.println("服务器已经关闭连接！！");
                    break;
                }
                index++;
                Thread.sleep(1*1000);
            }
                } catch (Exception e) {
                    e.printStackTrace();
        }
    }
}
