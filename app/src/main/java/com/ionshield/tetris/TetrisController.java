package com.ionshield.tetris;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TetrisController implements Serializable {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    public static final int HEIGHT_LIMIT = 16;
    public static final int BASE_DELAY = 1500;
    public static final double SCALING_LOW = 0;
    public static final double SCALING_MED = 0.001;
    public static final double SCALING_HIGH = 0.0025;
    public static final int LINE_SCORE = 100;
    public static final PointInt2D START_POINT = new PointInt2D(WIDTH / 2, HEIGHT - 2);


    private List<List<Cell>> cells = new ArrayList<>();
    private PointInt2D shapePosition;
    private Shape shape;
    private Shape nextShape;
    private int score = 0;
    private boolean canMove = true;
    private String difficulty;

    public TetrisController(String difficulty) {
        this.difficulty = difficulty;
        for (int i = 0; i < HEIGHT; i++) {
            cells.add(new ArrayList<Cell>());
            for (int j = 0; j < WIDTH; j++) {
                cells.get(i).add(new Cell());
            }
        }

        shapePosition = START_POINT;
        shape = Shape.makeRandomShape();
        nextShape = Shape.makeRandomShape();
        tryCreateShape();
    }

    public boolean takeNextShape() {
        shape = nextShape;
        nextShape = Shape.makeRandomShape();
        shapePosition = START_POINT;
        return tryCreateShape();
    }

    public Shape getShape() {
        return shape;
    }

    public Shape getNextShape() {
        return nextShape;
    }

    public PointInt2D getShapePosition() {
        return shapePosition;
    }

    public Cell get(PointInt2D point) {
        return get(point.x, point.y);
    }

    public Cell get(int x, int y) {
        return cells.get(y).get(x);
    }

    public void set(PointInt2D point, Cell cell) {
        set(point.x, point.y, cell);
    }

    public void set(int x, int y, Cell cell) {
        cells.get(y).set(x, cell);
    }

    public boolean inBoundsX(int x) {
        return x >= 0 && x < WIDTH;
    }

    public boolean inBoundsY(int y) {
        return y >= 0 && y < HEIGHT;
    }

    public boolean inBounds(PointInt2D point) {
        return inBoundsX(point.x) && inBoundsY(point.y);
    }

    public boolean inLimitY(int y) {
        return y >= 0 && y < HEIGHT_LIMIT;
    }

    public boolean inShape(PointInt2D point) {
        return point.equals(shapePosition.plus(shape.p0)) || point.equals(shapePosition.plus(shape.p1)) || point.equals(shapePosition.plus(shape.p2)) || point.equals(shapePosition.plus(shape.p3));
    }

    public boolean isAvailable(PointInt2D point) {
        return inBounds(point) && (!get(point).isFull || inShape(point));
    }

    public int getScore() {
        return score;
    }

    public boolean tryCreateShape() {
        PointInt2D p0 = shapePosition.plus(shape.p0);
        PointInt2D p1 = shapePosition.plus(shape.p1);
        PointInt2D p2 = shapePosition.plus(shape.p2);
        PointInt2D p3 = shapePosition.plus(shape.p3);

        if (inBounds(p0) && !get(p0).isFull && inBounds(p1) && !get(p1).isFull && inBounds(p2) && !get(p2).isFull && inBounds(p3) && !get(p3).isFull) {
            get(p0).isFull = true;
            get(p0).fullColor = shape.shapeColor;
            get(p1).isFull = true;
            get(p1).fullColor = shape.shapeColor;
            get(p2).isFull = true;
            get(p2).fullColor = shape.shapeColor;
            get(p3).isFull = true;
            get(p3).fullColor = shape.shapeColor;
            return true;
        }
        return false;
    }

    public boolean tryMoveBy(PointInt2D vector) {
        if (!canMove) {
            return false;
        }
        PointInt2D p0 = shapePosition.plus(shape.p0);
        PointInt2D p1 = shapePosition.plus(shape.p1);
        PointInt2D p2 = shapePosition.plus(shape.p2);
        PointInt2D p3 = shapePosition.plus(shape.p3);

        if (isAvailable(p0.plus(vector)) && isAvailable(p1.plus(vector)) && isAvailable(p2.plus(vector)) && isAvailable(p3.plus(vector))) {
            get(p0).isFull = false;
            get(p1).isFull = false;
            get(p2).isFull = false;
            get(p3).isFull = false;

            get(p0.plus(vector)).isFull = true;
            get(p0.plus(vector)).fullColor = shape.shapeColor;
            get(p1.plus(vector)).isFull = true;
            get(p1.plus(vector)).fullColor = shape.shapeColor;
            get(p2.plus(vector)).isFull = true;
            get(p2.plus(vector)).fullColor = shape.shapeColor;
            get(p3.plus(vector)).isFull = true;
            get(p3.plus(vector)).fullColor = shape.shapeColor;

            shapePosition = shapePosition.plus(vector);
            return true;
        }
        return false;
    }

    public boolean tryMoveDown() {
        return tryMoveBy(new PointInt2D(0, -1));
    }

    public boolean tryMoveUp() {
        return tryMoveBy(new PointInt2D(0, 1));
    }

    public boolean tryMoveLeft() {
        return tryMoveBy(new PointInt2D(-1, 0));
    }

    public boolean tryMoveRight() {
        return tryMoveBy(new PointInt2D(1, 0));
    }

    public boolean tryRotateCWAround(PointInt2D offset) {
        if (!canMove) {
            return false;
        }
        PointInt2D p0 = shapePosition.plus(shape.p0);
        PointInt2D p1 = shapePosition.plus(shape.p1);
        PointInt2D p2 = shapePosition.plus(shape.p2);
        PointInt2D p3 = shapePosition.plus(shape.p3);

        PointInt2D r0 = shape.p0.minus(offset).rotateCW().plus(offset).plus(shapePosition);
        PointInt2D r1 = shape.p1.minus(offset).rotateCW().plus(offset).plus(shapePosition);
        PointInt2D r2 = shape.p2.minus(offset).rotateCW().plus(offset).plus(shapePosition);
        PointInt2D r3 = shape.p3.minus(offset).rotateCW().plus(offset).plus(shapePosition);

        if (isAvailable(r0) && isAvailable(r1) && isAvailable(r2) && isAvailable(r3)) {
            get(p0).isFull = false;
            get(p1).isFull = false;
            get(p2).isFull = false;
            get(p3).isFull = false;

            get(r0).isFull = true;
            get(r0).fullColor = shape.shapeColor;
            get(r1).isFull = true;
            get(r1).fullColor = shape.shapeColor;
            get(r2).isFull = true;
            get(r2).fullColor = shape.shapeColor;
            get(r3).isFull = true;
            get(r3).fullColor = shape.shapeColor;

            shape.p0 = shape.p0.minus(offset).rotateCW().plus(offset);
            shape.p1 = shape.p1.minus(offset).rotateCW().plus(offset);
            shape.p2 = shape.p2.minus(offset).rotateCW().plus(offset);
            shape.p3 = shape.p3.minus(offset).rotateCW().plus(offset);
            return true;
        }
        return false;
    }

    public boolean tryRotateCCWAround(PointInt2D offset) {
        if (!canMove) {
            return false;
        }
        PointInt2D p0 = shapePosition.plus(shape.p0);
        PointInt2D p1 = shapePosition.plus(shape.p1);
        PointInt2D p2 = shapePosition.plus(shape.p2);
        PointInt2D p3 = shapePosition.plus(shape.p3);

        PointInt2D r0 = shape.p0.minus(offset).rotateCCW().plus(offset).plus(shapePosition);
        PointInt2D r1 = shape.p1.minus(offset).rotateCCW().plus(offset).plus(shapePosition);
        PointInt2D r2 = shape.p2.minus(offset).rotateCCW().plus(offset).plus(shapePosition);
        PointInt2D r3 = shape.p3.minus(offset).rotateCCW().plus(offset).plus(shapePosition);

        if (isAvailable(r0) && isAvailable(r1) && isAvailable(r2) && isAvailable(r3)) {
            get(p0).isFull = false;
            get(p1).isFull = false;
            get(p2).isFull = false;
            get(p3).isFull = false;

            get(r0).isFull = true;
            get(r0).fullColor = shape.shapeColor;
            get(r1).isFull = true;
            get(r1).fullColor = shape.shapeColor;
            get(r2).isFull = true;
            get(r2).fullColor = shape.shapeColor;
            get(r3).isFull = true;
            get(r3).fullColor = shape.shapeColor;

            shape.p0 = shape.p0.minus(offset).rotateCCW().plus(offset);
            shape.p1 = shape.p1.minus(offset).rotateCCW().plus(offset);
            shape.p2 = shape.p2.minus(offset).rotateCCW().plus(offset);
            shape.p3 = shape.p3.minus(offset).rotateCCW().plus(offset);
            return true;
        }
        return false;
    }

    public int clearRows() {
        int res = 0;
        for (int i = 0; i < HEIGHT; i++) {
            boolean ok = true;
            for (int j = 0; j < WIDTH; j++) {
                if (!get(j, i).isFull) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                res++;
                List<Cell> tmp = cells.get(i);
                for (int k = i; k < HEIGHT - 1; k++) {
                    cells.set(k, cells.get(k + 1));
                }
                for (int j = 0; j < WIDTH; j++) {
                    tmp.get(j).isFull = false;
                }
                cells.set(HEIGHT - 1, tmp);
                i--;
            }
        }
        return res;
    }

    public boolean step() {

        if (canMove) {
            boolean ok = tryMoveDown();
            if (ok) {
                return true;
            }
            else {
                canMove = false;
                return true;
            }
        }
        else {
            int res = clearRows();

            score += res * LINE_SCORE;

            boolean ok = takeNextShape();

            if (ok) {
                canMove = true;
                return true;
            }
            else {
                return false;
            }

        }
    }

    public int getDelay() {
        switch (difficulty) {
            case "low":
                return (int)Math.round(BASE_DELAY / (1.0 + score * SCALING_LOW));
            case "med":
                return (int)Math.round(BASE_DELAY / (1.0 + score * SCALING_MED));
            case "high":
                return (int)Math.round(BASE_DELAY / (1.0 + score * SCALING_HIGH));
        }
        return BASE_DELAY;
    }

    public static class Shape {
        public enum Type {
            I, L, J, S, Z, O, T
        }

        public int shapeColor;
        public PointInt2D p0;
        public PointInt2D p1;
        public PointInt2D p2;
        public PointInt2D p3;

        public Shape(int shapeColor, PointInt2D p0, PointInt2D p1, PointInt2D p2, PointInt2D p3) {
            this.shapeColor = shapeColor;
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        public static Shape makeRandomShape() {
            int index = (int)Math.floor(Math.random() * 7);

            return makeShape(Type.values()[index]);
        }

        public static Shape makeShape(Type type) {
            switch (type) {
                case I:
                    return new Shape(0xff8040bf, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(0, -1), new PointInt2D(0, -2));
                case L:
                    return new Shape(0xff4040bf, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(0, -1), new PointInt2D(1, -1));
                case J:
                    return new Shape(0xff40bfbf, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(0, -1), new PointInt2D(-1, -1));
                case S:
                    return new Shape(0xff40bf40, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(1, 0), new PointInt2D(1, -1));
                case Z:
                    return new Shape(0xffbfbf40, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(-1, 0), new PointInt2D(-1, -1));
                case O:
                    return new Shape(0xffbf8040, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(1, 0), new PointInt2D(1, 1));
                case T:
                    return new Shape(0xffbf4040, new PointInt2D(0, 0), new PointInt2D(0, 1), new PointInt2D(1, 0), new PointInt2D(-1, 0));
            }
            return new Shape(0xff000000, new PointInt2D(0, 0), new PointInt2D(0, 0), new PointInt2D(0, 0), new PointInt2D(0, 0));
        }
    }


    public static class Cell {
        public boolean isFull = false;
        public int fullColor = 0xffffffff;
        public int emptyColor = 0xff222222;

        public int getColor() {
            return isFull ? fullColor : emptyColor;
        }
    }
}
