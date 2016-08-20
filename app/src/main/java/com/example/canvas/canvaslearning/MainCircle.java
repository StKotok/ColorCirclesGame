package com.example.canvas.canvaslearning;

import android.graphics.Color;

public class MainCircle extends SimpleCircle {

    public static final int INIT_RADIUS = 20;
    public int MAIN_SPEED = 30;
    public static final int COLOR = Color.BLUE;

    public MainCircle(int x, int y) {
        super(x, y, INIT_RADIUS);
        setColor(COLOR);
    }

    public void moveMainCircleWhenTouchAt(int xNew, int yNew) {
        int dx = (xNew - x) * MAIN_SPEED / GameManager.getWidth();
        int dy = (yNew - y) * MAIN_SPEED / GameManager.getHeight();
        x += dx;
        y += dy;
    }

    public void initRadius() {
        radius = INIT_RADIUS;
    }

}
