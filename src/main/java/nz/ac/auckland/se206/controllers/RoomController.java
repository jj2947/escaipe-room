package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class RoomController {

  @FXML private Pane room;
  @FXML private Rectangle door;
  @FXML private Label timerLabel;
  @FXML private Pane chatContainer;
  @FXML private ImageView mapImage;
  @FXML private Ellipse mapEllipse;
  @FXML private Rectangle mapRectangleOne;
  @FXML private Rectangle mapRectangleTwo;
  @FXML private Label goBackLabel;
  @FXML private ImageView classroomImage;
  @FXML private ImageView nzView;
  @FXML private ImageView australiaView;
  @FXML private ImageView argentinaView;
  @FXML private ImageView indiaView;
  @FXML private ImageView greenlandView;
  @FXML private ImageView brazilView;
  @FXML private ImageView canadaView;
  @FXML private ImageView usaView;
  @FXML private ImageView chinaView;
  @FXML private ImageView russiaView;
  @FXML private Ellipse nzMap;
  @FXML private Ellipse australiaMap;
  @FXML private Ellipse greenlandMapOne;
  @FXML private Rectangle greenlandMapTwo;
  @FXML private Rectangle usaMapOne;
  @FXML private Rectangle usaMapTwo;
  @FXML private Rectangle usaMapThree;
  @FXML private Rectangle usaMapFour;
  @FXML private Rectangle canadaMapOne;
  @FXML private Rectangle canadaMapTwo;
  @FXML private Rectangle canadaMapThree;
  @FXML private Rectangle canadaMapFour;
  @FXML private Rectangle russiaMapOne;
  @FXML private Rectangle russiaMapTwo;
  @FXML private Rectangle russiaMapThree;
  @FXML private Rectangle russiaMapFour;
  @FXML private Rectangle russiaMapFive;
  @FXML private Rectangle russiaMapSix;
  @FXML private Rectangle russiaMapSeven;
  @FXML private Rectangle chinaMapOne;
  @FXML private Ellipse chinaMapTwo;
  @FXML private Rectangle chinaMapThree;
  @FXML private Rectangle chinaMapFour;
  @FXML private Rectangle brazilMapOne;
  @FXML private Rectangle brazilMapTwo;
  @FXML private Rectangle brazilMapThree;
  @FXML private Rectangle argentinaMapOne;
  @FXML private Rectangle argentinaMapTwo;
  @FXML private Rectangle indiaMapOne;
  @FXML private Rectangle indiaMapTwo;
  @FXML private Rectangle indiaMapThree;
  @FXML private Rectangle indiaMapFour;
  @FXML private Label messageText;
  @FXML private ImageView ghost;
  @FXML private Label exitLabel;
  @FXML private ImageView ghost1;
  @FXML private Pane blackboardContainer;
  @FXML private ImageView chatButton;
  @FXML private ImageView ghost2;
  @FXML private Label messageText1;
  @FXML private Rectangle backRect;
  @FXML private Label hiddenNumberOne;
  @FXML private Label hiddenNumberTwo;
  @FXML private Polyline doorRectangle;
  @FXML private Polyline mapRectangle;
  @FXML private Path path;
  @FXML private ImageView ghostSpeechBubble;
  @FXML private Label speechBubbleLabel;
  private Shadow shadow = new Shadow(10, Color.BLACK);
  private Glow glow = new Glow(0.8);
  private boolean playForward = true;
  private boolean ghostMoving = false;
  private boolean isSpeechBubbleShowing = false;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    // Adding timerLabel to synched timer
    GameState.timer.setClass(timerLabel, hiddenNumberOne, hiddenNumberTwo);
    timerLabel.setText(String.format("%02d:%02d", GameState.totalTime / 60, 0));
    addBlackboard();
    // Initialization code goes here
    GameState.roomController = this;
  }

  public void addBlackboard() {
    // Adding the blackboard to the scene
    blackboardContainer.getChildren().add(GameState.blackboardController.getPane());
    chatButton.toFront();
  }

  public void responseLoading() {
    ghost.setEffect(shadow);
    Random random = new Random();
    int randomNumber;
    if (isSpeechBubbleShowing) {
      randomNumber = random.nextInt(3);
      System.out.println("Not moving ghost ");
    } else {
      randomNumber = random.nextInt(4); // Generates a random number 0, 1, 2, 3
      System.out.println("Moving ghost ");
    }

    switch (randomNumber) {
      case 0:
        // Apply the effect to 'room'
        room.setEffect(glow);
        break;
      case 1:
        // Apply the glow effect to 'exitLabel'
        Platform.runLater(
            () -> {
              exitLabel.setEffect(glow);
              ghost1.setVisible(true);
              ghost2.setVisible(true);
              ghost1.toFront();
              ghost2.toFront();
            });
        break;
      case 2:
        // Make the escape message visible
        Platform.runLater(
            () -> {
              messageText.setVisible(true);
              messageText1.setVisible(true);
              messageText.toFront();
              messageText1.toFront();
            });
        break;
      case 3:
        Platform.runLater(
            () -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
      default:
        break;
    }
  }

  public void responseLoaded() {
    // Remove all the effects from room
    Platform.runLater(
        () -> {
          ghost.setEffect(null);
          messageText.setVisible(false);
          room.setEffect(null);
          exitLabel.setEffect(null);
          ghost1.setVisible(false);
          ghost2.setVisible(false);
          messageText1.setVisible(false);
          messageText.toBack();
          messageText1.toBack();
          ghost1.toBack();
          ghost2.toBack();
        });
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
   * Handles the click event on the door.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickDoor(MouseEvent event) {

    if (GameState.isChatOpen) {
      GameState.hallController.openChat();
    }

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.HALLWAY));

    if (!GameState.isChatOpen) {
      // Resizing the window so the scene fits
      sceneRectangleIsIn.getWindow().sizeToScene();
    }

    GameState.hallController.addBlackboard();
  }

  @FXML
  private void onClickChat() {

    // Add the chat to the chat container
    if (!GameState.chatInRoom) {
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

  public void openChat() {
    GameState.chatInGym = false;
    GameState.chatInHall = false;
    GameState.chatInLocker = false;
    chatContainer.getChildren().add(GameState.chatController.getChatPane());
    GameState.chatInRoom = true;
  }

  @FXML
  private void releaseChat() {
    chatButton.setOpacity(1);
  }

  @FXML
  private void enterChatButton() {
    chatButton.setOpacity(0.5);
  }

  @FXML
  private void onClickGhost() {
    if (!GameState.isChatOpen) {
      onClickChat();
      Platform.runLater(() -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
    } else if (!isSpeechBubbleShowing) {
      Platform.runLater(() -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
      ghostMoving = true;
    }
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

  @FXML
  private void enterBackButton() {
    goBackLabel.setOpacity(0.5);
    backRect.setOpacity(0.1);
  }

  @FXML
  private void exitBackButton() {
    goBackLabel.setOpacity(1);
    backRect.setOpacity(0.5);
  }

  @FXML
  public void clickMap() {
    disableWhileMapOpen();
    enableCountries();
    showWhenOnMap();
  }

  @FXML
  public void exitMap() {
    enableWithMapClose();
    disableCounties();
    hideAfterMap();
  }

  @FXML
  public void nzEnter() {
    nzView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void nzExit() {
    nzView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void australiaEnter() {
    australiaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void australiaExit() {
    australiaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void argentinaEnter() {
    argentinaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void argentinaExit() {
    argentinaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void indiaEnter() {
    indiaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void indiaExit() {
    indiaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void greenlandEnter() {
    greenlandView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void greenlandExit() {
    greenlandView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void brazilEnter() {
    brazilView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void brazilExit() {
    brazilView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void canadaEnter() {
    canadaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void canadaExit() {
    canadaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void usaEnter() {
    usaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void usaExit() {
    usaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void chinaEnter() {
    chinaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void chinaExit() {
    chinaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void russiaEnter() {
    russiaView.setVisible(true);
    mapImage.setVisible(false);
  }

  @FXML
  public void russiaExit() {
    russiaView.setVisible(false);
    mapImage.setVisible(true);
  }

  @FXML
  public void nzClicked() {
    isTheCountry("New Zealand");
  }

  @FXML
  public void australiaClicked() {
    isTheCountry("Australia");
  }

  @FXML
  public void argentinaClicked() {
    isTheCountry("Argentina");
  }

  @FXML
  public void usaClicked() {
    isTheCountry("USA");
  }

  @FXML
  public void canadaClicked() {
    isTheCountry("Canada");
  }

  @FXML
  public void brazilClicked() {
    isTheCountry("Brazil");
  }

  @FXML
  public void chinaClicked() {
    isTheCountry("China");
  }

  @FXML
  public void russiaClicked() {
    isTheCountry("Russia");
  }

  @FXML
  public void greenlandClicked() {
    isTheCountry("Greenland");
  }

  @FXML
  public void indiaClicked() {
    isTheCountry("India");
  }

  @FXML
  public void doorEntered() {
    GameState.blackboardController.setHoverText("Hallway Door");
    doorRectangle.setVisible(true);
  }

  @FXML
  public void doorExited() {
    GameState.blackboardController.setHoverText("");
    doorRectangle.setVisible(false);
  }

  @FXML
  public void mapEntered() {
    GameState.blackboardController.setHoverText("Map");
    mapRectangle.setVisible(true);
  }

  @FXML
  public void mapExited() {
    GameState.blackboardController.setHoverText("");
    mapRectangle.setVisible(false);
  }

  public void showWhenOnMap() {
    mapImage.setVisible(true);
    classroomImage.setOpacity(0.5);
    goBackLabel.setVisible(true);
    backRect.setVisible(true);
  }

  public void hideAfterMap() {
    mapImage.setVisible(false);
    classroomImage.setOpacity(1);
    goBackLabel.setVisible(false);
    backRect.setVisible(false);
  }

  public void disableWhileMapOpen() {
    mapEllipse.setDisable(true);
    mapRectangleOne.setDisable(true);
    mapRectangleTwo.setDisable(true);
    door.setDisable(true);
  }

  public void enableWithMapClose() {
    mapEllipse.setDisable(false);
    mapRectangleOne.setDisable(false);
    mapRectangleTwo.setDisable(false);
    door.setDisable(false);
  }

  public void enableCountries() {

    nzMap.setVisible(true);

    australiaMap.setVisible(true);
    // Enabling argentina
    argentinaMapOne.setVisible(true);
    argentinaMapTwo.setVisible(true);
    // Enabling india
    indiaMapOne.setVisible(true);
    indiaMapTwo.setVisible(true);
    indiaMapThree.setVisible(true);
    indiaMapFour.setVisible(true);
    // Enabling greenland
    greenlandMapOne.setVisible(true);
    greenlandMapTwo.setVisible(true);
    // Enabling brazil
    brazilMapOne.setVisible(true);
    brazilMapTwo.setVisible(true);
    brazilMapThree.setVisible(true);
    // Enabling canada
    canadaMapOne.setVisible(true);
    canadaMapTwo.setVisible(true);
    canadaMapThree.setVisible(true);
    canadaMapFour.setVisible(true);
    // Enabling usa
    usaMapOne.setVisible(true);
    usaMapTwo.setVisible(true);
    usaMapThree.setVisible(true);
    usaMapFour.setVisible(true);
    // Enabling china
    chinaMapOne.setVisible(true);
    chinaMapTwo.setVisible(true);
    chinaMapThree.setVisible(true);
    chinaMapFour.setVisible(true);
    // Enabling russia
    russiaMapOne.setVisible(true);
    russiaMapTwo.setVisible(true);
    russiaMapThree.setVisible(true);
    russiaMapFour.setVisible(true);
    russiaMapFive.setVisible(true);
    russiaMapSix.setVisible(true);
    russiaMapSeven.setVisible(true);
  }

  public void disableCounties() {
    // Disabling New Zealand
    nzMap.setVisible(false);
    // Disabling australia
    australiaMap.setVisible(false);
    // Disabling argentina
    argentinaMapOne.setVisible(false);
    argentinaMapTwo.setVisible(false);
    // Disabling india
    indiaMapOne.setVisible(false);
    indiaMapTwo.setVisible(false);
    indiaMapThree.setVisible(false);
    indiaMapFour.setVisible(false);
    // Disabling greenland
    greenlandMapOne.setVisible(false);
    greenlandMapTwo.setVisible(false);
    // Disabling brazil
    brazilMapOne.setVisible(false);
    brazilMapTwo.setVisible(false);
    brazilMapThree.setVisible(false);
    // Disabling canada
    canadaMapOne.setVisible(false);
    canadaMapTwo.setVisible(false);
    canadaMapThree.setVisible(false);
    canadaMapFour.setVisible(false);
    // Disabling usa
    usaMapOne.setVisible(false);
    usaMapTwo.setVisible(false);
    usaMapThree.setVisible(false);
    usaMapFour.setVisible(false);
    // Disabling china
    chinaMapOne.setVisible(false);
    chinaMapTwo.setVisible(false);
    chinaMapThree.setVisible(false);
    chinaMapFour.setVisible(false);
    // Disabling russia
    russiaMapOne.setVisible(false);
    russiaMapTwo.setVisible(false);
    russiaMapThree.setVisible(false);
    russiaMapFour.setVisible(false);
    russiaMapFive.setVisible(false);
    russiaMapSix.setVisible(false);
    russiaMapSeven.setVisible(false);
  }

  public boolean isTheCountry(String country) {
    // Checking to see if the country is the one to find
    if (GameState.isRiddleResolved
        && !GameState.countryIsFound
        && GameState.countryToFind.equals(country)) {
      GameState.countryIsFound = true;
      // Updating the game play to reflect the country being found
      GameState.textFlow.getChildren().clear();
      GameState.blackboardController.showHallpass();
      GameState.blackboardController.showItemLabel();
      GameState.blackboardController.setObjectiveText("Objective: Look around the School");
      GameState.lockerController.setQuestion();
      GameState.isGhostTalking = true;
      if (!playForward) {
        Platform.runLater(
            () -> playForward = GameState.moveGhost(ghost, path, playForward, shadow));
        isSpeechBubbleShowing = true;
        // Create a PauseTransition for 4 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(
            event -> {
              // After 4 seconds, set speech bubble
              setSpeechBubble("Hallpass Found");
              GameState.isGhostTalking = false;
            });

        // Start the pause transition
        pause.play();
      } else {
        setSpeechBubble("Hallpass Found");
      }
      // Changing the chat to the next state
      GameState.chatController.sayFact();
      GameState.currentState = "state3";
      GameState.chatController.newStateHint();
      if (!GameState.isChatOpen) {
        onClickChat();
      }
      GameState.isGhostTalking = false;
      return true;
    }
    return false;
  }

  private void setSpeechBubble(String text) {
    isSpeechBubbleShowing = true;
    Platform.runLater(
        () -> {
          speechBubbleLabel.setText(text);
          ghostSpeechBubble.toFront();
          speechBubbleLabel.toFront();
          ghostSpeechBubble.setVisible(true);
          speechBubbleLabel.setVisible(true);
        });

    // Create a PauseTransition for 4 seconds
    PauseTransition pause = new PauseTransition(Duration.seconds(8));
    pause.setOnFinished(
        event -> {
          // After 4 seconds, send the speech bubble and label to the back
          Platform.runLater(
              () -> {
                ghostSpeechBubble.toBack();
                speechBubbleLabel.toBack();
                ghostSpeechBubble.setVisible(false);
                speechBubbleLabel.setVisible(false);
                isSpeechBubbleShowing = false;
              });
        });

    // Start the pause transition
    pause.play();
  }

  /** Fades the scene in */
  public void fadeIn() {
    FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), room);
    room.setOpacity(0);
    fadeTransitionIn.setFromValue(0);
    fadeTransitionIn.setToValue(1);
    fadeTransitionIn.play();
  }
}
