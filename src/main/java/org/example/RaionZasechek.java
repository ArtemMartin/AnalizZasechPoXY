package org.example;

public class RaionZasechek {
    double x;
    double y;
    int count;

    public RaionZasechek(double x, double y, int count) {
        this.x = x;
        this.y = y;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Р-н засечек " +
                "X = " + x +
                ", Y = " + y +
                ", к-во засечек в районе = " + count;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getCount() {
        return count;
    }
}
