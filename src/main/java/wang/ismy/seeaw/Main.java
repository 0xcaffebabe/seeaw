package wang.ismy.seeaw;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
//        Server server=new MyServer(1999);

        Client myClient=new MyClient("www.ismy.wang",1999);


//        Client client=new ControllClient("www.ismy.wang",1999);
//
//        DTO dto=new DTO();
//
//        dto.setMsg("bind").addParams("name","715711877");
//
//        client.send(dto.toString());
//
//        dto.setMsg("broadcast").addParams("cmd","powershell");
//
//        client.send(dto.toString());




    }
}
