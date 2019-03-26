package com.example.android.learnmiwok.fragment;



import android.annotation.SuppressLint;

import android.app.AlertDialog;
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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
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
import android.widget.TimePicker;
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
import com.baidu.mapapi.map.MyLocationConfiguration;
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
import com.example.android.learnmiwok.MyApp;
import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.TimingTextView;
import com.example.android.learnmiwok.acticity.App_Activity;
import com.example.android.learnmiwok.acticity.SosActivity;

import com.example.android.learnmiwok.bean.LocationBean;
import com.example.android.learnmiwok.common.ConnectionManager;
import com.example.android.learnmiwok.common.MinaService;
import com.example.android.learnmiwok.common.SessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.apache.http.Header;
import org.json.JSONException;

import com.example.android.learnmiwok.acticity.CalledActivity;
import com.example.android.learnmiwok.acticity.MyMessageActivity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class app_safety_Fragment extends Fragment implements OnGetGeoCoderResultListener,View.OnTouchListener {

    /**广播通知,渠道ID*/
    final String CHANNEL_ID = "com.example.android.learnmiwok";
    final String CHANNEL_NAME = "friends_notify";
    public String msg;
    public  String b_msg;
    int MaxTime;
    public static double la,lo;
    private static final String TAG="app_safety_Fragment";
    private static final String path="http://192.168.43.162:8080/Te/location";
    private Spinner spinner;
    private static String[] mArrayString = null;
    //用户id，与好友id；
    private String UserID=MyApp.getInstance().getUserip(),OtherID;
    private ArrayAdapter<String> mArrayAdapter;
    //定位相关
    private  static JSONObject send_loc;
    private static String firstSend=null;
    private UiSettings mUiSettings;
    private MapView mMapView=null;//什么地图组件
    private BaiduMap mBaiduMap;
    BitmapDescriptor mCurrentMarker,othersCurrentMarker;
    //短串分享
    private ShareUrlSearch mShareUrlSearch = null;
    private String uri;
    // 定位相关
    private boolean isfirstNotify;
    Marker marker;
    LatLng latLng=null;
    LocationClient mLocClient;
    boolean isFirstLoc = true;// 是否首次定位
    LocationClientOption option = null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    //控件
    private TimingTextView timingTextView;
    private Animation animation;
    private TextView textView_message;
    private TextView textView_s;
    private ImageButton img_message;
    private  View view;
    private LinearLayout s_loc,be_called;
    private Button SOS;
    private Button s,r;
    private LocationBean locationBean=new LocationBean();
    private  LocationBean locationBean_e=new LocationBean();

    public MyLocationListener myListener = new MyLocationListener();
    MessageBroadcastReceiver receiver = new MessageBroadcastReceiver();
    private List<Marker> markers=new ArrayList<Marker>();
    BaseFullBottomSheetFragment bottomSheetDialog = new BaseFullBottomSheetFragment();
    /**timer对象**/
    Timer mTimer = null;

    /**TimerTask对象**/
    TimerTask mTimerTask = null;
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("client", "yes");
                    jsonObject.put("Userid", UserID);
                    jsonObject.put("lo", latLng.longitude);
                    jsonObject.put("la", latLng.latitude);
                    jsonObject.put("firstSend",firstSend);
                    firstSend="no";
                    //mina框架传值
                    SessionManager.getInstance().writeToServer(jsonObject.toString());
                    break;
                case 1:

                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 导入自定义地图样式
     * @param context
     * @param fileName
     */
    private void setMapCustomFile(Context context, String fileName) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets().open("mapConfig/" + fileName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = context.getFilesDir().getAbsolutePath();
            File file = new File(moduleName + "/" + fileName);
            if (file.exists()) file.delete();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            //将自定义样式文件写入本地
            fileOutputStream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//设置自定义样式文件
        mMapView.setCustomMapStylePath(moduleName + "/" + fileName);
    }


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
        //自定义地图图层文件
        setMapCustomFile(getActivity().getApplicationContext(), "custom_map_config.json");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_app_safety_main, container, false);

        initView(view);
        initMapView(view);
        sinnerStart();
        //注册接受服务
        registerBroadcast();

        Log.d("onCreateView","CreateView");
        return view;
    }
    public void StartTimer(){
        if(mTimer == null){
            mTimerTask = new TimerTask(){
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);

                    if(timingTextView.getTime()==0){
                        /**
                         * 如何没有及时到达目的地，自动启动报警
                         */
                        timingTextView.setTextColor(getResources().getColor(R.color.red));
//                        myListener.sendPhoneNumber("18181766092");
                    };

                }

            };
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 20,3000);
        }
    }
    public void CloseTimer(){
        if(mTimer !=null){
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask!= null){
            mTimerTask = null;
        }
        timingTextView.setTextColor(getResources().getColor(R.color.black));
        handler.sendEmptyMessage(1);
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

        timingTextView=(TimingTextView)view.findViewById(R.id.Count_down);
        s=(Button)view.findViewById(R.id.button1);
        r=(Button)view.findViewById(R.id.button2);
        SOS=(Button)view.findViewById(R.id.fab);
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
        SOS.setOnClickListener(new View.OnClickListener() {
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
        //开启上传位置线程
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开Dialog，选择出行时间
                 SelecTimeDialog();
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭线程
                CloseTimer();
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("client","no");
                jsonObject.put("Userid",UserID);
                uploadingLoc("no",locationBean_e);
                //mina框架传值
                SessionManager.getInstance().writeToServer(jsonObject.toString());
                s.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
                timingTextView.stop();
                timingTextView.setVisibility(View.GONE);
            }
        });

        SOS.setOnTouchListener(this);
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
        mMapView.setMapCustomEnable(true);
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
        option.setScanSpan(1000);//设置每秒更新一次位置信息
        option.disableCache(true);// 禁止启用缓存定位
        option.setIsNeedLocationDescribe(true);//设置需要位置描述信息
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        //option.setOpenAutoNotifyMode(); //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        mLocClient.setLocOption(option);
        mLocClient.start();
    }
    /**
     * 上传本地位置给服务器
     */
    private void uploadingLoc(String isStart,LocationBean locationBean){
        send_loc=JSONObject.parseObject(JSONObject.toJSONString(locationBean));
        RequestParams entity = null;
        entity = new RequestParams();
        entity.put("loc",send_loc.toString());
        entity.put("isStart",isStart);
        AsyncHttpClient httpClient=new AsyncHttpClient();
        Log.e("userid",UserID);
        httpClient.post(path,entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
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
        try {
            if (markers.size()!=0){
               markers.clear();
               marker.remove();
            }
            marker=(Marker) mBaiduMap.addOverlay(option);
            markers.add(marker);
        }catch (Exception e){
            e.printStackTrace();
        }

        //显示在中心
        if(isfirstNotify){
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(new MapStatus.Builder().target(new LatLng(latitute,longtitute))
                            .overlook(-15).rotate(360).zoom(13).build());
            mBaiduMap.setMapStatus(mapStatusUpdate);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(latitute,longtitute));
            mBaiduMap.animateMapStatus(u);
            isfirstNotify=false;
        }

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
//          定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(null,true,null,0x441e90ff,0));
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
//          实时获取地址短串
            shardUrlGPS(latLng);
//          获取本地位置信息
            locationBean.setDate(location.getTime());
            locationBean.setUserId(Integer.parseInt(UserID));
            locationBean.setAddr(location.getAddrStr());
            locationBean.setLocationDescribe(location.getLocationDescribe());

            locationBean_e.setUserId(Integer.parseInt(UserID));
            locationBean_e.setEnd_addr(location.getAddrStr());
            locationBean_e.setEnd_locationDescribe(location.getLocationDescribe());
            if(location.getFloor()!=null){
                //开启室内定位
                mLocClient.startIndoorMode();
            }
            if (isFirstLoc) {

                if(latLng!=null) {
                    // 设置地图中心点
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                            .newMapStatus(new MapStatus.Builder().target(latLng)
                                    .overlook(-15).rotate(360).zoom(17).build());
                    mBaiduMap.setMapStatus(mapStatusUpdate);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                    mBaiduMap.animateMapStatus(u);
                    isFirstLoc = false;

                }


            }

            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getAddrStr()+location.getLocationDescribe());

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
        mArrayString = new String[]{"我的位置","出行记录","我的线索","我的取证"};
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
                        case 0:   //显示在中心
                            showMylocation();
                            break;
                        case 1:
                            RequestParams entity = null;
                            entity = new RequestParams();
                            entity.put("isStart","con");
                            entity.put("userid",MyApp.getInstance().getUserip());
                            AsyncHttpClient httpClient=new AsyncHttpClient();
                            httpClient.post(path,entity, new JsonHttpResponseHandler(){
                                @Override
                                public  void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                                    if(statusCode==200){
                                        for(int i=0;i<response.length();i++){
                                            try {
                                                b_msg=response.getString("show");
                                                Log.e("ssss",b_msg);
                                                MyApp.getInstance().setMsg(b_msg) ;
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }
                                        bottomSheetDialog.show(getActivity().getSupportFragmentManager(),"dialog");
                                    }
                                }
                            });


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
        //第二次点击相同
        spinner.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

                try {
                    Class<?> clazz = AdapterView.class;
                    Field mOldSelectedPosition = clazz.getDeclaredField("mOldSelectedPosition");
                    Field mSelectedPosition = clazz.getDeclaredField("mSelectedPosition");
                    mOldSelectedPosition.setAccessible(true);
                    mSelectedPosition.setAccessible(true);
                    if (mOldSelectedPosition.getInt(spinner) == mSelectedPosition.getInt(spinner)) {
                        //响应事件
                        switch (mSelectedPosition.getInt(spinner)){
                            case 0:
                                showMylocation();
                                break;
                            case 1:

                                bottomSheetDialog.show(getActivity().getSupportFragmentManager(),"dialog");
                                break;
                            case 2:
                                Toast.makeText(getActivity(),"我的线索",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getActivity(),"我的取证",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }


    private void showMylocation(){
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder().target(latLng)
                        .overlook(-15).rotate(360).zoom(17).build());
        mBaiduMap.setMapStatus(mapStatusUpdate);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(u);
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
     * receive message and update Marker on baiduMap
     */
    private class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            msg=intent.getStringExtra(ConnectionManager.MESSAGE);
            if (isJson(msg)){

                JSONObject object=JSONObject.parseObject(msg);
                //接受通知
                if(object.getString("firstSend").equals("yes")){
                    isfirstNotify=true;
                    notifing(object.getString("situation"));
                }

                if (object.getString("safe").equals("yes")) {
                    isfirstNotify=true;
                    marker.remove();
                    notifing(object.getString("situation"));
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
                if(msg.equals("好友未上线"))
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
                    .setTicker("好友守护")
                    .setContentTitle("请与好友保持联系")
                    .setContentText(notif)
                    .setAutoCancel(true).setContentIntent(contentIntent);

            mNotificationManager.notify(0, builder.build());



        }

    }

    /**
     * 时间选择Dialog
     */
    private void SelecTimeDialog(){

        AlertDialog dialog=new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.setContentView(R.layout.time_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MaxTime=0;

        ((TimePicker) dialog.getWindow().findViewById(R.id.time_picker)).setIs24HourView(true);
        ((TimePicker) dialog.getWindow().findViewById(R.id.time_picker)).setHour(0);
        ((TimePicker) dialog.getWindow().findViewById(R.id.time_picker)).setMinute(0);
        ((TimePicker) dialog.getWindow().findViewById(R.id.time_picker)).setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {

                MaxTime=hourOfDay*3600+minute*60;
            }
        });
        dialog.getWindow().findViewById(R.id.quit).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }});
        dialog.getWindow().findViewById(R.id.select).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //开启线程
                StartTimer();
                timingTextView.setMaxTime(MaxTime);
                timingTextView.setVisibility(View.VISIBLE);
                timingTextView.start();
                firstSend="yes";
                uploadingLoc("yes",locationBean);
                locationBean_e.setDate(locationBean.getDate());
                s.setVisibility(View.GONE);
                r.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }});
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
            Intent newIntent = new Intent(context, App_Activity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//SINGLE_TOP 单例活动不创建新Activity
            context.startActivity(newIntent);



        }
    }

}
