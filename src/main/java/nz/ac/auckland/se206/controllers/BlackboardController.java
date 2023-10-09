package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.GameState;

/** This is the controller for the blackboard. */
public class BlackboardController {

  @FXML private AnchorPane blackboardPane;
  @FXML private TextField blackboard;
  @FXML private ImageView hallpassImage;
  @FXML private ImageView basketballImage;
  @FXML private Label itemLabel;
  @FXML private Label objectiveLabel;
  @FXML private Label hoverLabel;

  public void initialize() {
    // Initialization code goes here
    GameState.blackboardController = this;
  }

  public void setBlackboard(String text) {
    blackboard.setText(text);
  }

  public AnchorPane getPane() {
    return blackboardPane;
  }

  public void showHallpass() {
    hallpassImage.setVisible(true);
  }

  public void showBasketball() {
    basketballImage.setVisible(true);
  }

  public void showItemLabel() {
    itemLabel.setVisible(true);
  }

  public void setObjectiveText(String text) {
    objectiveLabel.setText(text);
  }

  public void setHoverText(String text) {
    hoverLabel.setText(text);
  }
}
