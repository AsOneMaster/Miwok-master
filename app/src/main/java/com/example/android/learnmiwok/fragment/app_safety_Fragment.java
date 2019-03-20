package com.example.android.learnmiwok.fragment;



import android.annotation.SuppressLint;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.acticity.App_Activity;
import com.example.android.learnmiwok.acticity.SosActivity;
import com.example.android.learnmiwok.common.ConnectionManager;
import com.example.android.learnmiwok.common.MinaService;
import com.example.android.learnmiwok.common.SessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.apache.http.Header;

import com.example.android.learnmiwok.acticity.CalledActivity;
import com.example.android.learnmiwok.acticity.MyMessageActivity;
import com.example.android.learnmiwok.acticity.UsersActivity;
import com.example.android.learnmiwok.bean.locationBean;


public class app_safety_Fragment extends Fragment implements OnGetGeoCoderResultListener,View.OnTouchListener {
    final String CHANNEL_ID = "com.example.android.learnmiwok";
    final String CHANNEL_NAME = "friends_notify";
    public String msg;
    public static double la,lo;
    private static final String TAG="app_safety_Fragment";
    private static final String path="http://192.168.43.162:8080/Te/location";
    private Spinner spinner;
    private static String[] mArrayString = null;
    //用户id，与好友id；
    private String UserID=UsersActivity.UserID,OtherID;
    private ArrayAdapter<String> mArrayAdapter;
    //定位相关
    private Marker marker;
    private UiSettings mUiSettings;
    private MapView mMapView=null;//什么地图组件
    private BaiduMap mBaiduMap;
    BitmapDescriptor mCurrentMarker,othersCurrentMarker;
    //短串分享
    private ShareUrlSearch mShareUrlSearch = null;
    private String uri;
    // 定位相关
    LatLng latLng=null;
    LocationClient mLocClient;
    boolean isFirstLoc = true;// 是否首次定位
    LocationClientOption option = null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    //控件
    private Animation animation;
    private TextView textView_message;
    private TextView textView_s;
    private ImageButton img_message;
    private  View view;
    private LinearLayout s_loc,be_called;
    private ImageButton fab;
    private Button s,r;
    private  static JSONObject send_loc;
    private AsyncHttpClient httpClient1=new AsyncHttpClient();
    private static  locationBean locationBean=new locationBean();
    public MyLocationListener myListener = new MyLocationListener();
    MessageBroadcastReceiver receiver = new MessageBroadcastReceiver();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Log.e("onView","onViewCreate");
        Log.e("onViewstart", "Starting Service...");
        Intent intent = new Intent(getActivity().getApplicationContext(),MinaService.class);
        getActivity().getApplicationContext().startService(intent);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_safe, container, false);
        initView(view);
        initMapView(view);
        sinnerStart();

        registerBroadcast();

        Log.d("onCreateView","CreateView");
        return view;
    }
    @Override
    public void onResume() {
        mMapView.onResume();
        Log.d("onResume","Resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        Log.d("onPause","Pause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mLocClient.stop();
        mMapView = null;
        getActivity().getApplicationContext().stopService(new Intent(getActivity().getApplicationContext(),MinaService.class));
        unregisterBroadcast();
        Log.d("onDestroy","Destroy");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.d("onStop","Stop");
        super.onStop();
    }
    //初始化控件
    public  void initView(View view){
        s=(Button)view.findViewById(R.id.button1);
        r=(Button)view.findViewById(R.id.button2);
        fab=(ImageButton)view.findViewById(R.id.fab);
        textView_message = (TextView) view.findViewById(R.id.textView_message);
        textView_s=(TextView)view.findViewById(R.id.textView_s);
        s_loc=(LinearLayout)view.findViewById(R.id.share_loc);
        s_loc.setClickable(true);

        be_called=(LinearLayout)view.findViewById(R.id.be_called);
        be_called.setClickable(true);
        img_message=(ImageButton)view.findViewById(R.id.img_message);
        img_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MyMessageActivity.class);
                if (intent != null) {
                    startActivity(intent);
                   //getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.no_ture_out);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SosActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
//                callPhoneNumber("18181766092");
            }
        });
        be_called.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CalledActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }

            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadingLoc();
                s.setVisibility(View.GONE);
                r.setVisibility(View.VISIBLE);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("client","no");
                jsonObject.put("Userid",UserID);
                //mina框架传值
                SessionManager.getInstance().writeToServer(jsonObject.toString());
                s.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
            }
        });

        fab.setOnTouchListener(this);
        s.setOnTouchListener(this);
    }

    //一键拨号
    private void callPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
    //加载地图
    public void initMapView(View view){
        mMapView =(MapView) view.findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        //隐藏指南针
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
        // 删除百度地图LoGo
        mMapView.removeViewAt(1);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 定位初始化
        mLocClient = new LocationClient(getActivity().getApplicationContext());
        mLocClient.registerLocationListener(myListener);//将原代码中public class MyLocationListener implements BDLocationListener改为 public class MyLocationListener extends BDAbstractLocationListener

        option = new LocationClientOption();
        Log.i(TAG, "==-->option:="+option);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式：高精度，低功耗，仅设备
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(0);//设置每秒更新一次位置信息
        option.disableCache(false);// 禁止启用缓存定位
        option.setIsNeedLocationDescribe(true);//设置需要位置描述信息
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        option.setOpenAutoNotifyMode(); //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        mLocClient.setLocOption(option);
        mLocClient.start();




    }
    /**
     * 上传本地位置给服务器
     */
    private void uploadingLoc(){

        send_loc=JSONObject.parseObject(JSONObject.toJSONString(locationBean).toString());
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("client","yes");
        jsonObject.put("Userid",UserID);

        //mina框架传值
        SessionManager.getInstance().writeToServer(jsonObject.toString());

        RequestParams entity = null;
        entity = new RequestParams();
        entity.put("loc",send_loc.toString());
        AsyncHttpClient httpClient=new AsyncHttpClient();

        Log.e("userid",UserID);
        httpClient.post(path,entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
//                if(statusCode==200){
//                    for(int i=0;i<response.length();i++){
//                        try {
//                            UserID=response.getString("userid");
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//
//                        }
                }
            });

    }


    /**
     * 显示他人位置
     */
    private void shareLocation(double la,double lo){
        //添加他人位置
        Resources r = this.getResources();
        Bitmap bmp= BitmapFactory.decodeResource(r, R.drawable.head_img);
        addOthersLocation(la ,lo,bmp);
        Log.e("SOO",la+""+lo);
    }
    /**
     * 添加他人位置
     */
    public void addOthersLocation(double latitute,double longtitute, Bitmap touxiang) {

        Resources r = this.getResources();

        Bitmap bmp= BitmapFactory.decodeResource(r, R.drawable.dingwei);//标识红点

        //构建Marker图标
        othersCurrentMarker = BitmapDescriptorFactory
                .fromBitmap(mergeBitmap(bmp,touxiang));

        //定义Maker坐标点
        LatLng point = new LatLng(latitute, longtitute);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(othersCurrentMarker);
        //在地图上添加Marker，并显示
        marker=(Marker) mBaiduMap.addOverlay(option);
        //显示在中心
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder().target(new LatLng(latitute,longtitute))
                        .overlook(-15).rotate(360).zoom(12).build());
        mBaiduMap.setMapStatus(mapStatusUpdate);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(latitute,longtitute));
        mBaiduMap.animateMapStatus(u);
    }
    //将两张图片合并为一张图片 用作头像
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        int Width = firstBitmap.getWidth();
        int height = firstBitmap.getHeight();
        secondBitmap = zoomImage(secondBitmap,Width,height);
        Bitmap bitmap = Bitmap.createBitmap(Width, height*2,
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(secondBitmap, new Matrix(), null);
        canvas.drawBitmap(firstBitmap, 0, height, null);
        return bitmap;
    }

    //缩放头像图片
    public  Bitmap zoomImage(Bitmap bgimage, double newWidth,
                             double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){

            animation=AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
            v.startAnimation(animation);
        }
        //抬起操作
        if(event.getAction()==MotionEvent.ACTION_UP){

            animation=AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_out);
            v.startAnimation(animation);
        }
        //移动操作
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            animation=AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_stay);
            v.startAnimation(animation);
        }
        return false;

    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            //                        // 反Geo搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(mBaiduMap.getMapStatus().target));
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
//          实时获取地址短串
            shardUrlGPS(latLng);
