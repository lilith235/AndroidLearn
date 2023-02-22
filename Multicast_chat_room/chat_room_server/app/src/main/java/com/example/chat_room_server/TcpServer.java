package com.example.chat_room_server;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TcpServer implements ClientDataHandle.ResponseListener {

    private static ClientListener mClientListener;
    private static List<ClientDataHandle>  mClientHandles = new ArrayList<>();
    private static ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public TcpServer() {

        try {
            mClientListener = new ClientListener();
            mClientListener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        mClientListener.exit();
        for (int i = 0; i < mClientHandles.size(); i++) {
            ClientDataHandle handle = mClientHandles.get(i);
            handle.exit();
        }
        mClientHandles.clear();

        if (mExecutorService != null){
            mExecutorService.shutdownNow();
        }
//        System.out.println("服务器退出");
        Constans.setINFO("服务器退出\n");
    }



    public synchronized void sendBroad(String msg) {
        for (ClientDataHandle clientHandle : mClientHandles) {
            clientHandle.sendMsg(msg);
        }
    }



    @Override
    public void newMsg(final ClientDataHandle handle, final String msg) {
//        System.out.println(handle.getInfo()+" say: "+msg);

            Constans.setINFO(msg+"\n");

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                for (ClientDataHandle clientHandle : mClientHandles) {
                    if (clientHandle == handle){
                        //跳过自己
                        continue;
                    }
                    //发送给其他客户端
                    clientHandle.sendMsg(msg);
                }
            }
        });

    }

    class ClientListener extends Thread {

        private boolean flag = true;
        private ServerSocket serverSocket;
        private Socket socket;
        public ClientListener() throws IOException {
            if(serverSocket==null){
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(Constans.TCP_PORT));
            }

        }

        @Override
        public void run() {
            super.run();
            try {
                do {
                    socket = serverSocket.accept();
                    ClientDataHandle clientHandle = new ClientDataHandle(socket,TcpServer.this);
                    synchronized (TcpServer.this) {
                        mClientHandles.add(clientHandle);
                    }
                }while (flag);


            } catch (IOException e) {
               // e.printStackTrace();
            } finally {
                exit();
            }

        }

        public void exit() {
            flag = false;
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    serverSocket = null;
                }

                if (socket != null){
                    socket.close();
                    socket = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
//                System.out.println("close error: "+e.toString());
                Constans.setINFO("close error: "+e.toString()+"\n");
            }
        }
    }


}
