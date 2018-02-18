package com.ntunin.cybervision.activity;

import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVActor;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;

import math.latlng.LatLng;
import math.vector.Vector3;
import whereareyou.ntunin.com.whereareyou.Friend;
import whereareyou.ntunin.com.whereareyou.R;

/**
 * Created by nik on 21.06.17.
 */

public class Attraction implements Injectable{
    private String description;
    private String name;
    private CRVActor actor;
    private Friend friend;
    private LatLng global;

    @Override
    public void init(ResMap<String, Object> data) {
        ResMap<String, Object> argument = (ResMap<String, Object>) data.get("argument");
        global = (LatLng) data.get("globalOffset");
        this.actor = (CRVActor) argument.get("actor");
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public void prepare() {
        this.actor.prepare();
    }

    public void present() {
        double[] buf = new double[2];
        global.offsetFromLatLng(buf, friend.getLat(), friend.getLng());
        CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        Vector3 position = (Vector3) factory.get(R.string.vector3).init((float)buf[0], (float)0, (float)buf[1]);
        CRVBody body = actor.getBody();
        body.setR(position);
        this.actor.play();
    }

    public Attraction clone() {
        Attraction c = new Attraction();
        c.actor = actor.clone();
        c.global = global;
        return  c;
    }
}
