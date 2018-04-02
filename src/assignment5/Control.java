package assignment5;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.File;
import java.util.ArrayList;

public class Control {
    public static final double[] speeds = {0.25, 0.5, 1, 2, 4};
    private static final double gridSquareSize = 25;
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    public ScrollPane mainScroll;
    public Label sizeDisplay;

    /* MAKE CRITTERS */
    public ChoiceBox<String> critterAddSelect;
    public TextField critterAddNum;
    public Button critterAdd;

    /* SEED */
    public TextField setSeedNum;
    public Button setSeed;

    /* STEP */
    public TextField stepNum;
    public CheckBox stepCont;
    public Button step;
    public Slider stepSpeed;
    public Button stepPlay;

    /* STATS */
    public VBox statsShow;

    @FXML
    protected void initialize(){
        /* INITIALIZE GRID */
        sizeDisplay.setText(Params.world_width + " x " + Params.world_height);

        GridPane grid = new GridPane();

        grid.setHgap(-1);
        grid.setVgap(-1);

        mainScroll.setContent(grid);

        for(int x = 0; x < Params.world_width; x++){
            for(int y = 0; y < Params.world_height; y++){
                Shape s = new Rectangle(gridSquareSize, gridSquareSize);
                s.setFill(null);
                s.setStroke(Color.RED);
                grid.add(s, x, y);
            }
        }

        /* SET DROP DOWN MENU FOR CRITTERS */
        String[] list = (new File("src/" + myPackage)).list();
        ArrayList<String> finalList = new ArrayList<>();

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

        /* SPEED SLIDER */
        stepSpeed.valueProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            ToggleSpeed();
        });
    }

    public void ToggleCont(){
        stepNum.setDisable(stepCont.isSelected());
        stepSpeed.setDisable(!stepCont.isSelected());

        step.setVisible(!stepCont.isSelected());
        stepPlay.setVisible(stepCont.isSelected());

        ToggleSpeed();
    }

    public void ToggleSpeed(){
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


}
