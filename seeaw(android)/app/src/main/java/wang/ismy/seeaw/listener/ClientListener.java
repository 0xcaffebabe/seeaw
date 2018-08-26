package wang.ismy.seeaw.listener;



import wang.ismy.seeaw.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//该类的作用是负责监听客户端的消息，如果服务器发来消息
//会调其内部类Listener的run方法，run方法会从输入流中读取一行数据，后将消息传递给receive方法处理


public class ClientListener {

    private Client client;


    private boolean flag=true;
    public ClientListener(Client client) {
        this.client=client;

        new Listener().start();
    }

    class Listener extends Thread{

        @Override
        public void run(){

            try {
                BufferedReader reader=new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
                PrintWriter writer=new PrintWriter(client.getSocket().getOutputStream());
                while(flag){
                    String msg=null;
                    if((msg=reader.readLine())!=null){
                        client.receive(msg);
                    }
                }
            } catch (IOException e) {
                System.err.println("连接出现异常，五秒后重连");
                try {
                    Thread.sleep(5000);
                    client.init();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

}
