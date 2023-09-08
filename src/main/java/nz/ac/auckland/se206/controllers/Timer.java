package nz.ac.auckland.se206.controllers;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;

public class Timer {
  private int counter;
  private Label timerLabel;

  public Timer(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  public void startTimer() {
    counter =
        GameState.totalTime; // Setting value to the number of time choosen at the start of the game
    updateLabel();

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

  private void updateLabel() {
    timerLabel.setText(String.format("%02d:%02d", counter / 60, counter % 60));
  }

  public int getCounter() {
    return counter;
  }

  public Timer getTimer() {
    return this;
  }
}
