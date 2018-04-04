package assignment5;

/*
    FLEA
    - Annoying insect.
    - Runs around the map randomly and reproduces at a very rapid rate.
    - Always attempts to flee from Critters unless they are Algae.
 */

import java.util.List;

public class Critter4 extends Critter {

    @Override
    public String toString(){
        return "4";
    }

    @Override
    public void doTimeStep() {
        walk(getRandomInt(8));

        if(getEnergy() > 100){
            reproduce(new Critter4(), getRandomInt(8));
            reproduce(new Critter4(), getRandomInt(8));
        }
    }

    @Override
    public boolean fight(String opponent) {
        return opponent.equals("@");
    }

    public static void runStats(List<Critter> critters) {

        String output = new String();
        output.concat(critters.size() + " total Fleas    ");
        return output;
    }
}
