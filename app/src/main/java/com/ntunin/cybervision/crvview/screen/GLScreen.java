package com.ntunin.cybervision.crvview.screen;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVScreen;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.res.ResMap;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mikhaildomrachev on 18.04.17.
 */

public class GLScreen extends CRVScreen implements Injectable {
    GL10 gl;

    @Override
    public void resume() {
        gl = (GL10) CRVInjector.main().getInstance(R.string.gl);
    }

    @Override
    public void update(float deltaTime) {
    }
    @Override
    public void present(float deltaTime) {
        clean();
    }

    private void clean() {
        gl.glClearColor(0, 0f, 0f, 0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(ResMap<String, Object> data) {
    }
}
