package math.intpoint;

import com.ntunin.cybervision.crvobjectfactory.Releasable;

/**
 * Created by nikolay on 11.03.17.
 */

public class Point extends Releasable{
    public int x;
    public int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public void release() {
        super.release();
        x = 0;
        y = 0;
    }

    @Override
    public Point init(Object... args) {
        if(args.length == 2) {
            x = (int) args[0];
            y = (int) args[1];
        }
        return this;
    }
}
