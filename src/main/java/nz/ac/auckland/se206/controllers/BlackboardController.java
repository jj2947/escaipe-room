package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.GameState;

public class BlackboardController {

  @FXML private AnchorPane blackboardPane;
  @FXML private TextField blackboard;

  public void initialize() {
    // Initialization code goes here
    GameState.blackboardController = this;
  }

  public void openBlackboard() {
    Stage stage = (Stage) blackboardPane.getScene().getWindow();
    stage.setHeight(100);
  }

  public void closeBlackboard() {
    Stage stage = (Stage) blackboardPane.getScene().getWindow();
    stage.setHeight(100);
  }

  public void setBlackboard(String text) {
    blackboard.setText(text);
  }
}
