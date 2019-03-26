package com.example.android.learnmiwok.acticity;


import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



import com.example.android.learnmiwok.R;


import java.util.ArrayList;

import java.util.List;

import com.example.android.learnmiwok.adapter.FragmentTabAdapter;

import com.example.android.learnmiwok.fragment.app_mineFragment;
import com.example.android.learnmiwok.fragment.app_safety_Fragment;
import com.example.android.learnmiwok.fragment.app_squareFragment;



public class App_Activity extends FragmentActivity {
    private FragmentManager manager;
    // private FragmentTransaction transaction;
    private String ip;
    private RadioGroup rgs;
    private RadioButton[] rb;
    private Drawable drawables[];
    private List<Fragment> fragments = new ArrayList<Fragment>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_);
        fragments.add(new app_safety_Fragment());
        fragments.add(new app_squareFragment());
        fragments.add(new app_mineFragment());
        rgs = (RadioGroup) findViewById(R.id.tab_group);
        //定义RadioButton数组用来装RadioButton，改变drawableTop大小
        rb = new RadioButton[3];
        //将RadioButton装进数组中
        rb[0] = (RadioButton) findViewById(R.id.image_safety);
        rb[1] = (RadioButton) findViewById(R.id.image_square);
        rb[2] = (RadioButton) findViewById(R.id.image_mine);
        //for循环对每一个RadioButton图片进行缩放
        for (int i = 0; i < rb.length; i++) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            drawables = rb[i].getCompoundDrawables();
            //获取drawables，2/5表示图片要缩小的比例
            Rect r = new Rect(0, 0, drawables[1].getMinimumWidth() * 2 / 5, drawables[1].getMinimumHeight() * 2 / 5);
            //定义一个Rect边界
            drawables[1].setBounds(r);
            //给每一个RadioButton设置图片大小
            rb[i].setCompoundDrawables(null, drawables[1], null, null);
        }
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.fragment, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");

            }
        });
        //获取ip地址
        // getMyip();


    }
    /**
     * 获取本地ip地址
     */
//    private void getMyip(){
//
//        ConnectivityManager conMann = (ConnectivityManager)
//                this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo NetworkInfo = conMann.getActiveNetworkInfo();
//
//        if (NetworkInfo!=null&&NetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE) {
//            ip = getLocalIpAddress();
//            System.out.println("本地ip-----"+ip);
//        }else if(NetworkInfo!=null&&NetworkInfo.getType()==ConnectivityManager.TYPE_WIFI)
//        {
//            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            int ipAddress = wifiInfo.getIpAddress();
//            ip = intToIp(ipAddress);
//            System.out.println("wifi_ip地址为------"+ip);
//        }
//        MyApp myApp=(MyApp)getApplication();
//        myApp.setUserip(ip);
//    }
//
/////如果连接的是移动网络,获取本地ip地址：getLocalIpAddress();这样获取的是ipv4格式的ip地址。
//    public String getLocalIpAddress() {
//        try {
//            String ipv4;
//            ArrayList<NetworkInterface>  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface ni: nilist)
//            {
//                ArrayList<InetAddress>  ialist = Collections.list(ni.getInetAddresses());
//                for (InetAddress address: ialist){
//                    if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress()))
//                    {
//                        return ipv4;
//                    }
//                }
//
//            }
//
//        } catch (SocketException ex) {
//            Log.e("localip", ex.toString());
//        }
//        return null;
//    }
////如果连接的是WI-FI网络，获取WI-FI ip地址：intToIp(ipAddress);
//    public static String intToIp(int ipInt) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(ipInt & 0xFF).append(".");
//        sb.append((ipInt >> 8) & 0xFF).append(".");
//        sb.append((ipInt >> 16) & 0xFF).append(".");
//        sb.append((ipInt >> 24) & 0xFF);
//        return sb.toString();
//    }
//    //以上得到的地址是Ipv6的地址，而不是Ipv4的地址,这里转ipv4
//    private String getlocalIp() {
//        String ip;
//
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()&&!inetAddress.isLinkLocalAddress()) {
////	    	                	ip=inetAddress.getHostAddress().toString();
//                        System.out.println("ip=========="+inetAddress.getHostAddress().toString());
//                        return inetAddress.getHostAddress().toString();
//
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            Log.e("WifiPreference IpAddress", ex.toString());
//        }
//        return null;
//    }


    /**
     * 点击两次back退出程序
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //退出程序但不销毁===ROOT_Activity
//            moveTaskToBack(false);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
}




