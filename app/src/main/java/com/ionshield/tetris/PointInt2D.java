package com.ionshield.tetris;

public class PointInt2D {
    public int x;
    public int y;

    public PointInt2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(PointInt2D b) {
        if (b == this) {
            return true;
        }
        if (b == null) {
            return false;
        }
        return this.x == b.x && this.y == b.y;
    }

    public PointInt2D plus(PointInt2D b) {
        return new PointInt2D(this.x + b.x, this.y + b.y);
    }

    public PointInt2D minus(PointInt2D b) {
        return new PointInt2D(this.x - b.x, this.y - b.y);
    }

    public PointInt2D minus() {
        return new PointInt2D(-x, -y);
    }

    public PointInt2D rotateCW() {
        return new PointInt2D(+y, -x);
    }

    public PointInt2D rotateCCW() {
        return new PointInt2D(-y, +x);
    }

}
