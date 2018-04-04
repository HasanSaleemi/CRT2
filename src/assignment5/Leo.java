package assignment5;

/*
    LEO
    - Small, feline Critter.
    - Only fights Algae and Fleas.
    - Movement is dependent on skin.
 */

public class Leo extends Critter {

    @Override
    public CritterShape viewShape() {
        return CritterShape.TRIANGLE;
    }

    @Override
    public String toString() {
        return "3";
    }

    private String skin;

    public Leo() {
        switch (Critter.getRandomInt(3)){
            case 0:
                skin = "spots";
                break;
            case 1:
                skin = "stripes";
                break;
            case 2:
                skin = "colors";
                break;
        }
    }
    public boolean fight(String vs) {
        return !vs.equals("1") && !vs.equals("2");
    }
    @Override
    public void doTimeStep() {
        if (getEnergy() > 150)
            reproduce(new Leo(), Critter.getRandomInt(8));

        if(getEnergy() > 50){
            switch(skin) {
                case "spots":
                    walk(getRandomInt(2)*4);
                    break;
                case "stripes":
                    walk(getRandomInt(2) == 1 ? 2 : 6);
                    break;
                case "colors":
                    walk(getRandomInt(2) == 1 ? 1 : 5);
                    break;
            }
        }
    }
    public static String runStats(java.util.List<Critter> Teds) {
        String output = "";
        int spots = 0;
        int stripes = 0;
        int colors = 0;

        for (Object obj : Teds) {
            Leo c = (Leo) obj;
            switch (c.skin) {
                case "spots":
                    spots++;
                    break;
                case "stripes":
                    stripes++;
                    break;
                case "colors":
                    colors++;
                    break;
            }
        }

        double total=spots+stripes+colors;
        output = output.concat("" + Teds.size() + " total Leos    ");
        output = output.concat("" + spots*100 / (total) + "% spotted   ");
        output = output.concat("" + stripes*100 / (total) + "% striped   ");
        output = output.concat("" + colors * 100 / (total) + "% colored   ");
        return output;
    }
}