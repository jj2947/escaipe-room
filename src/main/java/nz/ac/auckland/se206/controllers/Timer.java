package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Timer {
  private int counter;
  private Scene currentScene;
  private Label classroomLabel;
  private Label hallwayLabel;
  private Label gymLabel;
  private Label lockerLabel;

  // Everyone second the timerlabels in the different scenes are updated
  private Timeline timeline =
      new Timeline(
          new KeyFrame(
              Duration.seconds(1),
              e -> {
                if (counter == 0) {
                  GameState.isTimeReached = true;
                  timeIsUp();
                }
                updateLabels();
              }));

  public Timer() {}

  public void startTimer() {
    counter =
        GameState.totalTime; // Setting value to the number of time choosen at the start of the game
    // updateLabel();
    counter--;

    Task<Void> backgroundTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            while (counter > 0 && !GameState.isTimeReached) {
              Thread.sleep(1000); // Wait for 1 second
              counter--;
            }

            return null;
          }
        };

    // Create a new thread for the countdown and start it
    Thread countdownThread = new Thread(backgroundTask);
    countdownThread.start();
  }

  public int getCounter() {
    return counter;
  }

  public Timer getTimer() {
    return this;
  }

  public void setCounter(int time) {
    // Allows pre-set of time
    counter = time;
  }

  public void setClass(Label lab) {
    // Timerlabel in Classroom
    classroomLabel = lab;
    update();
  }

  public void setHall(Label lab) {
    // Timerlabel in Hallway
    hallwayLabel = lab;
    update();
  }

  public void setGym(Label lab) {
    // Timerlabel in Gym
    gymLabel = lab;
    update();
  }

  public void setLocker(Label lab) {
    lockerLabel = lab;
    update();
  }

  public void update() {
    // If all three rooms have been loaded the timeline is started synching there label changes
    if (classroomLabel != null && hallwayLabel != null && gymLabel != null && lockerLabel != null) {
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
    }
  }

  public void updateLabels() {
    // Formatted time and then setting all the timer labels to that time
    String timeCurrent = String.format("%d:%02d", counter / 60, counter % 60);
    classroomLabel.setText(timeCurrent);
    hallwayLabel.setText(timeCurrent);
    gymLabel.setText(timeCurrent);
    lockerLabel.setText(timeCurrent);
  }

  public void timeIsUp() {
    // Finding out which scene we are currently in
    if (classroomLabel.getScene() != null) {
      currentScene = classroomLabel.getScene();
    } else if (hallwayLabel.getScene() != null) {
      currentScene = hallwayLabel.getScene();
    } else if (gymLabel.getScene() != null) {
      currentScene = gymLabel.getScene();
    } else if (lockerLabel.getScene() != null) {
      currentScene = lockerLabel.getScene();
    }

    // Switching to the end scene
    Platform.runLater(
        () -> {
          try {
            SceneManager.addUi(AppUi.END, App.loadFxml("end"));
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          currentScene.setRoot(SceneManager.getUiRoot(AppUi.END));
          currentScene.getWindow().sizeToScene();
        });
  }
}
