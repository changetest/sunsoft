package cn.itcast.bigdata.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/15.
 */
public class TimeClientHandler  implements Runnable{
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean  stop ;

    public TimeClientHandler(String host,int port) {
        this.host = host == null  ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {

//        System.out.println("jin dao run fangfa le ....");
        try{

            doConnect();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        while(!stop){

            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                SelectionKey key = null;
                while(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e){
                        if( key != null ){
                            key.cancel();
                            if( key.channel() != null){
                                key.channel().close();
                            }
                        }
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void doConnect() throws IOException {
//        System.out.println("go into  doConnect()1");
        if(socketChannel.connect(new InetSocketAddress(host,port))) {
//            System.out.println("go into  doConnect()2");
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);

        } else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void handleInput(SelectionKey key) throws IOException{
//        System.out.println("go into handleInput()");
        if(key.isValid()){
            SocketChannel sc = (SocketChannel) key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0 ){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"utf-8");
                    System.out.println(" Now is :" + body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                } else {

                }

            }
        }
    }

    private void doWrite(SocketChannel sc) throws IOException{
//        System.out.println("doWrite()");
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
//        System.out.println("req");
        if( !writeBuffer.hasRemaining() ){
//            System.out.println("进来了没呢");
            System.out.println("Send order 2 server succed ");
        }
    }

}
