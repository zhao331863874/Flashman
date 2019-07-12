package com.ddinfo.flashman.network;

import com.ddinfo.flashman.BuildConfig;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.constant.UrlConstant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * Created by fuh on 2017/3/22.
 * Email：unableApe@gmail.com
 */

public class SocketManager {
    private static Socket mSocket;

    private SocketManager() {

    }

    public static SocketManager getSocketManage() {
        init();
        return new SocketManager();
    }

    private static void init() {
        {
            try {
                IO.Options options = new IO.Options();
                options.multiplex = true;
                options.query = "Authorization=" + ExampleConfig.token;
                mSocket = IO.socket(BuildConfig.API_SERVER_URL, options);
                //                mSocket = IO.socket("http://127.0.0.1:3000/", options);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
