package wang.ismy.seeaw;

import wang.ismy.seeaw.listener.ServerListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class Server {

    private List<Socket> clientList=new ArrayList<Socket>();

    private ServerSocket serverSocket;

    private ServerListener serverListener;

    private int port;
    public Server(int port) {
        this.port=port;
        init();
    }

    public void init(){
        try {
            serverSocket=new ServerSocket(1999);

            System.err.println(this.getClass().getName()+"正在监听1999");
            serverListener=new ServerListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void receive(Socket socket,String msg);

    public void send(Socket socket,String msg) throws IOException {
        PrintWriter writer=new PrintWriter(socket.getOutputStream());
        msg=msg.replaceAll("\n","&@");
        writer.println(msg);
        writer.flush();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<Socket> getClientList() {
        return clientList;
    }

    public void broadcast(String msg){

        for(Socket i:clientList){
            if(i==getMainConSocket()){
                continue;
            }
            try {
                send(i,msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract Socket getMainConSocket();
}
