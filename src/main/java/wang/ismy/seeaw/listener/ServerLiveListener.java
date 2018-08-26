package wang.ismy.seeaw.listener;

import wang.ismy.seeaw.Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServerLiveListener extends Thread{

    private Server server;

    public HashMap<Socket, Date> getLiveSockets() {
        return liveSockets;
    }

    private HashMap<Socket,Date> liveSockets =new HashMap<Socket, Date>();

    public ServerLiveListener(Server server){
        this.server=server;
    }
    @Override
    public void run(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true){
            List<Socket> removeSockets=new ArrayList<Socket>();
            for(Socket i: server.getClientList()){
                long time;
                if(liveSockets.get(i)==null){
                    time=0;
                }else{
                    time=liveSockets.get(i).getTime();
                }

                if(System.currentTimeMillis()-time>10000){
                    System.err.println(i+"存活超时，被移除");
                    removeSockets.add(i);
                }
            }

            server.getClientList().removeAll(removeSockets);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
