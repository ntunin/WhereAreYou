package com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvinjector.CRVInjector;

/**
 * Created by nikolay on 31.08.16.
 */

public class CRVSkin {
    GL10 gl;
    FloatBuffer vertices;
    ShortBuffer indices;
    FloatBuffer uv;
    FloatBuffer normals;
    Texture texture;
    String name;
    List<CRVSkin> children;
    int numberOfVertices;
    int numberOfIndices;
    Material defaultMaterial;
    float[] objectMatrix;
    float[] relativeMatrix;

    private String id;

    public CRVSkin(String name) {
        gl = (GL10) CRVInjector.main().getInstance(R.string.gl);
        this.name = name;
        children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void addFrame(CRVSkin child) {
        children.add(child);
    }

    public void setIndices(short[] indices) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(indices.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        this.indices = byteBuffer.asShortBuffer();
        this.indices.put(indices);
        this.indices.flip();
        numberOfIndices = indices.length;
    }

    public void setVertices(float[] vertices) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 3 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        this.vertices = byteBuffer.asFloatBuffer();
        this.vertices.put(vertices);
        this.vertices.flip();
        numberOfVertices = vertices.length;
    }
    public void setNormals(float[] normals) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(normals.length * 3 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        this.normals = byteBuffer.asFloatBuffer();
        this.normals.put(normals);
        this.normals.flip();
    }
    public void setUV(float[] uv) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(uv.length * 2 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        this.uv = byteBuffer.asFloatBuffer();
        this.uv.put(uv);

    }

    public void setDefaultMaterial(Material defaultMaterial) {
        this.defaultMaterial = defaultMaterial;
    }

    public void setObjectMatrix(float[] objectMatrix) {
        this.objectMatrix = objectMatrix;
    }

    public void setRelativeMatrix(float[] relativeMatrix) {
        this.relativeMatrix = relativeMatrix;
    }

    public void draw() {
        setupObjectMatrix();
        setupRelativeMatrix();
        setupMaterial();
        bindVertices();
        bindTextures();
        bindNormals();
        drawMesh();
        if(objectMatrix != null) gl.glPopMatrix();
        drawChildren();
        if(relativeMatrix != null) gl.glPopMatrix();

    }

    private void drawMesh() {
        if(indices == null) {
            drawPoints();
        } else {
            drawFaces();
        }
    }

    private void drawChildren() {
        for(CRVSkin child: children) {
            child.draw();
        }
    }

    private void drawFaces() {
        if(indices == null) return;
        gl.glDrawElements(GL10.GL_TRIANGLES, numberOfIndices, GL10.GL_UNSIGNED_SHORT, indices);
    }
    private  void drawPoints() {
        if(vertices == null) return;
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numberOfVertices);
    }
    private  void bindVertices () {
        if(vertices == null) return;
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 3 * 4, vertices);
    }
    private void bindTextures() {
        if(texture != null) {
            texture.bind();
        }
        if (uv != null) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            uv.position(0);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 2 * 4, uv);
        }

    }

    private void bindNormals() {
        if(normals == null) return;
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        normals.position(0);
        gl.glNormalPointer(GL10.GL_FLOAT, 3 * 4, normals);
    }

    private void setupObjectMatrix() {
        if(objectMatrix == null) return;
        gl.glPushMatrix();
        gl.glMultMatrixf(objectMatrix, 0);
    }
    private void setupRelativeMatrix() {
        if(relativeMatrix == null) return;
        gl.glPushMatrix();
        gl.glMultMatrixf(relativeMatrix, 0);
    }

    private void setupMaterial() {
        if(defaultMaterial == null) return;
        defaultMaterial.enable(gl);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
