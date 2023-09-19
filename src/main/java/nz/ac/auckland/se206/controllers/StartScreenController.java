package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class StartScreenController {
  @FXML private Button startButton;
  @FXML private Button exitButton;
  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button twoButton;
  @FXML private Button fourButton;
  @FXML private Button sixButton;
  @FXML private Label toDoLabel;

  private Button difficulty = null;
  private Button time = null;

  /**
   * Called when the start button is clicked
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onStart(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    // Updating gamestate on number of hints
    if (difficulty.equals(easyButton)) {
      GameState.numberOfHints = -1;
    } else if (difficulty.equals(mediumButton)) {
      GameState.numberOfHints = 5;
    } else {
      GameState.numberOfHints = 0;
    }
    // Updating game state depending on time choosen
    if (time.getText().equals("2")) {
      GameState.totalTime = 120;
    } else if (time.getText().equals("4")) {
      GameState.totalTime = 240;
    } else {
      GameState.totalTime = 360;
    }
    // Switching Scenes to the room
    try {
      // Starting the timer
      // THE GAME WILL CRASH IF THE USER SELECTS TIME AND DIFFICULTY BEFORE THE ROOM HAS LOADED
      // A FIX NEEDS TO BE IN PLACE
      GameState.timer.startTimer();
      sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
      sceneButtonIsIn.getWindow().sizeToScene();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Called when the exit button is clicked */
  @FXML
  private void onExit() {
    // exit application
    Platform.exit();
    System.exit(0);
  }

  /** Setting difficulty to easy */
  @FXML
  private void onClickEasy() {
    difButtonClicked(easyButton);
  }

  /** Setting difficulty to medium */
  @FXML
  private void onClickMedium() {
    difButtonClicked(mediumButton);
  }

  /** Setting difficulty to hard */
  @FXML
  private void onClickHard() {
    difButtonClicked(hardButton);
  }

  /** Setting time to Two */
  @FXML
  private void onClickTwo() {
    timeButtonClicked(twoButton);
    GameState.timer.setCounter(120);
  }

  /** Setting time to Four */
  @FXML
  private void onClickFour() {
    timeButtonClicked(fourButton);
    GameState.timer.setCounter(240);
  }

  /** Setting time to Six */
  @FXML
  private void onClickSix() {
    timeButtonClicked(sixButton);
    GameState.timer.setCounter(360);
  }

  /** Depending on current state label text is changed */
  private void updateToDo() {
    if (difficulty == null && time == null) {
      // None have been selected
      toDoLabel.setText("Please Select Time and Difficulty");
    } else if (difficulty == null) {
      // Only time selected
      toDoLabel.setText("Please select Difficulty");
    } else if (time == null) {
      // Only difficulty selected
      toDoLabel.setText("Please select Time");
    } else {
      // Both have been selected
      toDoLabel.setText(
          "You can now start the game, you will have "
              + time.getText()
              + " minutes on "
              + difficulty.getText()
              + " Difficulty to escape");
      startButton.setDisable(false);
    }
  }

  /**
   * A difficulty button has been clicked, updating gamestate
   *
   * @param button which difficulty button has been clicked
   */
  private void difButtonClicked(Button button) {
    // If difficulty has alreaady been set before
    if (difficulty != null) {
      difficulty.setDisable(false);
    }
    // Updating to new difficulty
    difficulty = button;
    difficulty.setDisable(true);
    updateToDo();
  }

  /**
   * A time button has been clicked, updating gamestate
   *
   * @param button which time button has been clicked
   */
  private void timeButtonClicked(Button button) {
    // If time has already been set before
    if (time != null) {
      time.setDisable(false);
    }
    // Updating to new time
    time = button;
    time.setDisable(true);
    updateToDo();
  }
}
