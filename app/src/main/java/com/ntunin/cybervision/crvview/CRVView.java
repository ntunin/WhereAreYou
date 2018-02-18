package com.ntunin.cybervision.crvview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by mikhaildomrachev on 17.04.17.
 */

public abstract class CRVView extends FrameLayout {

    public CRVView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public abstract void start();
}
