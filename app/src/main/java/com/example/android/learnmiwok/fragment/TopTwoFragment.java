package com.example.android.learnmiwok.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.android.learnmiwok.R;


public class TopTwoFragment extends Fragment {

    private View view;
    private WebView showView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_top_two, container, false);
        showView = (WebView)view.findViewById(R.id.webview);
        WebSettings webSettings = showView.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);


        showView.setWebViewClient(new WebViewClient() {
                                      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                                          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                              view.loadUrl(request.getUrl().toString());
                                          } else {
                                              view.loadUrl(request.toString());
                                          }
                                          return false;
                                      }});
        showView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((keyCode == KeyEvent.KEYCODE_BACK) &&showView.canGoBack()){
                  getActivity().runOnUiThread(new Runnable(){
                      @Override
                        public void run(){
                            showView.goBack();
                                }
                            });
                  return true;
                }
                return false;
            }

        });
//        button =(Button)view.findViewById(R.id.heart);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent=new Intent(Intent.ACTION_VIEW);
////                intent.setData(Uri.parse("http://m.woniu8.com"));
////                startActivity(intent);
//                showView.loadUrl("http://m.woniu8.com/");
//                button.setVisibility(View.GONE);
//            }
//        });
        showView.loadUrl("http://m.woniu8.com/ceshi/");
        return view;
    }


}
