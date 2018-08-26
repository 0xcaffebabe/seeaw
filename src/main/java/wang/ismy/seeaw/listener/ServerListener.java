package wang.ismy.seeaw.listener;

import wang.ismy.seeaw.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {

    private Server server;

    private boolean flag=true;
    public ServerListener(Server server){
        this.server=server;
        new Listener().start();
    }

    public void stop(){
        flag=false;
    }

    class Listener extends Thread{

        @Override
        public void run(){

            while(flag){

                try {
                    final Socket socket=server.getServerSocket().accept();
                    server.getClientList().add(socket);
                    if(socket!=null){
                        new Thread(new Runnable() {
                            public void run() {

                                try {
                                    BufferedReader reader=
                                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                    String content=null;
                                    while((content=reader.readLine())!=null){
                                        server.receive(socket,content);
                                    }

                                } catch (IOException e) {
                                   System.err.println(socket+"断开");
                                }

                            }
                        }).start();
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
