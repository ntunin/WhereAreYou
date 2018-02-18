package math.intsize;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;

/**
 * Created by nikolay on 11.03.17.
 */

public class Size extends Releasable implements Injectable{
    public int width;
    public int height;

    @Override
    public void release() {
        super.release();
        width = 0;
        height = 0;
    }

    @Override
    public Size init(Object... args) {
        if(args.length == 2) {
            this.width = (int) args[0];
            this.height = (int) args[1];
        }
        return this;
    }

    @Override
    public void init(ResMap<String, Object> data) {

    }
}
