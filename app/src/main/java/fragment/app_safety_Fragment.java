package fragment;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
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

import java.lang.reflect.Field;

import acticity.CalledActivity;
import acticity.MyMessageActivity;

public class app_safety_Fragment extends Fragment implements OnGetGeoCoderResultListener {
    private static final String TAG="app_safety_Fragment";
    private Spinner spinner;
    private static String[] mArrayString = null;
    private ArrayAdapter<String> mArrayAdapter;
    private MapView mMapView=null;//什么地图组件
    private BaiduMap mBaiduMap;
//短串分享
    private ShareUrlSearch mShareUrlSearch = null;
    private String uri;
    // 定位相关
    LatLng latLng=null;

    LocationClient mLocClient;
    boolean isFirstLoc = true;// 是否首次定位
    LocationClientOption option = null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private TextView textView_message;
    private TextView textView_s;
    private ImageButton img_message;
    private  View view;
    private LinearLayout s_loc,be_called;
    private ImageButton fab;
    public MyLocationListener myListener = new MyLocationListener();
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
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber("18181766092");
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
        option.setScanSpan(0);//设置每秒更新一次位置信息
        option.disableCache(false);// 禁止启用缓存定位
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


            //COMPASS罗盘态LocationMode.NORMAL 普通态LocationMode.FOLLOWING;//定位跟随态
//            MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
//            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//            BitmapDescriptor mCurrentMarker= BitmapDescriptorFactory.fromResource(R.drawable.dingwei);
//            //设置
//            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//            mBaiduMap.setMyLocationConfiguration(config);
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
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            if (location.getLocType() == BDLocation.TypeGpsLocation){//获取error code
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
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
                    uri=arg0.getUrl().toString();
                    s_loc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),uri+"!!!",Toast.LENGTH_SHORT).show();

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
            smsManager.sendTextMessage(phoneNumber,null,"您的学生处于危险中，在"+uri.toString()+"附近，请尽快前往",null,null);
        }
    }


        /**
         * 加载下拉框
         */
    boolean first=true;
    private void sinnerStart(){
        spinner= (Spinner)view.findViewById(R.id.test_spinner);
//        初始化spinner中显示的数据
        mArrayString = new String[]{"出行记录","我的求救","我的线索","我的取证"};
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
                            Toast.makeText(getActivity(),"出行记录",Toast.LENGTH_SHORT).show();
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
//        第二次点击相同
        spinner.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            BDLocation location;
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
                                Toast.makeText(getActivity(),"出行记录",Toast.LENGTH_SHORT).show();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }



    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

    }
}
