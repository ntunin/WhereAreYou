package com.ntunin.cybervision.android.io;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.res.AssetManager;
import android.os.Environment;

import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.io.FileIO;
import com.ntunin.cybervision.res.ResMap;


public class CRVAndroidFileIO implements FileIO, Injectable {
    AssetManager assets;
    String externalStoragePath;

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }
    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }
    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }

    @Override
    public void init(ResMap<String, Object> data) {
        this.assets = CRVContext.current().getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
    }
}