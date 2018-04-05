package assignment5;

/*
    WINSTON
    - Moody critter.
    - Fights more and runs when moody, fights less and walks when happy.
 */

import javafx.scene.paint.Color;

public class Winston extends Critter {

    @Override
    public CritterShape viewShape() { return CritterShape.DIAMOND; }
    @Override
    public javafx.scene.paint.Color viewOutlineColor() { return Color.BLACK; }
    @Override
    public javafx.scene.paint.Color viewColor() { return Color.ROYALBLUE; }

    @Override
    public String toString() { return "2"; }

    private int direction;
    private String mood;

    public Winston() {
        direction = Critter.getRandomInt(8);

        switch (Critter.getRandomInt(3)){
            case 0:
                mood = "happy";
                break;
            case 1:
                mood = "sad";
                break;
            case 2:
                mood = "angry";
                break;
        }
    }
    public boolean fight(String vs) {
        switch (mood) {
            case "angry":
                return true;
            case "sad":
                return getRandomInt(2) == 0;
            case "happy":
                return getRandomInt(4) == 0;
        }
        return false;
    }
    @Override
    public void doTimeStep() {
        if (getEnergy() > 160)
            reproduce(new Winston(), Critter.getRandomInt(8));

        switch (mood) {
            case "angry":
                run(direction);
                break;
            case "sad":
                walk(direction);
                break;
            case "happy":
                walk(direction);
                break;
        }
        direction = Critter.getRandomInt(8);
    }
    public static String runStats(java.util.List<Critter> Winstons) {
        String output = "";

        int happy = 0;
        int sad = 0;
        int angry = 0;

        for (Object obj : Winstons) {
            Winston c = (Winston) obj;
            switch (c.mood) {
                case "happy":
                    happy++;
                    break;
                case "sad":
                    sad++;
                    break;
                case "angry":
                    angry++;
                    break;
            }
        }

        double total=happy+sad+angry;

        output = output.concat("" + Winstons.size() + " total Winstons    ");
        output = output.concat("" + Math.floor(happy*100 / (total)) + "% happy   ");
        output = output.concat("" + Math.floor(sad*100 / (total)) + "% sad   ");
        output =  output.concat("" + Math.floor(angry*100 / (total)) + "% angry   ");
        return output;
    }
}