package com.example.house2.chat;

public class ServiceThread extends Thread {
    MyService.myServiceHandler handler;
    MyService.connectSocket connect;
    boolean isRun = true;

    public  ServiceThread(MyService.connectSocket connect){
        this.connect = connect;
    }

    public void stopForever(){
        synchronized (this){
            this.isRun = false;
        }
    }
    // 여기서 원하는 myservice클래스에 있는 작업을 어떻게 진행할건지 정해야한다.
    public void run(){
        Runnable connect = new MyService.connectSocket();
        new Thread(connect).start();
        System.out.println("I am in on start");

//            handler.sendEmptyMessage(0);

//        while (isRun){
//            // 소켓연결하는 쓰레드시작
//            System.out.println("I am in on start");
//            //  Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();
//            Runnable connect = new MyService.connectSocket();
//            new Thread(connect).start();
////            handler.sendEmptyMessage(0);
//
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }

}

