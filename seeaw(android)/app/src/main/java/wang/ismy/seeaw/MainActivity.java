package wang.ismy.seeaw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {


    private EditText editText;

    private Button button;

    private TextView textView;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private ControllClient controllClient;

    private Bitmap bitmap;
    private ImageView imageView;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {

            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                controllClient = new ControllClient("www.ismy.wang", 1999);
                DTO dto = new DTO();
                dto.setMsg("bind").addParams("name", "715711877");
                try {
                    controllClient.send(dto.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
         imageView=new ZZoomImageView(this);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        ((LinearLayout)findViewById(R.id.imageLinerlayout)).addView(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DTO dto = new DTO();
                            dto.setMsg("broadcast").addParams("cmd", editText.getText().toString());
                            controllClient.send(dto.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });



    }



    class ControllClient extends Client {

        public ControllClient(String host, int port) {
            super(host, port);
        }

        @Override
        public void receive(final String s) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            final JSONObject jsonObject = new JSONObject(new String(s.getBytes(), "utf8"));

                            if (jsonObject.optString("msg").equals("screen")) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            final Bitmap bitmap=getData(jsonObject.getJSONObject("params").optString("ret"));

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    imageView.setImageBitmap(bitmap);

                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        } catch (JSONException e) {

                        }
                        textView.setText(new String(s.getBytes(), "utf8").replaceAll("&@", "\n"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public Bitmap getData(String path) {

        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmap;

    }


}