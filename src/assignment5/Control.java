package assignment5;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Control {
    public static final double[] speeds = {0.25, 0.5, 1, 2, 4};
    private static final double gridSquareSize = 25;
    private static ArrayList<String> finalList;
    private static Map<String, Boolean> statsIgnore = new HashMap<>();
    private double scale = 1.0;

    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /* MAKE CRITTERS */
    public ChoiceBox<String> critterAddSelect;
    public TextField critterAddNum;
    public Button critterAdd;

    /* SEED */
    public TextField setSeedNum;
    public Button setSeed;
    public Label seedDisplay;

    /* STEP */
    public TextField stepNum;
    public CheckBox stepCont;
    public Button step;
    public Slider stepSpeed;
    public Button stepPlay;

    private static AnimationTimer aniStep;
    private static boolean aniPlaying = false;

    /* STATS */
    public VBox statsShow;
    public Label statsLabel;
    public Button statsSelectAll;
    public Button statsSelectNone;

    /* GRID */
    private GridPane grid;
    public ScrollPane mainScroll;
    public Label sizeDisplay;
    public Slider zoomSlider;
    public Button nuke;

    @FXML
    protected void initialize(){
        setGridTopText();

        /* INITIALIZE GRID*/
        grid = new GridPane();
        grid.setGridLinesVisible(true);

        grid.setStyle("-fx-border-color: grey ; -fx-border-radius: 0 ; -fx-border-width: 4");

        mainScroll.setContent(grid);

        for(int x = 0; x < Params.world_width; x++){
            ColumnConstraints c = new ColumnConstraints(gridSquareSize);
            c.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(c);
        }
        for(int y = 0; y < Params.world_height; y++){
            RowConstraints c = new RowConstraints(gridSquareSize);
            c.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(c);
        }

        mainScroll.setHvalue(0.5);
        mainScroll.setVvalue(0.5);

        /* SET DROP DOWN MENU FOR CRITTERS */
        String[] list = (new File("src/" + myPackage)).list();
        finalList = new ArrayList<>();

        if(list != null){
            for(String fName : list){
                if(fName.contains(".java") && !fName.equals("Critter.java")){
                    String fmt = fName.replace(".java", "");
                    try {
                        if(Critter.class.isAssignableFrom(Class.forName(myPackage + "." + fmt))){
                            finalList.add(fmt);

                            CheckBox statCheck = new CheckBox(fmt);
                            statCheck.setSelected(true);
                            statsShow.getChildren().add(statCheck);
                            statCheck.setOnMouseClicked(event ->{
                                statsIgnore.put(fmt, statCheck.isSelected() ? null : true);
                                UpdateStats();
                            });
                        }
                    } catch (ClassNotFoundException ignored){}
                }
            }
            critterAddSelect.setItems(FXCollections.observableArrayList(finalList));
            critterAddSelect.setValue(finalList.get(0));
        }

        /* FIX TEXT BOXES */
        stepNum.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue)
                fixText(stepNum);
        });
        critterAddNum.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue)
                fixText(critterAddNum);
        });
        setSeedNum.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue)
                fixSeed(setSeedNum);
        });

        /* SLIDERS */
        stepSpeed.valueProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            ToggleSpeed();
        });
        zoomSlider.valueProperty().addListener((arg0, oldV, newV) -> {
            scale = (int)zoomSlider.getValue()/10.0 >= 0.1 ? (int)zoomSlider.getValue()/10.0 : 0.05;
            ScaleGrid();
        });


        /* SHOW STATS */
        UpdateStats();

        /* SETUP ANIMATION */
        aniStep = new AnimationTimer() {
            private long lastStep = 0;

            @Override
            public void handle(long now) {
                if(now - lastStep >= (long)((100_000_000)/speeds[(int)stepSpeed.getValue() - 1])){
                    lastStep = now;
                    Critter.worldTimeStep();
                    UpdateStats();
                }
            }
        };
    }

    public void quit(){
        System.exit(0);
    }

    public void setGridTopText(){
        sizeDisplay.setText(Params.world_width + " x " + Params.world_height + " (" + scale + "x)");
    }

    public void UpdateStats(){
        statsLabel.setText("");
        for(String className : finalList){
            try{
                if(statsIgnore.get(className) == null){
                    List<Critter> getCrits = Critter.getInstances(className);
                    String getStats = (String)Class.forName(myPackage + "." + className).getMethod("runStats", List.class).invoke(null, getCrits);
                    statsLabel.setText(statsLabel.getText() + className.toUpperCase() + "\n" + getStats + "\n\n");
                }
            } catch (Exception ignored) {}
        }
        if(statsLabel.getText().equals(""))
            statsLabel.setText("No stats to show...");
        RefreshGrid();
    }

    private void BulkSelectStats(boolean val){
        for(Node s : statsShow.getChildren()){
            CheckBox get = (CheckBox)s;
            get.setSelected(val);
            statsIgnore.put(get.getText(), val ? null : true);
        }
        UpdateStats();
    }
    public void SelectAllStats(){
        BulkSelectStats(true);
    }
    public void SelectNoStats(){
        BulkSelectStats(false);
    }

    private ArrayList<Node> curGridNodes = new ArrayList<>();

    private void resizeSVG(SVGPath svg, double width, double height) {
        double originalWidth = svg.prefWidth(-1);
        double originalHeight = svg.prefHeight(originalWidth);

        double scaleX = width / originalWidth;
        double scaleY = height / originalHeight;

        svg.setScaleX(scaleX);
        svg.setScaleY(scaleY);
    }

    private Shape getShape(Critter.CritterShape c){
        Shape s;
        double scaledSize = gridSquareSize * scale;

        switch (c){
            case STAR:
                SVGPath svg = new SVGPath();
                svg.setContent("M0,449h1235l-999,726 382-1175 382,1175z");
                resizeSVG(svg, scaledSize, scaledSize);
                return svg;
            case CIRCLE:
                s = new Circle(scaledSize/2);
                return s;
            case DIAMOND:
                s = new Rectangle(scaledSize*Math.sin(Math.PI/4), scaledSize*Math.sin(Math.PI/4));
                s.setRotate(45);
                return s;
            case SQUARE:
                s = new Rectangle(scaledSize, scaledSize);
                return s;
            case TRIANGLE:
                s = new Polygon(scaledSize/2, 0, -scaledSize/2, 0, 0, -scaledSize);
                return s;
        }

        return new Rectangle(scaledSize, scaledSize);
    }

    private void scaleShape(Shape s){
        double scaledSize = gridSquareSize * scale;

        switch (s.getAccessibleText()){
            case "STAR":
                resizeSVG((SVGPath)s, scaledSize, scaledSize);
                break;
            case "CIRCLE":
                ((Circle) s).setRadius(scaledSize/2);
                break;
            case "DIAMOND":
                ((Rectangle) s).setWidth(scaledSize*Math.sin(Math.PI/4));
                ((Rectangle) s).setHeight(scaledSize*Math.sin(Math.PI/4));
                break;
            case "SQUARE":
                ((Rectangle) s).setWidth(scaledSize);
                ((Rectangle) s).setHeight(scaledSize);
                break;
            case "TRIANGLE":
                ((Polygon) s).getPoints().clear();
                ((Polygon) s).getPoints().addAll(scaledSize/2, 0.0, -scaledSize/2, 0.0, 0.0, -scaledSize);
                break;
        }
    }

    public void ZoomIn(){
        zoomSlider.increment();
    }
    public void ZoomOut(){
        zoomSlider.decrement();
    }

    private void ScaleGrid(){
        for(Node n : grid.getChildren()){
            if(n instanceof Shape)
                scaleShape((Shape)n);
        }

        for(RowConstraints r : grid.getRowConstraints()){
            r.setPrefHeight(gridSquareSize * scale);
        }
        for(ColumnConstraints w : grid.getColumnConstraints()){
            w.setPrefWidth(gridSquareSize * scale);
        }

        setGridTopText();
    }

    private void RefreshGrid(){
        grid.getChildren().removeAll(curGridNodes);
        curGridNodes.clear();

        for(Critter c : Critter.population){
            Shape s = getShape(c.viewShape());
            s.setAccessibleText(c.viewShape().toString());
            s.setFill(c.viewFillColor());
            s.setStroke(c.viewOutlineColor());
            grid.add(s, c.x_coord, c.y_coord);
            curGridNodes.add(s);
        }
    }

    public void NukeWorld(){
        Critter.clearWorld();
        UpdateStats();
    }

    public void MakeClick(){
        fixText(critterAddNum);

        int count = Integer.parseInt(critterAddNum.getText());
        String critName = critterAddSelect.getValue();

        for(int i = 0; i < count; i++){
            try {
                Critter.makeCritter(critName);
            } catch (InvalidCritterException ignored) {}
        }
        UpdateStats();
    }
    public void StepClick(){
        fixText(stepNum);

        int count = Integer.parseInt(stepNum.getText());

        for(int i = 0; i < count; i++){
            Critter.worldTimeStep();
        }

        UpdateStats();
    }
    public void PlayClick(){
        aniPlaying = !aniPlaying;

        if(aniPlaying)
            aniStep.start();
        else
            aniStep.stop();

        critterAdd.setDisable(aniPlaying);
        setSeed.setDisable(aniPlaying);
        stepCont.setDisable(aniPlaying);
        nuke.setDisable(aniPlaying);

        ToggleSpeed();
    }
    public void SeedClick(){
        fixSeed(setSeedNum);
        long checkNum = Long.parseLong(setSeedNum.getText());
        Critter.setSeed(checkNum);
        seedDisplay.setText("SEED: " + checkNum);
    }

    public void ToggleCont(){
        stepNum.setDisable(stepCont.isSelected());
        stepSpeed.setDisable(!stepCont.isSelected());

        step.setVisible(!stepCont.isSelected());
        stepPlay.setVisible(stepCont.isSelected());

        ToggleSpeed();
    }

    public void ToggleSpeed(){
        if(aniPlaying)
            stepPlay.setText("STOP (" + speeds[(int)stepSpeed.getValue() - 1] + "x)");
        else
            stepPlay.setText("PLAY (" + speeds[(int)stepSpeed.getValue() - 1] + "x)");
    }

    public static void fixText(TextField box){
        try{
            int checkNum = Integer.parseInt(box.getText());
            if(checkNum <= 0)
                box.setText("1");
        } catch(NumberFormatException ignored) {
            box.setText("1");
        }
    }
    public static void fixSeed(TextField box){
        try{
            long checkNum = Long.parseLong(box.getText());
        } catch(NumberFormatException ignored) {
            char sep[] = box.getText().toCharArray();

            if(sep.length > 0){
                long checkNum = 0;
                for (char aSep : sep)
                    checkNum += (long)aSep;

                box.setText(Long.toString(checkNum));
            } else
                box.setText("0");
        }
    }
}
