package com.ntunin.cybervision.journal.cameracapturing;

import android.graphics.Color;

import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.errno.ErrCodes;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.io.FileIO;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikolay on 12.03.17.
 */

public class FileCapturing extends JournalingCameraCapturing {

    private ImageFrame frame;
    private String src;
    private CRVObjectFactory factory;

    public FileCapturing init(String src, CRVObjectFactory factory) {
        this.src = src;
        this.factory = factory;
        return this;
    }

    public void start() {
        this.frame = getFrame();
        if(this.frame == null) {
            return;
        }
        handleFrame(this.frame);
    }

    private ImageFrame getFrame() {
        try {

            FileIO io = (FileIO) CRVInjector.main().getInstance(R.string.io);
            InputStream in = io.readAsset(src);
            if(in == null) {
                return null;
            }
            ImageFrame frame = createFrame(in);
            return frame;
        } catch (IOException e) {
            ERRNO.write(ErrCodes.COULD_NOT_READ_FILE);
            return null;
        }
    }

    private ImageFrame createFrame(InputStream in) {
        List<List<Integer>> data = new LinkedList<List<Integer>>();

        try {
            List<Integer> row = new LinkedList<>();
            StringBuilder builder = new StringBuilder();
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            int x = 0;
            int y = 0;
            for(int i = 0; i < buffer.length; i++) {
                char c = (char) buffer[i];
                switch (c) {
                    case '\n': {
                        String v = builder.toString();
                        builder = new StringBuilder();
                        int color = Integer.parseInt(v, 16);
                        row.add(color);
                        data.add(row);
                        row = new LinkedList<>();
                        x = 0;
                        y++;
                        break;
                    }
                    case  ' ': {
                        String v = builder.toString();
                        builder = new StringBuilder();
                        int color = Integer.parseInt(v, 16);
                        row.add(color);
                        x++;
                        break;
                    }
                    case '\uFFFF': {
                        break;
                    }
                    default: {
                        builder.append(c);
                    }
                }
            }
            String v = builder.toString();
            int color = Integer.parseInt(v, 16);
            row.add(color);

            data.add(row);

        } catch (Exception e) {
            assert false;
        }

        int height = data.size();
        int width = data.get(0).size();
        ImageFrame frame = (ImageFrame) factory.get(R.string.image_frame).init(width, height);
        int y = 0;
        while(data.size() > 0) {
            List<Integer> row = data.get(0);
            data.remove(0);
            int x = 0;
            while(row.size() > 0) {
                int pixel = row.get(0);
                row.remove(0);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                frame.put(x, y, r, g, b);
                x++;
            }
            y++;
        }
        return frame;
    }

    public void stop() {

    }

    @Override
    public void init(ResMap<String, Object> data) {
        super.init(data);
        factory = (CRVObjectFactory) data.get(R.string.object_factory);
        this.src = (String) data.get(R.string.test_file);
    }

}
