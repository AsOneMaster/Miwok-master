package com.example.android.learnmiwok.common;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.android.learnmiwok.acticity.UsersActivity;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

public class ConnectionManager {
    public static final String BROADCAST_ACTION="com.xxx.mina.common";
    public static final String MESSAGE = "message";
    private ConnectionConfig config;
    private WeakReference<Context> context;
    private NioSocketConnector connection;
    private IoSession session;
    private InetSocketAddress address;

    public ConnectionManager(ConnectionConfig config){
        this.config = config;
        this.context = new WeakReference<Context>(config.getContext());
        init();
    }

    private void init(){
        address = new InetSocketAddress(config.getIp(),config.getPort());
        connection = new NioSocketConnector();
        connection.setDefaultRemoteAddress(address);
        connection.getSessionConfig().setReadBufferSize(config.getReadBufferSize());
        connection.getFilterChain().addLast("logger", new LoggingFilter());
        connection.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
        connection.setHandler(new DefaultHandler(context.get()));
    }

    public boolean connect(){
        try {
            JSONObject jsonOb=new JSONObject();
            jsonOb.put("name",UsersActivity.UserID) ;
            jsonOb.put("client","0") ;
            ConnectFuture future = connection.connect();
            future.awaitUninterruptibly();
            session = future.getSession();
           session.write(jsonOb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tag",e.getMessage());
            return false;
        }
        return session == null ? false:true;
    }

    public void disConnect(){
        connection.dispose();
        connection = null;
        session = null;
        address = null;
        context = null;
    }
    private static class DefaultHandler extends IoHandlerAdapter {
        private Context context;
        public DefaultHandler(Context context) {
            this.context = context;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            //put session to sessionmanager for send message
            SessionManager.getInstance().setSession(session);
        }

        @Override
        public void messageReceived(IoSession session, Object message)
                throws Exception {
            if(context != null){
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(MESSAGE, message.toString());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }
    }
}
