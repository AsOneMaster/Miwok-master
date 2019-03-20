package com.example.android.learnmiwok.acticity;


import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.example.android.learnmiwok.AutoFitTextureView;
import com.example.android.learnmiwok.R;

import java.io.File;
import java.io.FileOutputStream;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SosActivity extends Activity {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final String TAG = "SosActivity";

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private static boolean sos_loc=false;
    private Timer timer=new Timer();
    private Handler handler;
    private MediaPlayer mMediaPlayer=new MediaPlayer();
    private AutoFitTextureView textureView;
    // 摄像头ID（通常0代表后置摄像头，1代表前置摄像头）
    private String mCameraId = "0";
    // 定义代表摄像头的成员变量
    private CameraDevice cameraDevice;
    // 预览尺寸
    private Size previewSize;
    private CaptureRequest.Builder previewRequestBuilder;
    // 定义用于预览照片的捕获请求
    private CaptureRequest previewRequest;
    // 定义CameraCaptureSession成员变量
    private CameraCaptureSession captureSession;
    private ImageReader imageReader;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture
                , int width, int height) {
            // 当TextureView可用时，打开摄像头
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture
                , int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        //  摄像头被打开时激发该方法
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            SosActivity.this.cameraDevice = cameraDevice;
            // 开始预览
            createCameraPreviewSession();  // ②
        }

        // 摄像头断开连接时激发该方法
        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            SosActivity.this.cameraDevice = null;
        }

        // 打开摄像头出现错误时激发该方法
        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            SosActivity.this.cameraDevice = null;
            SosActivity.this.finish();
        }
    };

    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        textureView = (AutoFitTextureView) findViewById(R.id.texture);
        // 为该组件设置监听器
        textureView.setSurfaceTextureListener(mSurfaceTextureListener);
//        findViewById(R.id.capture).setOnClickListener(this);
       //播放警笛声
        playVoice();
        //每两秒一次拍照
        takephoto();

    }

    @Override
    protected void onStart() {
        super.onStart();
        i=0;
        sos_loc=false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        i=0;
        sos_loc=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVoice();
        sos_loc=true;
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sos_loc=true;
        finish();

    }

    /**
     * 每几秒自动拍照
     */
    private void takephoto(){
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 0) {
                    /**
                     * 写执行的代码
                     */
                    captureStillPicture();
                    Log.d("SosActivity", "123456");
                }
            }
        };

        /**
         * timer.schedule()方法的三个参数解释 :

         ① : 一个TimerTask对象

         ② : 0 的意思 : 当你调用了timer.schedule()方法之后,这个方法就肯定会调用TimerTask()方法中的run()方法,这个参数指的是这两者之间的差值,也就是说用户在调用了schedule()方法之后,会等待0时间,才会第一次执行run()方法,0也就是代表无延迟了,如果传入其他的,就代表要延迟执行了

         ③ : 第一次调用了run()方法之后,从第二次开始每隔多长时间调用一次run()方法.
         */
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!sos_loc){
                    // (1) 使用handler发送消息
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }else{
                    timer.cancel();
                }


            }
        },3000,3000);

    }

    private void captureStillPicture() {
        try {
            if (cameraDevice == null) {
                return;
            }
            // 创建作为拍照的CaptureRequest.Builder
            final CaptureRequest.Builder captureRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(imageReader.getSurface());
            // 设置自动对焦模式
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 设置自动曝光模式
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取设备方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION
                    , ORIENTATIONS.get(rotation));
            // 停止连续取景
            captureSession.stopRepeating();
            // 捕获静态图像
            captureSession.capture(captureRequestBuilder.build()
                    , new CameraCaptureSession.CaptureCallback()  // ⑤
                    {
                        // 拍照完成时激发该方法
                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session
                                , CaptureRequest request, TotalCaptureResult result) {
                            try {
                                // 重设自动对焦模式
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                                        CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                                // 设置自动曝光模式
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                                // 打开连续取景模式
                                captureSession.setRepeatingRequest(previewRequest, null,
                                        null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 警笛声
     *
     */
    private void playVoice(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mMediaPlayer.start();
    }
    private void stopVoice(){
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
    // 打开摄像头
    private void openCamera(int width, int height) {
        setUpCameraOutputs(width, height);
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            // 打开摄像头
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            manager.openCamera(mCameraId, stateCallback, null); // ①
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface surface = new Surface(texture);
            // 创建作为预览的CaptureRequest.Builder
            previewRequestBuilder = cameraDevice
                    .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将textureView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(new Surface(texture));
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            cameraDevice.createCaptureSession(Arrays.asList(surface
                    , imageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
                    {
                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            // 如果摄像头为null，直接结束方法
                            if (null == cameraDevice) {
                                return;
                            }

                            // 当摄像头已经准备好时，开始显示预览
                            captureSession = cameraCaptureSession;
                            try {
                                // 设置自动对焦模式
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // 设置自动曝光模式
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                                // 开始显示相机预览
                                previewRequest = previewRequestBuilder.build();
                                // 设置预览时连续捕获图像数据
                                captureSession.setRepeatingRequest(previewRequest,
                                        null, null);  // ④
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(SosActivity.this, "配置失败！"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private static int i=0;
    private void setUpCameraOutputs(int width, int height) {

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            // 获取指定摄像头的特性
            CameraCharacteristics characteristics
                    = manager.getCameraCharacteristics(mCameraId);
            // 获取摄像头支持的配置属性
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            // 获取摄像头支持的最大尺寸
            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
            // 创建一个ImageReader对象，用于获取摄像头的图像数据
            imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.JPEG, 2);
            imageReader.setOnImageAvailableListener(
                    new ImageReader.OnImageAvailableListener() {
                        // 当照片数据可用时激发该方法
                        @Override
                        public void onImageAvailable(ImageReader reader) {

                            // 获取捕获的照片数据
                            Image image = reader.acquireNextImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.remaining()];
                            // 使用IO流将照片写入指定文件
                            File file = new File(getExternalFilesDir(null), "pic"+i+".png");
                            buffer.get(bytes);
                            if(i<3) {
                                i++;
                            }
                            try (
                                    FileOutputStream output = new FileOutputStream(file)) {
                                    output.write(bytes);
                                    Toast.makeText(SosActivity.this, "保存: " + file, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                image.close();
                            }
                        }
                    }, null);

            // 获取最佳的预览尺寸
            previewSize = chooseOptimalSize(map.getOutputSizes(
                    SurfaceTexture.class), width, height, largest);
            // 根据选中的预览尺寸来调整预览组件（TextureView的）的长宽比
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(
                        previewSize.getWidth(), previewSize.getHeight());
            } else {
                textureView.setAspectRatio(
                        previewSize.getHeight(), previewSize.getWidth());
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d(TAG, "出现错误");
        }
    }

    private static Size chooseOptimalSize(Size[] choices
            , int width, int height, Size aspectRatio) {
        // 收集摄像头支持的打过预览Surface的分辨率
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的。
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            System.out.println("找不到合适的预览尺寸！！！");
            return choices[0];
        }
    }

    // 为Size定义一个比较器Comparator
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // 强转为long保证不会发生溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }


}
