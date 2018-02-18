package com.ntunin.cybervision.io.xfile;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nik on 20.06.16.
 */
public interface XFactory {
    CRVSkin loadFrame(XTextStreamReader reader);
}
