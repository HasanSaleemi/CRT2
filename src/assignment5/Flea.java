package assignment5;

/*
    FLEA
    - Annoying insect.
    - Runs around the map randomly and reproduces at a very rapid rate.
    - Always attempts to flee from Critters unless they are Algae.
 */

import javafx.scene.paint.Color;

import java.util.List;

public class Flea extends Critter {

    @Override
    public CritterShape viewShape() { return CritterShape.CIRCLE; }
    @Override
    public javafx.scene.paint.Color viewOutlineColor() { return Color.GRAY; }
    @Override
    public javafx.scene.paint.Color viewColor() { return Color.BLACK; }

    @Override
    public String toString(){
        return "4";
    }

    @Override
    public void doTimeStep() {
        walk(getRandomInt(8));

        if(getEnergy() > 50){
            reproduce(new Flea(), getRandomInt(8));
        }
    }

    @Override
    public boolean fight(String opponent) {
        return opponent.equals("@");
    }

    public static String runStats(List<Critter> critters) {
        String output = "";
        output = output.concat(critters.size() + " total Fleas    ");
        return output;
    }
}
