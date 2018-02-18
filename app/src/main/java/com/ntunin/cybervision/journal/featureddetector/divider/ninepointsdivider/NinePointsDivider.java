package com.ntunin.cybervision.journal.featureddetector.divider.ninepointsdivider;

import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.errno.ErrCodes;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;
import com.ntunin.cybervision.journal.featureddetector.divider.Divider;
import com.ntunin.cybervision.journal.featureddetector.divider.DividerDelegate;

import math.intsize.Size;

/**
 * Created by nikolay on 12.03.17.
 */

public class NinePointsDivider extends Divider {
    private ImageFrame frame;
    private int x, y;
    private DividerDelegate delegate;
    private int colorDistanceRim = 50;

    private static int[][][] U = new int[][][]{
            new int[][]{
                    new int[]{0,1,7,3,5,2,6}, new int[]{1,0,3,7,4,2,6}, new int[]{1,3,0,4,5,7,2,6}, new int[]{3,4,1,5,0,2,6}, new int[]{4,3,5,1,7,2,6}, new int[]{5,4,3,7,0,2,6}, new int[]{5,7,4,0,3,1,2,6}, new int[]{7,0,5,1,4,2,6}
            },
            new int[][]{
                    new int[]{0,1,2,6,5,3,7}, new int[]{1,2,0,4,6,3,7}, new int[]{2,1,4,0,5,3,7}, new int[]{2,4,1,5,0,6,3,7}, new int[]{4,5,2,6,1,3,7}, new int[]{5,4,6,2,0,3,7}, new int[]{6,5,4,0,2,3,7}, new int[]{6,0,5,1,2,4,3,7}
            },
            new int[][]{
                    new int[]{1,7,2,6,3,5,0,4}, new int[]{1,2,3,7,6,0,4}, new int[]{2,3,1,5,7,0,4}, new int[]{3,2,1,5,6,0,4}, new int[]{3,5,2,6,1,7,0,4}, new int[]{5,6,3,7,2,0,4}, new int[]{6,5,7,3,1,0,4}, new int[]{7,6,5,1,2,0,4}
            },
            new int[][]{
                    new int[]{0,7,2,6,3,1,5}, new int[]{2,0,3,7,4,6,1,5}, new int[]{2,3,4,0,7,1,5}, new int[]{3,2,4,0,6,1,5}, new int[]{4,3,2,6,7,1,5}, new int[]{4,6,3,7,2,0,1,5}, new int[]{6,7,4,0,3,1,5}, new int[]{7,6,0,4,2,1,5}
            },

    };

    private static int[] D = new int[]{-1, 2, 3, 2, 0, 3, 0, 3, 1, 3, 0, 2, 0, 0, 0, 3, -1, 0, 1, 0, 2, 3, 2, 3, 3, 0, 0, 3, 3, 3, 0,0, };

    private static int[][] O = new int[][] {
            new int[]{1, 0},
            new int[]{1, -1},
            new int[]{0, -1},
            new int[]{-1, -1},
            new int[]{-1, 0},
            new int[]{-1, 1},
            new int[]{0, 1},
            new int[]{1, 1},
            new int[]{0, 0}
    };

    private int[] counts = new int[8];



    @Override
    public void handle(int startX, int startY) {
        if(this.frame == null || this.delegate == null){
            ERRNO.write(ErrCodes.NOT_INITIALIZED);
            return;
        }
        int dir = delegate.getDirection();
        x = startX; y = startY;
        Size size = frame.size();
        while(true) {
            boolean success = delegate.addPoint(x, y);
            if(!success) {
                int a = 0;
                return;
            }
            if(x == 0 || x >= size.width -1 || y == 0 || y >= size.height -1) {
                break;
            }
            int divide = 0;
            int max = 0;
            int b = frame.getBrightness(x, y);
            for(int i = 0; i < 8; i++) {
                int b1 = getBrightness(O[i]);
                counts[i] = (Math.abs(b1 - b) > colorDistanceRim)? 1: 0;
            }

            int h = Math.max(counts[1] + counts[2] + counts[3], counts[5] + counts[6] + counts[7]);
            int v = Math.max(counts[1] + counts[0] + counts[7], counts[3] + counts[4] + counts[5]);

            if(h == v) {
                //Diagonal priority
                if(counts[3] + counts[7] > counts[1] + counts[5]) {
                    divide = 3;
                } else {
                    divide = 1;
                }

            } else {
                if(h > v) {
                    divide = 0;
                } else {
                    divide = 2;
                }
            }

            int[] candidates = U[divide][dir];

            int min = 1000;
            for(int i = 0; i < candidates.length; i++) {
                divide = getDistance(b, candidates[i]);
                if(divide < colorDistanceRim) {
                    dir = candidates[i];
                    break;
                }
                if(divide < min) {
                    min = divide;
                    dir = candidates[i];
                }
            }

            int[] o = O[dir];
            x+=o[0];
            y+=o[1];

        }

    }

    int getBrightness(int[] o) {
        return frame.getBrightness(x + o[0], y + o[1]);
    }

    int getDistance(int b1, int d) {
        int[] o = O[d];
        int b2 = frame.getBrightness(x + o[0], y + o[1]);
        return Math.abs(b1 - b2);
    }



    @Override
    public NinePointsDivider init(Object... args) {
        if(args.length == 3) {
            this.frame = (ImageFrame) args[0];
            this.delegate = (DividerDelegate) args[1];
        }
        return this;
    }
}
