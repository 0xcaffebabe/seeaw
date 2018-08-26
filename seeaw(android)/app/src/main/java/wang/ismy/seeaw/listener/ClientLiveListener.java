package wang.ismy.seeaw.listener;

import wang.ismy.seeaw.Client;
import wang.ismy.seeaw.DTO;

import java.io.IOException;
import java.util.HashMap;

public class ClientLiveListener extends Thread {

    private Client client;

    public ClientLiveListener(Client client){
        this.client=client;
    }
        @Override
        public void run(){
            DTO dto=new DTO();
            dto.setMsg("heart");
            dto.setParams(new HashMap<String, String>());
            while(true){

                try {

                    client.send(dto.toString());
                    Thread.sleep(2000);
                } catch (IOException e) {
                   System.err.println("连接出现异常，重新连接");
                   client.init();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

}
