package acticity;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.android.learnmiwok.R;

/**
 * 伪装来电-实现响铃与震动
 */
public class CalledActivity extends AppCompatActivity {
    private ImageButton call_over;
    private ImageButton call_on;
    private ImageButton call_over_S;
    private static MediaPlayer mMediaPlayer;
    private static Vibrator vibrator;
    private RelativeLayout r_o,r_c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_safe_called);
        r_o=(RelativeLayout)findViewById(R.id.callddd);
        r_c=(RelativeLayout)findViewById(R.id.calling);

        call_over=(ImageButton)findViewById(R.id.call_over);
        call_on=(ImageButton)findViewById(R.id.call_on);
        call_over_S=(ImageButton)findViewById(R.id.call_over_S);
        call_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        call_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopRingTone();
                r_o.setVisibility(View.INVISIBLE);
            }
        });
        call_over_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PlayRingTone(getApplicationContext(),RingtoneManager.TYPE_RINGTONE);

    }

    @Override
    protected void onStop() {
        StopRingTone();
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        StopRingTone();
        super.onDestroy();
    }

    /**
     * 伪装来电铃声
     *
     * 获取的是铃声的Uri
     * @param ctx
     * @param type
     * @return
     */
    public static Uri getDefaultRingtoneUri(Context ctx, int type) {

        return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);

    }

    /**
     * 获取的是铃声相应的Ringtone
     * @param ctx
     * @param type
     */
    public Ringtone getDefaultRingtone(Context ctx, int type) {

        return RingtoneManager.getRingtone(ctx,
                RingtoneManager.getActualDefaultRingtoneUri(ctx, type));

    }

    /**
     * 播放铃声并使手机震动
     * @param ctx
     * @param type
     */

    public static void PlayRingTone(Context ctx,int type) {

        vibrator = (Vibrator)ctx.getSystemService(ctx.VIBRATOR_SERVICE);
// 等待3秒，震动3秒，从第0个索引开始，一直循环
        vibrator.vibrate(new long[]{500, 3000}, 0);

        mMediaPlayer = MediaPlayer.create(ctx,
                getDefaultRingtoneUri(ctx, type));
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }
    public static void StopRingTone() {
        mMediaPlayer.stop();
        vibrator.cancel();
    }
}
