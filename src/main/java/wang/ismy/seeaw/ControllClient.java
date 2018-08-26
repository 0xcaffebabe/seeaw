package wang.ismy.seeaw;

import wang.ismy.seeaw.listener.ClientLiveListener;

public class ControllClient extends Client {

    public ControllClient(String host, int port) {
        super(host, port);

        new ClientLiveListener(this).start();
    }

    public void receive(String msg) {

        System.err.println("控制端收到了一条消息:"+msg);
    }
}
