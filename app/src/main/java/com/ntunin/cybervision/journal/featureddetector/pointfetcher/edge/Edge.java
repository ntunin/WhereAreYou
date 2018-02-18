package com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge;

import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.errno.ErrCodes;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.crvinjector.CRVInjector;

import java.util.LinkedList;
import java.util.List;

import math.intpoint.Point;

/**
 * Created by nikolay on 12.03.17.
 */

public class Edge extends Releasable {
    private CRVObjectFactory factory;
    private EdgeRoot root;
    private EdgeNode first;
    private EdgeNode last;
    private EdgeRegister table;
    private int size = 0;

    public void push(Point point) {
        if(factory == null) {
            ERRNO.write(ErrCodes.NOT_INITIALIZED);
            return;
        }
        EdgeNode node = (EdgeNode) factory.get(R.string.edge_node).init(point);
        table.writeNode(node);
        if(this.root == null) {
            this.root = (EdgeRoot) factory.get(R.string.edge_root).init(point, this);
            table.writeRoot(root);
            this.first = node;
            this.last = node;
            this.first.next = node;
        } else {
            this.last.push(node);
            this.last = node;
        }
    }

    public Point getFirst() {
        return first.point;
    }

    public Point getLast() {
        return last.point;
    }

    public Edge split(Point point) {
        if(table == null) {
            ERRNO.write(ErrCodes.NOT_INITIALIZED);
            return null;
        }

        EdgeNode node = table.readNode(point);
        if(node == null) {
            return null;
        } else if(node.prev == null) {
            return this;
        } else {
            table.removeNode(point);
            Edge second = (Edge) factory.get("Edge").init(table, node);
            node.next = null;
            second.last = this.last;
            this.last = node;
            return second;
        }
    }

    public void push(Edge edge) {
        this.last.next = edge.first;
        edge.first.prev = this.last;
        this.last = edge.last;
        table.removeRoot(edge.root.getPoint());
    }


    public void iterate(EdgeIterator i) {
        EdgeNode node = first;
        List<EdgeNode> way = new LinkedList<>();
        do {
            if(node == null || way.indexOf(node) >= 0) return;
            i.handle(node.point);
            node = node.next;
            way.add(node);
        } while(node != last);
        i.handle(node.point);
    }


    @Override
    public Releasable init(Object... args) {
        CRVInjector injector = CRVInjector.main();
        this.factory = (CRVObjectFactory) injector.getInstance(R.string.object_factory);
        if(args.length >= 1) {
            table = (EdgeRegister) args[0];
        }
        if(args.length >= 2) {
            initFromNode((EdgeNode) args[1]);
        }
        return this;
    }

    private void initFromNode(EdgeNode node) {
        Point point = node.point;
        this.root = (EdgeRoot) factory.get(R.string.edge_root).init(point, this);
        root.link(this);
        table.writeRoot(root);
        this.first = (EdgeNode) factory.get(R.string.edge_node).init(point);
        this.first.next = node.next;
    }
}
