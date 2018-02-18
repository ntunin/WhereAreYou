package com.ntunin.cybervision.io.xfile.xstreamreader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.errno.ERRNO;

import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.io.FileIO;

/**
 * Created by Николай on 30.07.2016.
 */
public class XTextFileReader extends XStringStreamReader {

    public XTextFileReader(String path) {
        super("");
        try {
            FileIO io = (FileIO) CRVInjector.main().getInstance(R.string.io);
            InputStream stream = io.readAsset (path);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            String content = new String(buffer);
            this.stream = new StringBuilder(content);

        } catch (FileNotFoundException e) {
            ERRNO.write("File not found");
        } catch (IOException e) {
            ERRNO.write("Cannot read file");
        } catch (Exception e) {
            ERRNO.write("Exception");
        }
    }
}
