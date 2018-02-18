package math.vector;

/**
 * Created by nick on 01.03.16.
 */
import android.opengl.Matrix;
import android.util.Log;

import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.res.ResMap;

public class Vector3 extends Releasable {
    private static final float[] matrix = new float[16];
    private static final float[] inVec = new float[4];
    private static final float[] outVec = new float[4];
    public float x, y, z;

    public Vector3() {
    }
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    public Vector3 cpy() {
        CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        Vector3 v = (Vector3) factory.get(R.string.vector3).init(x, y, z);
        return v;
    }
    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public Vector3 set(Vector3 other) {
        return this.set(other.x, other.y, other.z);
    }
    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    public Vector3 add(Vector3 other) {
        return this.add(other.x, other.y, other.z);
    }
    public Vector3 sub(float x, float y, float z) {
        return this.add(-x, -y, -z);
    }
    public Vector3 sub(Vector3 other) {
        return this.sub(other.x, other.y, other.z);
    }
    public Vector3 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return  this;
    }
    public float len() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    public float lenSquared() {
        return x * x + y * y + z * z;
    }
    public Vector3 nor() {
        float len = len();
        if (len != 0) {
            this.x /= len;
            this.y /= len;
            this.z /= len;
        }
        return this;
    }
    public Vector3 rotate(float angle, float axisX, float axisY,
                          float axisZ) {
        inVec[0] = x;
        inVec[1] = y;
        inVec[2] = z;
        inVec[3] = 1;
        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        x = outVec[0];
        y = outVec[1];
        z = outVec[2];
        return this;
    }
    public static Vector3 sub(Vector3 B, Vector3 A) {
        float x = B.x - A.x;
        float y = B.y - A.y;
        float z = B.z - A.z;
        CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        Vector3 v = (Vector3) factory.get(R.string.vector3).init(x, y, z);
        return v;
    }
    public float dist(Vector3 other) {
        return sub(this, other).len();
    }
    public float dist(float x, float y, float z) {
        CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        Vector3 v = (Vector3) factory.get(R.string.vector3).init(x, y, z);
        return  sub(this, v).len();
    }
    public float distSquared(Vector3 other) {
        return sub(this, other).lenSquared();
    }
    public float distSquared(float x, float y, float z) {
        CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        Vector3 v = (Vector3) factory.get(R.string.vector3).init(x, y, z);
        return sub(this, v).lenSquared();
    }

    @Override
    public String toString() {
        return new StringBuilder("{").append(x).append(", ").append(y).append(", ").append(z).append("}").toString();
    }

    @Override
    public Releasable init(Object... args) {
        if(args.length >= 3) {
            try {
                x = (float)args[0];
                y = (float)args[1];
                z = (float)args[2];
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        }
        return this;
    }

    @Override
    public void release() {
        super.release();
        x = 0;
        y = 0;
        z = 0;
    }

}
