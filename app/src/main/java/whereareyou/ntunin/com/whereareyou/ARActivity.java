package whereareyou.ntunin.com.whereareyou;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ntunin.cybervision.activity.CRVStackViewActivity;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvcontext.CRVScreen;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.errno.ErrorListener;


/**
 * Created by nik on 29.04.17.
 */

public class ARActivity extends CRVStackViewActivity implements ErrorListener {

    private RelativeLayout progressView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ERRNO.subscribe(new int[]{
                R.string.no_accelerometer,
                R.string.no_gyroscope,
                R.string.camera_connection_error,
                R.string.camera_not_granted,
                R.string.not_init
        }, this);
        progressView = (RelativeLayout) findViewById(R.id.progressView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();

        FloatingActionButton back = (FloatingActionButton)findViewById(R.id.backToMap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected CRVScreen getScreen() {
        MyGameScreen screen = (MyGameScreen) CRVContext.get("hardSyncScreen");
        screen.setTestContext(this);
        return screen;
    }

    @Override
    public void onError(String error) {
        showLongToast(error);
    }

    private void showLongToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onPrepareProgress(int value) {
        progressBar.setProgress(value);
        if(100 - value <= 1 && progressView != null) {
            ((ViewGroup)progressView.getParent()).removeView(progressView);
            progressView = null;
        }
    }

}
