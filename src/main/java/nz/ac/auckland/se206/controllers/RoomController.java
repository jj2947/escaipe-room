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

    argentinaMapOne.setVisible(true);
    argentinaMapTwo.setVisible(true);

    indiaMapOne.setVisible(true);
    indiaMapTwo.setVisible(true);
    indiaMapThree.setVisible(true);
    indiaMapFour.setVisible(true);

    greenlandMapOne.setVisible(true);
    greenlandMapTwo.setVisible(true);

    brazilMapOne.setVisible(true);
    brazilMapTwo.setVisible(true);
    brazilMapThree.setVisible(true);

    canadaMapOne.setVisible(true);
    canadaMapTwo.setVisible(true);
    canadaMapThree.setVisible(true);
    canadaMapFour.setVisible(true);

    usaMapOne.setVisible(true);
    usaMapTwo.setVisible(true);
    usaMapThree.setVisible(true);
    usaMapFour.setVisible(true);

    chinaMapOne.setVisible(true);
    chinaMapTwo.setVisible(true);
    chinaMapThree.setVisible(true);
    chinaMapFour.setVisible(true);

    russiaMapOne.setVisible(true);
    russiaMapTwo.setVisible(true);
    russiaMapThree.setVisible(true);
    russiaMapFour.setVisible(true);
    russiaMapFive.setVisible(true);
    russiaMapSix.setVisible(true);
    russiaMapSeven.setVisible(true);
  }

  public void disableCounties() {
    nzMap.setVisible(false);

    australiaMap.setVisible(false);

    argentinaMapOne.setVisible(false);
    argentinaMapTwo.setVisible(false);

    indiaMapOne.setVisible(false);
    indiaMapTwo.setVisible(false);
    indiaMapThree.setVisible(false);
    indiaMapFour.setVisible(false);

    greenlandMapOne.setVisible(false);
    greenlandMapTwo.setVisible(false);

    brazilMapOne.setVisible(false);
    brazilMapTwo.setVisible(false);
    brazilMapThree.setVisible(false);

    canadaMapOne.setVisible(false);
    canadaMapTwo.setVisible(false);
    canadaMapThree.setVisible(false);
    canadaMapFour.setVisible(false);

    usaMapOne.setVisible(false);
    usaMapTwo.setVisible(false);
    usaMapThree.setVisible(false);
    usaMapFour.setVisible(false);

    chinaMapOne.setVisible(false);
    chinaMapTwo.setVisible(false);
    chinaMapThree.setVisible(false);
    chinaMapFour.setVisible(false);

    russiaMapOne.setVisible(false);
    russiaMapTwo.setVisible(false);
    russiaMapThree.setVisible(false);
    russiaMapFour.setVisible(false);
    russiaMapFive.setVisible(false);
    russiaMapSix.setVisible(false);
    russiaMapSeven.setVisible(false);
  }
}
