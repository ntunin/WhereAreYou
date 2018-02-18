package com.ntunin.cybervision.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.crvview.CRVView;

/**
 * Created by mikhaildomrachev on 17.04.17.
 */

public abstract class CRVStackViewActivity extends CRVContext {

    private CRVView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er);
        view = (CRVView) findViewById(R.id.er_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.start();
        String error = ERRNO.last();
        if(error != null) {
            Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
            toast.show();
        }
        ERRNO.write(null);
    }

}
