package com.example.chat_room;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient {

    static void bindWith(DeviceInfo info){
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(info.ip),info.port));
//            System.out.println("客户端已建立连接");
            Constans.setINFO("客户端已建立连接\n");
//            System.out.println("客户端信息：" + socket.getLocalAddress() + " 端口:" + socket.getLocalPort());
            Constans.setINFO("客户端信息：" + socket.getLocalAddress() + " 端口:" + socket.getLocalPort()+"\n");
            Constans.selfINFO+="IP: "+socket.getLocalAddress()+ " 端口: " + socket.getLocalPort();
//            System.out.println("服务器信息：" + socket.getInetAddress() + " 端口:" + socket.getPort());
            Constans.setINFO("服务器信息：" + socket.getInetAddress() + " 端口:" + socket.getPort()+"\n");
            MainMenu.upDateView();
            ReaderListener listener = new ReaderListener(socket);
            listener.start();
            sendData(socket);
            listener.exit();
            Constans.setINFO("客户端已退出\n");
            MainMenu.upDateView();
//            System.out.println("客户端已退出");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     * @param socket
     */
    static void sendData(Socket socket){
        try {
           PrintStream ps = new PrintStream(socket.getOutputStream());
            boolean isFinish = false;
            do {
                String msg = Constans.selfINFO+" 发送"+ Constans.sayTEXT;
                if(MainMenu.readIt){
                    ps.println(msg);
                    MainMenu.readIt=false;
                }
                if (MainMenu.conti){
                    isFinish = true;
                }
                MainMenu.upDateView();
            }while (!isFinish);
//            osReader.close();
            ps.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听数据
     */
    static class ReaderListener extends Thread{
        Socket socket;
        boolean isFinish = false;
        public ReaderListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    String response = responseReader.readLine();
                    //当服务器关闭连接了，则 IO 不再阻塞，会返回null
                    if (response == null){
                        Constans.setINFO("连接断开\n");
                        break;
                    }else {
                        if (MainMenu.conti){
                            Constans.setINFO("连接断开\n");
                            break;
                        }
                        Constans.setINFO(response + "\n");
                    }
                }while (!isFinish);
                responseReader.close();
            } catch (IOException e) {
               // e.printStackTrace();
            }finally {
                exit();
            }

        }
        public void exit(){
            isFinish =true;
            if (socket != null){
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
