package wang.ismy.seeaw;



import wang.ismy.seeaw.listener.ServerLiveListener;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyServer extends Server {

   private ServerLiveListener serverLiveListener=new ServerLiveListener(this);

    private Socket mainConSocket=null;
    public MyServer(int port) {
        super(port);
        serverLiveListener.start();
    }

    public void receive(Socket socket, String msg) {


        DTO dto=Utils.msgToDTO(msg);
        dto.addParams("sources",socket.getInetAddress().getHostAddress());

        if(dto.getMsg().equals("heart")){
            serverLiveListener.getLiveSockets().put(socket,new Date());
        }else{
            if(dto.getMsg().equals("broadcast") && socket==mainConSocket){
                broadcast(dto.getParams().get("cmd"));

            }else if(dto.getMsg().equals("list")&&socket==mainConSocket){

                try {
                    send(socket,getClientList().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if(dto.getMsg().equals("bind")) {

                if(dto.getParams().get("name").equals("715711877")){
                    mainConSocket=socket;
                }

            }else{

                if(mainConSocket==null){

                }else{
                    try {
                        send(mainConSocket,dto.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.err.println("服务器收到了来自"+socket+"的消息:"+dto);
            }

        }
    }

    public Socket getMainConSocket() {
        return mainConSocket;
    }


}
