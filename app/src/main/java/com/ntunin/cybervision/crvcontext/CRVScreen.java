package com.ntunin.cybervision.crvcontext;

public abstract class CRVScreen {

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
