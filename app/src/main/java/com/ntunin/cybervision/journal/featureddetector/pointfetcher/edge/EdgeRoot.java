package com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge;

import com.ntunin.cybervision.crvobjectfactory.Releasable;

import math.intpoint.Point;

/**
 * Created by nikolay on 28.03.17.
 */

public class EdgeRoot extends Releasable {

    Point point;

    Edge edge;


    public Point getPoint() {
        return point;
    }

    public void link(Edge edge) {

        this.edge = edge;
    }

    @Override
    public Releasable init(Object... args) {
        if(args.length == 2) {
            this.point = (Point) args[0];
            this.edge = (Edge) args[1];
        }
        return this;
    }

    public Edge getEdge() {
        return edge;
    }
}
