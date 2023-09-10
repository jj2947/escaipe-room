package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class RoomController {

  @FXML private Pane room;
  @FXML private Rectangle door;
  @FXML private Label timerLabel;
  @FXML private ImageView mapImage;
  @FXML private Ellipse mapEclipse;
  @FXML private Rectangle mapRectangleOne;
  @FXML private Rectangle mapRectangleTwo;
  @FXML private Label goBackLabel;
  @FXML private ImageView classroomImage;
  private Timer timer;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    // Initialization code goes here
    // Start the timer
    timer = new Timer(timerLabel);
    GameState.timer = timer;
    SceneManager.addUi(AppUi.HALLWAY, App.loadFxml("hallway"));
    SceneManager.addUi(AppUi.GYMNASIUM, App.loadFxml("gymnasium"));
    SceneManager.addUi(AppUi.CHAT, App.loadFxml("chat"));
    Platform.runLater(() -> startTimer());
  }

  private void startTimer() {
    timer.startTimer();
    // Update the timer label every second
    Task<Void> updateLabelTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (!GameState.isTimeReached) {
              Platform.runLater(() -> updateLabel());
              Thread.sleep(1000);
            }
            return null;
          }
        };

    // Create a new thread for the update task and start it
    Thread updateThread = new Thread(updateLabelTask);
    updateThread.setDaemon(true);
    updateThread.start();
  }

  private void updateLabel() {
    timerLabel.setText(
        String.format("%02d:%02d", timer.getCounter() / 60, timer.getCounter() % 60));
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickDoor(MouseEvent event) {
    System.out.println("hallway door clicked");

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.HALLWAY));

    // Resizing the window so the larger scene fits
    sceneRectangleIsIn.getWindow().sizeToScene();
  }

  @FXML
  public void clickMap() {
    disableWhileMapOpen();
    showWhenOnMap();
  }

  @FXML
  public void exitMap() {
    enableWithMapClose();
    hideAfterMap();
  }

  public void showWhenOnMap() {
    mapImage.setVisible(true);
    classroomImage.setOpacity(0.5);
    goBackLabel.setVisible(true);
  }

  public void hideAfterMap() {
    mapImage.setVisible(false);
    classroomImage.setOpacity(1);
    goBackLabel.setVisible(false);
  }

  public void disableWhileMapOpen() {
    mapEclipse.setDisable(true);
    mapRectangleOne.setDisable(true);
    mapRectangleTwo.setDisable(true);
    door.setDisable(true);
  }

  public void enableWithMapClose() {
    mapEclipse.setDisable(false);
    mapRectangleOne.setDisable(false);
    mapRectangleTwo.setDisable(false);
    door.setDisable(false);
  }
}
