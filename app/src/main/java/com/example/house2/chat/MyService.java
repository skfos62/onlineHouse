package com.example.house2.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.house2.R;
import com.example.house2.login_real;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.house2.chat.partner_chatt_view.ReAdapter;
import static com.example.house2.chat.partner_chatt_view.dataList;
import static com.example.house2.chat.partner_chatt_view.realDsataList;
import static com.example.house2.chat.partner_chatt_view.recyclerView;
import static com.example.house2.chat.partner_chatt_view.roomId;
import static com.example.house2.chat.partner_chatt_view.serVicemsg;
import static com.example.house2.login_real.loginIdInfoNick;
import static com.example.house2.mypage.mypage.staticUser;

public class MyService extends Service {
    // 소켓 선언
    static Socket socket;
    Context context;


    // 노티피케이션 관련한 선언
    Notification Notigi;
    NotificationManager Notifi_M;
    ServiceThread thread;
    String  channelId;




    // 서비스 생성자
    static MyService myService;
    public MyService() {
        context = this;
    }

    private final IBinder mBinder = new BindServiceBinder();
    private ICallback mCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // declare callback function
    // 콜백 함수 선언
    public interface ICallback {
        public void remoteCall();
    }
    // for registration in activity
    // 활동 등록
    public void registerCallback(ICallback cb){
        mCallback = cb;
    }

    // service contents 
    // 서비스 내용
    // 서비스로 메세지 보내는곳
    public void myServiceFunc(){
        // 채팅 액티비티랑 bind 되어있는 메소드!
        // 여기서 chat액티비티에 보낼 내용을 작성하면되는듯.
        // 서버접속해서 듣는중

        Log.d("BindService","called by Activity 0");
        Intent intent2 = new Intent();
//        intent2.putExtra("extra", "hello");
//        intent2.setAction("com.my.app");
//        sendBroadcast(intent2);
        /**
         * <채팅 방나누기>
         *     1. 어레이리스트에 방이름이랑 들어온 유저 이름을 저장한다.
         *          방넘버, 유저이름
         *     </채팅>
         */
        new Thread(){
            @Override
            public void run(){
                //new Socket(ip, port);
                try {
                    InputStream is = new ByteArrayInputStream(serVicemsg.getBytes());
                    // read it with BufferedReader
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(is));
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedReader keyboard = new BufferedReader(br);
                    Log.i("버퍼확인 service 2", "run: " + keyboard);
                    // 처음에 소켓 연결했을때 입장한 사람과 메세지를 클라이언트로 전송해준다.
                    PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    // printStram 즉 프린트만 하는 메소드
                    pw2.println(serVicemsg);
                    Log.i("pw 확인하기 service 2", "run: " + pw2.toString());
//                flush()는 현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다. (JSP)
                    pw2.flush();


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

//        //    메인 ui 바꿔주는 쓰레드
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                partner_chatt_view.handler.sendEmptyMessage(1);
//            }
//        });
//        thread.start();

        // call callback in Activity 
        // 액티비티에서 사용하는 콜백함수 부르기
        mCallback.remoteCall();

    }
    // Declare inner class 
    // 이너 클래스 소환
    public class BindServiceBinder extends Binder {
        MyService getService(){
            return MyService.this;
        // return current service 
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("소켓연결확인", "onCreate: 소켓연결되니?");
        connectSocket connect = new connectSocket();
        thread = new ServiceThread(connect);
        thread.start();
    }

    // 여기서 서비스가 돌고있는 동안 할일을 지정해주면된다!
    // 백그라운드에서 실행되는 동작들이 들어가는곳
    // 시스템이 이 메서드를 호출하는 것은 또 다른 구성 요소(예: 액티비티)가 서비스를 시작하도록 요청하는 경우입니다.
    //  <여기서 소켓을 연결하고 해당하는 tcp 서버를 통해 들어오는 내용을 액티비티로 보내주어야함!>
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        // 소켓서버 연결 시작 ------------------------------------------------------------------------
//        Log.i("소켓연결확인", "onStartCommand: 소켓연결되니?");
//        connectSocket connect = new connectSocket();
//        thread = new ServiceThread(connect);
//        thread.start();
//
        // 메세지가 오는지 확인하는중 - - - - - - - - - - - -
        try {
            BufferedReader msgReder = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputThread2 it = new InputThread2(socket,msgReder);
            it.start();
            Log.i("메세지 확인완료 ", "메세지 확인완료 ");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);

//        // 소켓서버 연결 끝 ------------------------------------------------------------------------
//        return START_STICKY;
    }
    // 노티피케이션을 띄우는 핸들러 --------------------------------------------------------------------
    // 서비스 핸들러
    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            channelId = "channerID";
            Intent intent = new Intent(MyService.this, partner_chatt_view.class);
            // 펜딩인텐트란?  기본 목적은 다른 애플리케이션(다른 프로세스)의 권한을 허가하여 가지고 있는 Intent를 마치 본인 앱의 프로세스에서 실행하는 것처럼 사용하게 하는 것
            // 노티피케이션이나 위젯, 알람 등을 사용할때 사용하는 인텐트
            // 인텐트가 가지고있는 기본클래스중에 하나!
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            // 노티피케이션을 bulider(건축업자, 작성기) 하는곳
            // 노티피케이션을 어떻게 쓸껀지 정하는곳 같음
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyService.this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(777, builder.build());
            Toast.makeText(MyService.this, "뜨나유~?", Toast.LENGTH_SHORT).show();

        }
    }
    // 노티피케이션을 띄우는 핸들러 --------------------------------------------------------------------


