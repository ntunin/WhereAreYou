package math.intsize;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.crvobjectfactory.ReleasableFactory;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;

/**
 * Created by nikolay on 11.03.17.
 */

public class IntSizeFactory extends ReleasableFactory implements Injectable {

    @Override
    protected Releasable create() {
        return new Size();
    }

    @Override
    protected String getTag() {
        return "Int Size";
    }

    @Override
    public void init(ResMap<String, Object> data) {
        return;
    }
}
