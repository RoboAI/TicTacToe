package com.example.tictactoe;

import static com.example.tictactoe.GlobalDefines.DEFAULT_UDP_PORT;
import static com.example.tictactoe.GlobalDefines.TAG;
import static com.example.tictactoe.GlobalDefines.globalMyIP;

import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class NDClient implements Runnable {
    volatile SingleMoveDataPacket waitForNewData;
    com.example.tictactoe.UDPSocket socket;
    Thread thread;

    //String connectIP = "192.168.1.100";
    //String connectIP = "192.168.43.46";//first client
    String connectIP = "192.168.43.46";//first client

    public NDClient(SingleMoveDataPacket object){
        waitForNewData = object;
    }

    public void startClient(){
        Log.i(TAG, "NDClient:startClient(): ");

        socket = new com.example.tictactoe.UDPSocket(DEFAULT_UDP_PORT, connectIP);
        boolean result = socket.connect();

        Log.i(TAG, "NDClient: connect = " + result);

        thread = new Thread(this);
        thread.start();
    }

    public void stopClient(){
        Log.i(TAG, "NDClient:stopClient(): ");
        socket.close();
        thread.interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);

        Log.i(TAG, "NDClient:run():");

        String s1;
        String s2;
        String all;
        byte[] bytes;
        DatagramPacket packet;
        //String myIP = globalMyIP.substring(globalMyIP.length() - 3);
        String myIP = globalMyIP;

        InetAddress sa;

        try{
            //Log.i(TAG, "NDClient:run: setBroadcast... ");
           // socket.socket.setBroadcast(true);
            sa = InetAddress.getByName(connectIP);
            packet = new DatagramPacket(new byte[1], 1, sa, DEFAULT_UDP_PORT);

            while(true) {

                Log.i(TAG, "NDClient:run: waiting for signal...");
                synchronized (waitForNewData) {
                    waitForNewData.wait();
                }

                Log.i(TAG, "NDClient:run: received signal...");

                s1 = String.valueOf(waitForNewData.x);
                s2 = String.valueOf(waitForNewData.y);
                all = GlobalDefines.START_TAG + myIP + "x" + s1 + "y" + s2;

                Log.i(TAG, "NDClient:run: myIP: " + myIP);
                Log.i(TAG, "NDClient:run: sending: " + all);

                bytes = all.getBytes();

                try {
                    packet.setData(bytes);
                    socket.socket.send(packet);
                    //socket.SendUDP(packet);
                } catch (IOException e) {
                    Log.i(TAG, "NDClient:run: IOException: " + e.getMessage());
                }


                if (Thread.currentThread().isInterrupted()) {
                    Log.i(TAG, "NDClient:run: Thread.currentThread().isInterrupted()");
                    throw new InterruptedException();
                }

                //Thread.sleep(1000);
            }
        }catch (InterruptedException e){
            Log.i(TAG, "NDClient:run: InterruptedException: " + e.getMessage());

        }catch (Exception e) {
            Log.i(TAG, "NDClient:run: Exception:" + e.getMessage());

        }

        Log.i(TAG, "NDClient:run: return from run()");
    }
}