    // 서버 소켓 연결 쓰레드 시작 ---------------------------------------------------------------------
    // 소켓연결하는 쓰레드 만들기 (찐쓰레드)
    static class connectSocket implements Runnable {
        @Override
        public void run() {
            try {
                //here you must put your computer's IP address.
                socket = new Socket("10.0.2.2", 10001);//아아디,포트
                // 이게 아마 아이디
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                // 이게 아마 내용 입력하는거
//                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                BufferedReader keyboard = new BufferedReader(br2);
                Log.i("버퍼확인 service", "run: " + pw);
                // Id = 지금 로그인한 유저의 아이디
                pw.println(login_real.realIDck);
                pw.flush();

                // 메세지가 오는지 확인하는중 - - - - - - - - - - - -
                try {

                    BufferedReader msgReder = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    InputThread2 it = new InputThread2(socket,msgReder);
                    it.start();
                    Log.i("메세지 확인완료 ", "메세지 확인완료 ");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {

                Log.e("TCP", "C: Error", e);

            }

        }
    }
    // 서버 소켓 연결 쓰레드 끝  ---------------------------------------------------------------------

    // 메세지 출력하는 클래스
    // 서버에서 무언가 오면 inputThread로 출력한다! -------------------------------------------------------
    static class InputThread2 extends Thread{

        private static final String TAG = "서비스내부";
        private Socket sock = null;
        private BufferedReader br = null;
        private Context context;

        public InputThread2(Socket sock,BufferedReader br) {
            this.sock = sock;
            this.br = br;
        }
        public synchronized void run() {
            synchronized (this) {
                if (br != null) {
                    try {
                        String line ;
//                        Log.i("InputThread2(서비스)>>", "run: InputThread 쓰레드 넣기전 브로드캐스트 : " + line);
                        Log.i("InputThread2(서비스)>>", "run: InputThread 쓰레드 넣기전 브로드캐스트 br : " + br.readLine());
                        while ((line = br.readLine()) != null) {
                            int i = 0;
                            Log.i("InputThread2(서비스)>>", "run: InputThread 0 : " + line);
                            // 입장하셨습니다. 라는게 뜨게 되면 데이터리스트 확인하기
                           if (line.contains(":")) {
                                String[] arr = line.split(":");
//                                for (String string : arr) {
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-1 (string확인)" + string);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-2 (arr확인)" + arr[0]);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-3 (arr확인)" + arr[1]);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-4 (arr확인)" + arr[2]);
////                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-5 (arr확인)" + arr[3]);
////                            System.out.println(string);
//                                }
                               if( arr.length != 3 ) {
                                   if (arr[0].equals(login_real.realIDck)) {
                                       // arr[0] : 방번호
                                       // arr[1] : 대화를 요청할 사람의 아이디
                                       // arr[2] : 내용
                                       dataList.add(new DataItem(arr[3], arr[0], Code.ViewType.RIGHT_CONTENT, arr[1]));
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 1 : " + dataList.get(i).getContent());
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 1-1 : " + dataList.get(i).getName());
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 1-3 (arr확인)" + arr[0]);
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 1-4 (arr확인)" + arr[1]);
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 1 (int확인) " + i);
                                       //    메인 ui 바꿔주는 쓰레드

                                       Thread thread = new Thread(new Runnable() {
                                           @Override
                                           public void run() {
                                               partner_chatt_view.handler.sendEmptyMessage(1);
                                               Log.i("ui변경 핸들러(서비스)>>", "run: InputThread 1 (int확인) ");
                                           }
                                       });
                                       thread.start();
                                       i++;

                                   } else {
                                       dataList.add(new DataItem(arr[3], arr[0], Code.ViewType.LEFT_CONTENT, arr[1]));
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 2 : " + dataList.get(i).getContent());
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 2-1 : " + dataList.get(i).getName());
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 2-2 (arr확인)" + arr[0]);
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 2-3 (arr확인)" + arr[1]);
                                       Log.i("InputThread2(서비스)>>", "run: InputThread 2 (int확인) " + i);

                                       //    메인 ui 바꿔주는 쓰레드
                                       Thread thread = new Thread(new Runnable() {
                                           @Override
                                           public void run() {
                                               partner_chatt_view.handler.sendEmptyMessage(1);
                                               Log.i("ui변경 핸들러(서비스)>>", "run: InputThread 2 (int확인) ");
                                           }
                                       });
                                       thread.start();
                                       i++;

                                   }
                               } else {

                               }
                            }
                        }


//
//                        while ((line = br.readLine()) != null) {
//                            int i = 0;
//                            Log.i("InputThread2(서비스)>>", "run: InputThread 0 : " + line);
//                            // 입장하셨습니다. 라는게 뜨게 되면 데이터리스트 확인하기
//                            if (line.contains("입장하셨습니다")) {
//                                dataList.add(new DataItem(line, null, Code.ViewType.CENTER_CONTENT,line));
//                                Log.i("InputThread2(서비스)>>", "run: InputThread 00 : " + dataList.get(0));
//                                i++;
//                                Log.i("InputThread2(서비스)>>", "run: InputThread 00 (int확인) " + i);
//
//                            } else if (line.contains(":")) {
//                                String[] arr = line.split(":");
////                                for (String string : arr) {
////                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-1 (string확인)" + string);
////                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-2 (arr확인)" + arr[0]);
////                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-3 (arr확인)" + arr[1]);
////                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-4 (arr확인)" + arr[2]);
//////                                    Log.i("InputThread2(서비스)>>", "run: InputThread 0-5 (arr확인)" + arr[3]);
//////                            System.out.println(string);
////                                }
//                                if (arr[0].equals(login_real.realIDck)) {
//                                    // arr[0] : 방번호
//                                    // arr[1] : 대화를 요청할 사람의 아이디
//                                    // arr[2] : 내용
//                                    dataList.add(new DataItem(arr[3], arr[0], Code.ViewType.RIGHT_CONTENT, arr[1]));
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 1 : " + dataList.get(i).getContent());
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 1-1 : " + dataList.get(i).getName());
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 1-3 (arr확인)" + arr[0]);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 1-4 (arr확인)" + arr[1]);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 1 (int확인) " + i);
//                                    //    메인 ui 바꿔주는 쓰레드
//
//                                    Thread thread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            partner_chatt_view.handler.sendEmptyMessage(1);
//                                            Log.i("ui변경 핸들러(서비스)>>", "run: InputThread 1 (int확인) " );
//                                        }
//                                    });
//                                    thread.start();
//                                    i++;
//
//                                } else {
//                                    dataList.add(new DataItem(arr[3], arr[0], Code.ViewType.LEFT_CONTENT, arr[1]));
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 2 : " + dataList.get(i).getContent());
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 2-1 : " + dataList.get(i).getName());
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 2-2 (arr확인)" + arr[0]);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 2-3 (arr확인)" + arr[1]);
//                                    Log.i("InputThread2(서비스)>>", "run: InputThread 2 (int확인) " + i);
//
//                                    //    메인 ui 바꿔주는 쓰레드
//                                    Thread thread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            partner_chatt_view.handler.sendEmptyMessage(1);
//                                            Log.i("ui변경 핸들러(서비스)>>", "run: InputThread 2 (int확인) " );
//                                        }
//                                    });
//                                    thread.start();
//                                    i++;
//
//                                }
//                            }
//                        }

//                        myService.myServiceFunc();

//                        line = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (br != null) {
                                Log.i("로그익셉션", "run: " + br.readLine());
//                        br.close();
                            }
                            if (sock != null) {
                                Log.i("로그익셉션", "run: " + sock.toString());
//                        sock.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }
        }
    }
    // 서버에서 무언가 오면 inputThread로 출력한다! -------------------------------------------------------



}
