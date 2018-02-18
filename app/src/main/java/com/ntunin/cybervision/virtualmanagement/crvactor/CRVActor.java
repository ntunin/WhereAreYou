package com.ntunin.cybervision.virtualmanagement.crvactor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;
import com.ntunin.cybervision.io.xfile.XFile;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;
import com.ntunin.cybervision.virtualmanagement.crvmotion.CRVPitch;
import com.ntunin.cybervision.virtualmanagement.crvmotion.CRVRoll;
import com.ntunin.cybervision.virtualmanagement.crvmotion.CRVScale;
import com.ntunin.cybervision.virtualmanagement.crvmotion.CRVTransition;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvmotion.CRVTranslation;
import com.ntunin.cybervision.virtualmanagement.crvmotion.CRVYaw;

import javax.microedition.khronos.opengles.GL10;

import math.vector.Vector3;

/**
 * Created by nikolay on 17.10.16.
 */

public class CRVActor implements Injectable{
    private List<CRVTransition> transitions;
    private CRVBody body;
    private CRVSkin skin;
    private String skinName;
    private GL10 gl;

    public CRVActor() {
    }

    public CRVActor(CRVBody body, CRVSkin dress) {
        this.body = body;
        this.skin = dress;
    }

    public void setTransitions(List<CRVTransition> transitions) {
        this.transitions = transitions;
    }

    public void play() {
        if(skin == null) {
            prepare();
        }
        gl.glPushMatrix();
        for(CRVTransition t: transitions) {
            t.act(body);
        }
        skin.draw();
        gl.glPopMatrix();
    }

    public CRVSkin getSkin() {
        return skin;
    }

    public CRVBody getBody() {
        return body;
    }

    public List<CRVTransition> getTransitions() {
        return transitions;
    }

    public void prepare() {
        this.skin = XFile.loadFrame(skinName);
        gl = (GL10) CRVContext.get(R.string.gl);
    }

    @Override
    public void init(ResMap<String, Object> data) {
        ResMap<String, Object> argument = (ResMap<String, Object>) data.get("argument");
        this.body = getBody(argument, data);
        this.skinName = (String) argument.get("skin");
        this.transitions = (List<CRVTransition>) data.get("transitions");
    }

    CRVBody getBody(ResMap<String, Object> argument, ResMap<String, Object> data) {
        Vector3 position = getVector((Map<String, Object>) argument.get("position"), data);
        Vector3 rotation = getVector((Map<String, Object>) argument.get("rotation"), data);
        Vector3 scale = getVector((Map<String, Object>) argument.get("scale"), data);
        CRVBody body = new CRVBody();
        if(position != null) {
            body.setR(position);
        }
        if(rotation != null) {
            body.setYaw(rotation.x);
            body.setPitch(rotation.y);
            body.setRoll(rotation.z);
        }
        if(scale != null) {
            body.setScale(scale);
        }
        return body;
    }


    Vector3 getVector(Map<String, Object> v, ResMap<String, Object> data) {
        if(v == null) return null;
        CRVObjectFactory factory = (CRVObjectFactory) data.get(R.string.object_factory);
        float x = (float)(double) v.get("x");
        float y = (float)(double) v.get("y");
        float z = (float)(double) v.get("z");
        Vector3 position = (Vector3) factory.get(R.string.vector3).init(x, y, z);
        return position;
    }

    public CRVActor clone() {
        CRVActor actor = new CRVActor();
        actor.body = body.clone();
        actor.skin = skin;
        actor.transitions = transitions;
        actor.skinName = skinName;
        actor.gl = gl;
        return actor;
    }




}
