package com.ntunin.cybervision.journal.featureddetector.pointfetcher.nautilus;

import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.errno.ErrCodes;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;
import com.ntunin.cybervision.journal.featureddetector.divider.Divider;
import com.ntunin.cybervision.journal.featureddetector.divider.DividerDelegate;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.PointFetcher;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge.Edge;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge.EdgeNode;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge.EdgeRegister;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge.EdgeRoot;

import math.intpoint.Point;
import math.intsize.Size;

/**
 * Created by nikolay on 11.03.17.
 */

public class Nautilus extends PointFetcher {
    private final double GOLDEN_SECTION = 1.62f;
    private  final double PI = Math.PI;
    private  final double _2_PI = 2/PI;

    @Override
    public void release() {
        super.release();
    }

    Size size;


    int halfScreenX;
    int halfScreenY;



    double current;
    double currentSqr;
    double target = 6.86;
    double targetSqr = 6.86 * 6.86;
    int frameDirection = 1;

    double rCircle;
    double oXCircle;

    double xStart;
    double xFinish;

    double alpha = 0;
    double dAlpha;

    int x;
    int y;

    int minSize;
    Edge edge;
    EdgeRegister table;


    public Nautilus() {
        super();
    }

    @Override
    public EdgeRegister start(ImageFrame frame) {
        reset();
        final CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        if(frame == null) {
            ERRNO.write(ErrCodes.INVALID_ARGUMENT);
            return null;
        }
        if(factory == null) {
            ERRNO.write(ErrCodes.NOT_INITIALIZED);
            return null;
        }
        super.start(frame);
        this.size = frame.size();
        this.table = (EdgeRegister) factory.get(R.string.edge_register).init(frame.size());
        divider = (Divider) factory.get(R.string.divider).init(frame, new DividerDelegate() {
            @Override
            public boolean addPoint(int x, int y) {
                if(x <= 0 || y <= 0 || x >= size.width - 1 || y >= size.height - 1) {
                    edge = null;
                    return false;
                }

                int hash = y*size.width + x;
                Point p = (Point) factory.get(R.string.int_point).init(x, y);




                x = x - halfScreenX;
                y = y - halfScreenY;
                int sqr = x*x + y*y;
                if(sqr < currentSqr || sqr > targetSqr) {
                    edge = null;
                    return false;
                }

                EdgeRoot root = table.readRoot(p);
                if(root != null) {
                    if(edge == null) {
                        return false;
                    }
                    edge.push(root.getEdge());
                    table.clearCache();
                    edge = null;
                    return false;
                }
                EdgeNode node = table.readNode(p);
                if(node != null) {
                    if(edge == null) {
                        return false;
                    }
                    Edge e = table.edgeFor(p);
                    if(e != null) {
                        e.split(p);
                        table.clearCache();
                    }
                    edge = null;
                    return false;
                }

                if(edge == null) {
                    edge = (Edge) factory.get(R.string.edge).init(table);
                }
                edge.push(p);

                return true;
            }

            @Override
            public int getDirection() {
                return 0;
            }
        });
        halfScreenX = size.width  / 2;
        halfScreenY = size.height / 2;

        minSize = Math.min(halfScreenX, halfScreenY);
        while(true) {
            current = target;
            currentSqr = current * current;
            target = current * GOLDEN_SECTION;
            targetSqr = target * target;
            rCircle = Math.abs(target + current) / 2;



            xStart = frameDirection * current;
            frameDirection *= -1;
            xFinish = frameDirection * target;

            oXCircle = Math.min(xStart, xFinish) + rCircle;

            dAlpha = 2 / rCircle;
            for(alpha = 0; alpha < PI; alpha += dAlpha) {
                double p = _2_PI * alpha - 1;
                int s = (int) Math.signum(p);
                p = 1 - Math.abs(p);
                x = (int) (rCircle * s * (1 - p*p) + oXCircle) + halfScreenX;
                y = (int) (rCircle * p * frameDirection) + halfScreenY;
                divider.handle(x, y);
                if(x == 0 || y == 0 || x == size.width - 1 || y == size.height - 1) {
                    divider.release();
                    return table;
                }
            }
        }
    }

    private void reset() {
        size = null;

        halfScreenX = 0;
        halfScreenY = 0;

        current = 0;
        currentSqr = 0;
        target = 6.86;
        targetSqr = 6.86 * 6.86;
        frameDirection = 1;

        rCircle = 0;
        oXCircle = 0;

        xStart = 0;
        xFinish = 0;

        alpha = 0;
        dAlpha = 0;

        x = 0;
        y = 0;

        minSize = 0;
        edge = null;
        table = null;
    }

}
