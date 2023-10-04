package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class HallwayController {

  @FXML private Pane room;
  @FXML private Label timerLabel;
  @FXML private Label geographyLabel;
  @FXML private Label gymLabel;
  @FXML private Rectangle classroomDoor;
  @FXML private Rectangle gymDoor;
  @FXML private Rectangle locker1;
  @FXML private Rectangle locker2;
  @FXML private Pane chatContainer;
  @FXML private Pane blackboardContainer;
  @FXML private ImageView ghost;
  @FXML private ImageView chatButton;
  @FXML private ImageView ghost1;
  @FXML private ImageView ghost2;
  @FXML private Label hiddenNumberOne;
  @FXML private Label hiddenNumberTwo;
  @FXML private Label messageText;
  @FXML private Polyline classroomDoorRectangle;
  @FXML private Polyline gymDoorRectangle;
  @FXML private Polyline lockerRectangle;
  @FXML private Polyline lockerRectangle2;
  @FXML private Polyline lockerRectangle3;
  @FXML private Polyline lockerRectangle4;
  private Shadow shadow = new Shadow(10, Color.BLACK);
  private Glow glow = new Glow(0.8);

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    // Adding timerlabel to synched timer
    GameState.timer.setHall(timerLabel, hiddenNumberOne, hiddenNumberTwo);
    GameState.hallController = this;
  }

  public void addBlackboard() {
    // Adding the blackboard to the scene
    blackboardContainer.getChildren().add(GameState.blackboardController.getPane());
    chatButton.toFront();
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

    if (GameState.countryIsFound) {
      if (GameState.isChatOpen) {
        Stage stage = (Stage) chatContainer.getScene().getWindow();
        stage.setWidth(1109);
        stage.centerOnScreen();
        GameState.lockerController.openChat();
      }

      // Switching to hallway scene
      Rectangle rectangle = (Rectangle) event.getSource();
      Scene sceneRectangleIsIn = rectangle.getScene();
      sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOCKER));

      if (!GameState.isChatOpen) {
        // Resizing the window so the scene fits
        sceneRectangleIsIn.getWindow().sizeToScene();
        // Get the stage after switching the scene
        Stage stage = (Stage) sceneRectangleIsIn.getWindow();
        stage.centerOnScreen();
      }
    }
  }

  @FXML
  private void onClickChat() {
    System.out.println("chat clicked");

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
  private void enterChatButton() {
    chatButton.setOpacity(0.5);
  }

  @FXML
  private void releaseChat() {
    chatButton.setOpacity(1);
  }

  public void openChat() {
    GameState.chatInGym = false;
    GameState.chatInRoom = false;
    GameState.chatInLocker = false;
    chatContainer.getChildren().add(GameState.chatController.getChatPane());
    GameState.chatInHall = true;
  }

  @FXML
  private void onEnterGhost() {
    System.out.println("hover on ghost");
    ghost.setEffect(shadow);
  }

  @FXML
  private void onExitGhost() {
    System.out.println("hover off ghost");
    ghost.setEffect(null);
  }

  @FXML 
  private void onClickGhost() {
    if (!GameState.isChatOpen) {
      onClickChat();
    }
  }

  @FXML
  public void classroomDoorEntered() {
    GameState.blackboardController.setHoverText("Classroom Door");
    classroomDoorRectangle.setVisible(true);
  }

  @FXML
  public void classroomDoorExited() {
    GameState.blackboardController.setHoverText("");
    classroomDoorRectangle.setVisible(false);
  }

  @FXML
  public void gymDoorEntered() {
    GameState.blackboardController.setHoverText("Gym Door");
    gymDoorRectangle.setVisible(true);
  }

  @FXML
  public void gymDoorExited() {
    GameState.blackboardController.setHoverText("");
    gymDoorRectangle.setVisible(false);
  }

  @FXML
  public void locker1Entered() {
    if (GameState.countryIsFound) {
      GameState.blackboardController.setHoverText("Locker");
    } else {
      GameState.blackboardController.setHoverText("Locker is locked");
    }
    lockerRectangle.setVisible(true);
    lockerRectangle2.setVisible(true);
  }

  @FXML
  public void locker2Entered() {
    if (GameState.countryIsFound) {
      GameState.blackboardController.setHoverText("Locker");
    } else {
      GameState.blackboardController.setHoverText("Locker is locked");
    }
    lockerRectangle3.setVisible(true);
    lockerRectangle4.setVisible(true);
  }

  @FXML
  public void lockerExited() {
    GameState.blackboardController.setHoverText("");
    lockerRectangle.setVisible(false);
    lockerRectangle2.setVisible(false);
    lockerRectangle3.setVisible(false);
    lockerRectangle4.setVisible(false);
  }

  public void responseLoading() {
    ghost.setEffect(shadow);
    Random random = new Random();
    int randomNumber = random.nextInt(3); // Generates a random number 0, 1, or 2

    switch (randomNumber) {
      case 0:
        // Apply the effect to 'room'
        room.setEffect(glow);
        break;
      case 1:
        ghost1.setVisible(true);
        ghost2.setVisible(true);
        break;
      case 2:
        // Make the escape message visible
        ghost1.setVisible(true);
        ghost1.setEffect(shadow);
        messageText.setVisible(true);
        break;
      default:
        break;
    }
  }

  public void responseLoaded() {
    // Remove the effects when the response is loaded
    ghost.setEffect(null);
    room.setEffect(null);
    gymLabel.setEffect(null);
    geographyLabel.setEffect(null);
    ghost1.setVisible(false);
    ghost1.setEffect(null);
    ghost2.setVisible(false);
    messageText.setVisible(false);
  }
}
