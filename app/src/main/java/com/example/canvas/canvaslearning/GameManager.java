package com.example.canvas.canvaslearning;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {

    public static final int MAX_CIRCLES = 15;
    public static final int RANDOM_SPEED = 3;
    public static final int MIN_RADIUS = 7;
    private static int width;
    private static int height;
    private MainCircle mainCircle;
    private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
    private boolean collision;

    public GameManager(CanvasView c, int w, int h) {
        this.canvasView = c;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    public static EnemyCircle getRandomCircle() {
        Random random = new Random();
        int width = getWidth();
        int height = getHeight();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int size = width + height;
        int radius = size / 150 + random.nextInt(size / 50);
        int dx = 1 + random.nextInt(RANDOM_SPEED);
        int dy = 1 + random.nextInt(RANDOM_SPEED);
        EnemyCircle enemyCircle = new EnemyCircle(x, y, radius, dx, dy);
        enemyCircle.setColor(EnemyCircle.ENEMY_COLOR);
        return enemyCircle;
    }

    private void initEnemyCircles() {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        circles = new ArrayList<>();
        for (int i = 0; i < MAX_CIRCLES; i++) {
            EnemyCircle circle;
            do {
                circle = getRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            circles.add(circle);
        }
        setCirclesColorBySize();
    }

    private void setCirclesColorBySize() {
        for (EnemyCircle c : circles) {
            c.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    public void onDraw() {
        canvasView.drawCircle(mainCircle);

        for (EnemyCircle enemyCircle : circles) {
            canvasView.drawCircle(enemyCircle);
        }
    }

    public void onTouchEvent(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x, y);
        collision = false;
        checkCollision();
        moveCircles();

        if (!collision) {
            resizeWhenOutstanding();
        }
    }

    private void resizeWhenOutstanding() {
        boolean smaller = false;
        boolean greater = false;
        for (EnemyCircle circle : circles) {
            if (circle.isSmallerThan(mainCircle)) {
                smaller = true;
            } else {
                greater = true;
            }
        }

        if (smaller) {

        } else {

        }

//        if (circles.size() > 3) {
//            if (greater > circles.size() - 1) {
//                reduceCirclesRadius(circles);
//                setCirclesColorBySize();
//                return;
//            }
//        }
//        if (circles.size() > 3) {
//            if (smaller > circles.size() - 3) {
//                mainCircle.setRadius(mainCircle.getRadius() - 1);
//            }
//        }
    }

    private void reduceCirclesRadius(ArrayList<EnemyCircle> circles) {
        for (EnemyCircle circle : circles) {
            circle.setRadius(circle.getRadius() - 1);
        }
    }

    private void increaseCirclesRadius(ArrayList<EnemyCircle> circles) {
        for (EnemyCircle circle : circles) {
            circle.setRadius(circle.getRadius() + 1);
        }
    }

    private void checkCollision() {
        EnemyCircle circleForDel = null;
        for (EnemyCircle circle : circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    growRadius(circle, mainCircle);
                } else {
                    growRadius(mainCircle, circle);
                }
                setCirclesColorBySize();
                if (circle.getRadius() < MIN_RADIUS) {
                    circleForDel = circle;
                }
                break;
            }
        }
        if (circleForDel != null) {
            circles.remove(circleForDel);
        }
        if (circles.isEmpty()) {
            gameEnd("You win!");
        }
        if (mainCircle.getRadius() < MIN_RADIUS) {
            gameEnd("You loose.");
        }
        if (mainCircle.getRadius() > height / 5) {
            gameEnd("You win!");
        }
    }

    public void growRadius(SimpleCircle from, SimpleCircle to) {
        collision = true;
        // todo: Написать правильную формулу для изменения площади кругов
        int sizeDifference = (int) Math.sqrt(Math.pow(to.getRadius(), 2) + Math.pow(from.getRadius(), 2));
        int sd = (int) (Math.sqrt(Math.pow(to.getRadius(), 2)) / 20);
        if (sd == 0) sd = 1;
        to.setRadius(to.getRadius() + sd);
        from.setRadius(from.getRadius() - sd);
    }

    private void gameEnd(String s) {
        canvasView.showMessage(s);
        mainCircle.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle : circles) {
            circle.moveOneStep();
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private void initMainCircle() {
        mainCircle = new MainCircle(width / 2, height / 2);
    }

}
