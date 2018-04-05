package assignment5;

/*
    REX
    - Aggressive Critter.
    - Almost always fight (top of the food chain).
    - Slow to reproduce, cannot run.
 */

import javafx.scene.paint.Color;

public class Rex extends Critter {

    private int direction;

    @Override
    public CritterShape viewShape() { return CritterShape.STAR; }
    @Override
    public javafx.scene.paint.Color viewOutlineColor() { return Color.YELLOW; }
    @Override
    public javafx.scene.paint.Color viewColor() { return Color.RED; }

    @Override
    public String toString() {
        return "1";
    }

    public Rex() {
        direction = Critter.getRandomInt(8);
    }

    public boolean fight(String vs) {
        return (getRandomInt(6) < 5);
    }

    @Override
    public void doTimeStep() {
        if(getRandomInt(2) == 1){
            walk(direction);
        }
        if (getEnergy() > 300)
            reproduce(new Rex(), Critter.getRandomInt(8));
        direction = Critter.getRandomInt(8);
    }

    public static String runStats(java.util.List<Critter> rexes) {
        String output = "";
        output = output.concat("" + rexes.size() + " total Rexes    ");
        return output;
    }

}