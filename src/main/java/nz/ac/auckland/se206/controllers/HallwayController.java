package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
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
  @FXML private Pane blackboardContainer;
  @FXML private ImageView ghost;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    // Adding timerlabel to synched timer
    GameState.timer.setHall(timerLabel);
    GameState.hallController = this;
  }

  public void addBlackboard() {
    // Adding the blackboard to the scene
    blackboardContainer.getChildren().add(GameState.blackboardController.getPane());
  }

  @FXML
  public void clickClassroomDoor(MouseEvent event) throws IOException {
    System.out.println("classroom door clicked");

    if (GameState.isChatOpen) {
      GameState.roomController.openChat();
    }

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.ROOM));

    if (!GameState.isChatOpen) {
      // Resizing the window so the scene fits
      sceneRectangleIsIn.getWindow().sizeToScene();
    }

    GameState.roomController.addBlackboard();
  }

  @FXML
  public void clickGymDoor(MouseEvent event) throws IOException {
    System.out.println("gym door clicked");

    if (GameState.isChatOpen) {
      GameState.gymController.openChat();
    }

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.GYMNASIUM));

   if (!GameState.isChatOpen) {
      // Resizing the window so the scene fits
      sceneRectangleIsIn.getWindow().sizeToScene();
    }

    GameState.gymController.addBlackboard();
  }

  @FXML
  public void clickLocker(MouseEvent event) throws IOException {
    System.out.println("locker clicked");

    if (GameState.isChatOpen) {
      Stage stage = (Stage) chatContainer.getScene().getWindow();
      stage.setWidth(1109);
      GameState.lockerController.openChat();
    }

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOCKER));

    if (!GameState.isChatOpen) {
      // Resizing the window so the scene fits
      sceneRectangleIsIn.getWindow().sizeToScene();
    }
  }

  @FXML
  public void onClickGhost() {
    System.out.println("ghost clicked");
    // Add the chat to the chat container
    if (!GameState.chatInHall) {
      openChat();
    }

    if (GameState.isChatOpen) {
      GameState.chatController.closeChat();
      GameState.isChatOpen = false;
    } else {
      GameState.chatController.openChat();
      GameState.isChatOpen = true;
    }
  }

  @FXML
  private void onEnterGhost() {
    System.out.println("hover on ghost");
    // Add the chat to the chat container
    if (!GameState.chatInHall) {
      openChat();
    }

    if (!GameState.isChatOpen) {
      GameState.chatController.openChat();
      GameState.isChatOpen = true;
    }
  }

  public void openChat() {
    GameState.chatInGym = false;
    GameState.chatInRoom = false;
    GameState.chatInLocker = false;
    chatContainer.getChildren().add(GameState.chatController.getChatPane());
    GameState.chatInHall = true;
  }
}
