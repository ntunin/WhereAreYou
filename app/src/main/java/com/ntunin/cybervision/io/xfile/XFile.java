package com.ntunin.cybervision.io.xfile;

import java.util.HashMap;
import java.util.Map;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextFileReader;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nik on 19.06.16.
 */
public class XFile {
    private static Map<String, XFactory> factories;
    public static CRVSkin loadFrame(String path){
        factories = loadFactories();
        XTextStreamReader reader = new XTextFileReader(path);
        return loadWithReader(reader);
    }
    private static Map<String, XFactory> loadFactories() {
        Map<String, XFactory> factories = new HashMap<String, XFactory>();
        factories.put("txt", new XText());
        return factories;
    }
    private static CRVSkin loadWithReader(XTextStreamReader reader) {
        String magicWord = reader.getString(4);
        int formatVersionNumber = Integer.parseInt( reader.getString(2) );
        int formatSubVersionNumber = Integer.parseInt( reader.getString(2) );
        String formatType =reader.getString(4).trim();
        int floatSize = Integer.parseInt( reader.getString(4) );
        XFactory factory = factories.get(formatType);
        return factory.loadFrame(reader);
    }

}
