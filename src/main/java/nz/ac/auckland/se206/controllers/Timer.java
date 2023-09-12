package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;

public class Timer {
  private int counter;
  private Label classroomLabel;
  private Label hallwayLabel;
  private Label gymLabel;
  private Label lockerLabel;

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

            if (counter == 0) {
              GameState.isTimeReached = true;
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

  // Everyone second the timerlabels in the different scenes are updated
  private Timeline timeline =
      new Timeline(
          new KeyFrame(
              Duration.seconds(1),
              e -> {
                updateLabels();
              }));

  public void updateLabels() {
    String timeCurrent =
        String.format(
            "%d:%02d", GameState.timer.getCounter() / 60, GameState.timer.getCounter() % 60);
    classroomLabel.setText(timeCurrent);
    hallwayLabel.setText(timeCurrent);
    gymLabel.setText(timeCurrent);
    lockerLabel.setText(timeCurrent);
  }
}
