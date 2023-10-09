package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
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
  @FXML private Path path;
  private boolean playForward = true;
  private boolean ghostMoving = false;
  private boolean isSpeechBubbleShowing = false;
  private boolean isFirstTimeLockerClicked = true;
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

  /**
   * Called when the classroom door is clicked. It switches the scene to the classroom.
   *
   * @param event
   * @throws IOException
   */
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

  /**
   * Called when the gym door is clicked. It switches the scene to the gym.
   *
   * @param event
   * @throws IOException
   */
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

  /**
   * Called when the locker is clicked. It switches the scene to the locker.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  public void clickLocker(MouseEvent event) throws IOException {
    System.out.println("locker clicked");

    if (GameState.countryIsFound) {
      if (isFirstTimeLockerClicked) {
        GameState.textFlow.getChildren().clear();
        isFirstTimeLockerClicked = false;
      }
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

  /** Called when the chat button is clicked. It opens the chat. */
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

  /** Called when the chat button is clicked. It opens the chat. */
  public void openChat() {
    GameState.chatInGym = false;
    GameState.chatInRoom = false;
    GameState.chatInLocker = false;
    chatContainer.getChildren().add(GameState.chatController.getChatPane());
    GameState.chatInHall = true;
  }

  @FXML
  private void onExitGhost() {
    if (ghostMoving == false) {
      ghost.setEffect(null);
    }
  }

  @FXML
  private void onEnterGhost() {
    ghostMoving = false;
    ghost.setEffect(shadow);
  }

  /** Called when the ghost is clicked. It moves the ghost. */
  @FXML
  private void onClickGhost() {
    // If the chat is not open, open it and move the ghost
    if (!GameState.isChatOpen) {
      onClickChat();
      Platform.runLater(() -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
    } else if (!isSpeechBubbleShowing) { // If the chat is open, but the speech bubble is not
      // showing, move the ghost
      Platform.runLater(() -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
      ghostMoving = true;
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

  /** Called when the locker is entered. It makes the locker visible and sets the hover text. */
  @FXML
  public void locker1Entered() {
    if (GameState.countryIsFound) { // If the country is found, the locker is unlocked
      GameState.blackboardController.setHoverText("Locker");
    } else {
      GameState.blackboardController.setHoverText("Locker is locked");
    }
    // Make the locker visible
    lockerRectangle.setVisible(true);
    lockerRectangle2.setVisible(true);
  }

  /** Called when the locker is exited. It makes the locker invisible and removes the hover text. */
  @FXML
  public void locker2Entered() {
    // If the country is found, the locker is unlocked
    if (GameState.countryIsFound) {
      GameState.blackboardController.setHoverText("Locker");
    } else {
      GameState.blackboardController.setHoverText("Locker is locked");
    }
    // Make the locker visible
    lockerRectangle3.setVisible(true);
    lockerRectangle4.setVisible(true);
  }

  /** Called when the locker is exited. It makes the locker invisible and removes the hover text. */
  @FXML
  public void lockerExited() {
    GameState.blackboardController.setHoverText("");
    lockerRectangle.setVisible(false);
    lockerRectangle2.setVisible(false);
    lockerRectangle3.setVisible(false);
    lockerRectangle4.setVisible(false);
  }

  /** Called when the response is loading. It sets the effects in the room. */
  public void responseLoading() {
    ghost.setEffect(shadow);
    Random random = new Random();
    int randomNumber = random.nextInt(4); // Generates a random number 0, 1, or 2

    switch (randomNumber) {
      case 0:
        // Apply the effect to 'room'
        room.setEffect(glow);
        break;
      case 1:
        ghost1.toFront();
        ghost2.toFront();
        break;
      case 2:
        // Make the escape message visible
        ghost1.toFront();
        ghost1.setEffect(shadow);
        messageText.toFront();
        break;
      case 3:
        Platform.runLater(
            () -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
      default:
        break;
    }
  }

  /** Called when the response is loaded. It removes the effects in the room. */
  public void responseLoaded() {
    // Remove the effects when the response is loaded
    Platform.runLater(
        () -> {
          ghost.setEffect(null);
          room.setEffect(null);
          gymLabel.setEffect(null);
          geographyLabel.setEffect(null);
          ghost1.toBack();
          ghost1.setEffect(null);
          ghost2.toBack();
          messageText.toBack();
        });
  }
}
