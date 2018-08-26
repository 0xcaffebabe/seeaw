package wang.ismy.seeaw;

import wang.ismy.seeaw.listener.ClientListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client {

    private Socket socket;

    private String host;

    private int port;
    public Client(String host,int port) {
        this.host=host;
        this.port=port;
        init();
    }

    public void init(){
        try {
            socket=new Socket(host,port);

            new ClientListener(this);

        } catch (IOException e) {
            System.err.println("初始化连接失败，五秒后重试");
            try {
                Thread.sleep(5000);
                init();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public abstract void receive(String msg);

    public void send(String msg) throws IOException {
        System.err.println(msg);
        PrintWriter writer=new PrintWriter(socket.getOutputStream());
        msg=msg.replaceAll("\n","&@");
        writer.println(msg);
        writer.flush();
    }

    public Socket getSocket(){
        return socket;
    }

}
