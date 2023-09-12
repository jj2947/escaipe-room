package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class HallwayController {

  @FXML private Label timerLabel;
  @FXML private Rectangle classroomDoor;
  @FXML private Rectangle gymDoor;
  @FXML private Rectangle locker1;
  @FXML private Rectangle locker2;
  @FXML private Pane chatContainer;
  private Timer timer;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    timer = GameState.timer;
    Platform.runLater(() -> updateTimer());
  }

  @FXML
  public void clickClassroomDoor(MouseEvent event) throws IOException {
    System.out.println("classroom door clicked");

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.ROOM));

    // Resizing the window so the larger scene fits
    sceneRectangleIsIn.getWindow().sizeToScene();
  }

  @FXML
  public void clickGymDoor(MouseEvent event) throws IOException {
    System.out.println("gym door clicked");

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.GYMNASIUM));

    // Resizing the window so the larger scene fits
    sceneRectangleIsIn.getWindow().sizeToScene();
  }

  @FXML
  public void clickLocker(MouseEvent event) throws IOException {
    System.out.println("locker clicked");

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOCKER));

    // Resizing the window so the larger scene fits
    sceneRectangleIsIn.getWindow().sizeToScene();
  }

  private void updateTimer() {
    // Update the timer label every second
    Task<Void> updateLabelTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (!GameState.isTimeReached) {
              Platform.runLater(
                  () ->
                      timerLabel.setText(
                          String.format(
                              "%d:%02d", timer.getCounter() / 60, timer.getCounter() % 60)));
              ;
              Thread.sleep(1000); // Wait for 1 second
            }
            if (GameState.isTimeReached) {
            switchToEndScene();
            }
            return null;
          }
        };

    // Create a new thread for the update task and start it
    Thread updateThread = new Thread(updateLabelTask);
    updateThread.setDaemon(true);
    updateThread.start();
  }

  @FXML
  public void onClickGhost(MouseEvent event) {
    System.out.println("ghost clicked");
    // Get the stage from the chatContainer's scene
    Stage stage = (Stage) timerLabel.getScene().getWindow();
    // Resize the stage
    stage.setWidth(1407);
     // Add the chat to the chat container
    if (!GameState.chatInHall) {
      GameState.chatInGym = false;
      GameState.chatInRoom = false;
      GameState.chatInLocker = false;
      chatContainer.getChildren().add(GameState.chatController.getChatPane());
      GameState.chatInHall = true;
    }
  }

  private void switchToEndScene() {
    Platform.runLater(
        () -> {
          Scene currentScene = timerLabel.getScene();
          if (currentScene != null) {
            try {
              SceneManager.addUi(AppUi.END, App.loadFxml("end"));
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            currentScene.setRoot(SceneManager.getUiRoot(AppUi.END));
            currentScene.getWindow().sizeToScene();
          }
        });
  }
}
