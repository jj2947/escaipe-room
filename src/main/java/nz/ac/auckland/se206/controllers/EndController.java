package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;

public class EndController {

  @FXML private Label textLabel;

  public void initialize() {
    updateLabel();
  }

  @FXML
  public void updateLabel() {
    if (GameState.isTimeReached) {
      textLabel.setText("You Lost!");
    } else {
      textLabel.setText("You Escaped!");
    }
  }
}
