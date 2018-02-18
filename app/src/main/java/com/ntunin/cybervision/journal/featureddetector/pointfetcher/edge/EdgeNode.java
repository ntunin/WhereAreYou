package com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge;

import com.ntunin.cybervision.crvobjectfactory.Releasable;

import math.intpoint.Point;

/**
 * Created by nikolay on 26.03.17.
 */

public class EdgeNode extends Releasable{
    EdgeNode next;
    EdgeNode prev;
    Point point;

    void push(EdgeNode node) {
        this.next = node;
        node.prev = this;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public Releasable init(Object... args) {
        if(args.length == 1) {
            this.point = (Point) args[0];
        }
        return this;
    }
}
