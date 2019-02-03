package fragment;


import android.graphics.Color;

import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.android.learnmiwok.R;

public class app_safety_Fragment extends Fragment implements OnGetGeoCoderResultListener {
    private static final String TAG="app_safety_Fragment";
    private MapView mMapView=null;
    private BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    boolean isFirstLoc = true;// 是否首次定位
    LocationClientOption option = null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private TextView textView_message;
    private TextView textView_s;
    public MyLocationListener myListener = new MyLocationListener();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_safety, container, false);
        mMapView =(MapView) view.findViewById(R.id.map_view);
        textView_message = (TextView) view.findViewById(R.id.textView_message);
        textView_s=(TextView)view.findViewById(R.id.textView_s);
        mBaiduMap = mMapView.getMap();
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
        option.disableCache(false);// 禁止启用缓存定位
        option.setIsNeedLocationDescribe(true);//设置需要位置描述信息
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setOpenAutoNotifyMode(); //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        mLocClient.setLocOption(option);
        mLocClient.start();
        return view;
    }



    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
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
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 设置地图中心点
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(new MapStatus.Builder().target(latLng)
                            .overlook(-15).rotate(180).zoom(17).build());
            mBaiduMap.setMapStatus(mapStatusUpdate);
            if (isFirstLoc) {

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                if (mBaiduMap.getLocationData()!=null)
                    if (mBaiduMap.getLocationData().latitude==location.getLatitude()
                            &&mBaiduMap.getLocationData().longitude==location.getLongitude()) {
                        isFirstLoc = false;
                    }
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
            }

            textView_s.setText(sb.toString());

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        mBaiduMap.clear();
////        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
////                .icon(BitmapDescriptorFactory
////                        .fromResource(R.drawable.bg_bulue)));
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//                .getLocation()));
////        String strInfo = String.format("纬度：%f 经度：%f",
////                result.getLocation().latitude, result.getLocation().longitude);
//                       // Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        mBaiduMap.clear();
//        //                mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//        //                                .icon(BitmapDescriptorFactory
//        //                                                .fromResource(R.drawable.icon_marka)));
//        BitmapDescriptor bd;
//
//        LatLng latLng = new LatLng( result.getLocation().latitude, result.getLocation().longitude);
//        TextView textView = new TextView(getActivity());
//        textView.setGravity(Gravity.CENTER);
//        textView.setBackgroundResource(R.drawable.bg_bulue);
//        textView.setTextColor(Color.RED);
//        textView.setText(result.getAddress());
//        //                 bd = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(textView));  //这个属于自定义的
//        //                 bd = BitmapDescriptorFactory.fromView(textView)/*(getBitmapFromView(textView))*/;  //这个是官方自身的
//
//        //                 OverlayOptions oo = new MarkerOptions().icon(bd).
//        //                 position(latLng);
//        //                 mBaiduMap.addOverlay(oo);
//        //                mBaiduMap.addOverlay(new TextOptions().bgColor(Color.RED).fontSize(40).text(result.getAddress()).rotate(0).position(latLng/*mBaiduMap.getMapStatus().target*/));
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//                .getLocation()));
//        textView_message.setText(result.getAddress());
//        textView_message.setTextColor(Color.RED);
//        textView_message.setBackgroundResource(R.drawable.bg_bulue);
////        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
////        mBaiduMap.setMapStatus(u);
//
//        //                Toast.makeText(MapControlDemo.this, result.getAddress(),Toast.LENGTH_LONG).show();
    }
}
