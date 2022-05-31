package main;

/**
 * class for weight display
 * @author Paul
 */

public class Counter {
    private int score;

    public Counter() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void add(float i) {
        score += i;
    }
}