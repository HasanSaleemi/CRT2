package assignment5;

/*
    REX
    - Aggressive Critter.
    - Almost always fight (top of the food chain).
    - Slow to reproduce, cannot run.
 */

public class Critter1 extends Critter {

    private int direction;

    @Override
    public String toString() {
        return "1";
    }

    public Critter1() {
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
            reproduce(new Critter1(), Critter.getRandomInt(8));
        direction = Critter.getRandomInt(8);
    }

    public static String runStats(java.util.List<Critter> rexes) {
        String output = new String();
        output.concat("" + rexes.size() + " total Rexes    ");
        return output;
    }

}