//          获取本地位置信息
            locationBean.setDate(location.getTime());
            locationBean.setUserId(UserID);
            locationBean.setLatitude(location.getLatitude());
            locationBean.setLongitude(location.getLongitude());
            locationBean.setAddr(location.getAddrStr());
            locationBean.setLocationDescribe(location.getLocationDescribe());


            if (isFirstLoc) {
                // 设置地图中心点
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(new MapStatus.Builder().target(latLng)
                                .overlook(-15).rotate(360).zoom(17).build());
                mBaiduMap.setMapStatus(mapStatusUpdate);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(u);
                isFirstLoc = false;

            }

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//获取维度
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());//获取经度
            sb.append("\nradius : ");
            sb.append(location.getRadius());//获取定位精度半径，单位是米
            if (location.getLocType() == BDLocation.TypeGpsLocation){//获取error code
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nlocationDescribe : ");
                sb.append(location.getLocationDescribe());
            }

            textView_s.setText(sb.toString());

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
        /**
         * 位置分享短串
         * @param l
         */
        public void shardUrlGPS(LatLng l){

            mShareUrlSearch = ShareUrlSearch.newInstance();
            OnGetShareUrlResultListener listener = new OnGetShareUrlResultListener() {

                @Override
                public void onGetRouteShareUrlResult(ShareUrlResult arg0) {
                    Log.d("URI1:",arg0.getUrl().toString());
                }
                @Override
                public void onGetLocationShareUrlResult(ShareUrlResult arg0) {
                    uri=arg0.getUrl();
                    s_loc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"求助短信已发送",Toast.LENGTH_SHORT).show();

                            myListener.sendPhoneNumber("18181766092");
                        }
                    });
                    Log.d("URI2:",arg0.getUrl().toString());
                }
                @Override
                public void onGetPoiDetailShareUrlResult(ShareUrlResult arg0) {
                    Log.d("URI3:",arg0.getUrl().toString());
                }
            };

            mShareUrlSearch.setOnGetShareUrlResultListener(listener);
            mShareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                    .location(l).name("你好友分享的位置").snippet("SOS"));
            Log.d("位置分享短串",new LocationShareURLOption()
                    .location(l).name("你好友分享的位置").snippet("SOS").toString());

        }
        //一键发送短信
        private void sendPhoneNumber(String phoneNumber){
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null,"【\bCare\byou\b】您好，您的学生通过Care\byou向您发起了求助，最后地点在"+uri.toString(),null,null);
        }
    }


        /**
         * 加载下拉框
         */
    boolean first=true;
    private void sinnerStart(){
        spinner= (Spinner)view.findViewById(R.id.test_spinner);
//        初始化spinner中显示的数据
        mArrayString = new String[]{"我的位置","我的求救","我的线索","我的取证"};
//        adapter_mytopactionbar_spinner改变了spinner的默认样式
        mArrayAdapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.adapter_mytopactionbar_spinner,mArrayString){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
//                    设置spinner展开的Item布局
                    convertView = getLayoutInflater().inflate(R.layout.adapter_mytopactionbar_spinner_item, parent, false);
                }
                TextView spinnerText=(TextView)convertView.findViewById(R.id.spinner_textView);
                spinnerText.setText(getItem(position));
                return convertView;
            }
        };
        spinner.setAdapter(mArrayAdapter);


        //设置第一次不触发点击事件：
       // spinner.setSelection(0, true);
