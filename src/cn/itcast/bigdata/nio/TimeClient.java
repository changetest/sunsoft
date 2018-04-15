package cn.itcast.bigdata.nio;

/**
 * Created by Administrator on 2018/4/15.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if( args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e){

            }
        }
        TimeClientHandler timeClientHandler = new TimeClientHandler("127.0.0.1", port);
        new Thread(timeClientHandler,"Time-Client -1").start();

//        new Thread(new TimeClientHandler("localhost",port)).start();
    }
}