//        spinner设置监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //设置初始化不触发点击事件：
                if (first) {
                    first= false;
                } else {
                    //处理事件的代码
                    switch (position){
                        case 0:

                            break;
                        case 1:
                            Toast.makeText(getActivity(),"我的求救",Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(getActivity(),"我的线索",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(getActivity(),"我的取证",Toast.LENGTH_SHORT).show();

                            break;
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

    }
    public void registerBroadcast(){
        IntentFilter filter = new IntentFilter(ConnectionManager.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, filter);
    }
    public void unregisterBroadcast(){
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(receiver);
    }


    /**
     * receive message and update ui
     */
    private class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            msg=intent.getStringExtra(ConnectionManager.MESSAGE);
            if (isJson(msg)){

                JSONObject object=JSONObject.parseObject(msg);
                //接受通知
                notifing(object.getString("situation"));
                if (object.getString("safe").equals("yes")) {
                    marker.remove();
                }
                if(object.getString("safe").equals("no")){
                    la = object.getDouble("la");

                    lo=object.getDouble("lo");
                    //好友守护，在地图上显示位置
                    shareLocation(la, lo);

                    Log.e("loo",lo+"");
                }



            }
            else {
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
            }




        }
        public boolean isJson(String content) {
            try {
                JSONObject.parseObject(content);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * 好友守护通知
         */
        public void notifing(String notif){


            NotificationManager mNotificationManager = (NotificationManager)
                    getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //只在Android O之上需要渠道
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
                //通知才能正常弹出

                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity(),CHANNEL_ID);

            Intent clickIntent = new Intent(getActivity(), NotificationClickReceiver.class); //点击通知之后要发送的广播

            PendingIntent contentIntent = PendingIntent.getBroadcast(getActivity(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("好友守护")
                    .setContentText(notif)
                    .setAutoCancel(true).setContentIntent(contentIntent);

            mNotificationManager.notify(0, builder.build());



        }

    }

    /**
     * 实时接受通知
     */
    public static class NotificationClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //todo 跳转之前要处理的逻辑
            //启动应用
            Log.e("TAG", "userClick:我被点击啦！！！ ");
            Intent newIntent = new Intent(context, App_Activity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(newIntent);



        }
    }

}