package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** This is the timer class. It is used to keep track of the time left in the game. */
public class Timer {
  private int counter = -1;
  private Scene currentScene;
  private Label classroomLabel;
  private Label hallwayLabel;
  private Label gymLabel;
  private Label lockerLabel;
  private Timeline timeline;
  private Thread countdownThread;
  private TextToSpeech textToSpeech;
  private List<Label> placesToHide = new ArrayList<>();

  /**
   * This is the constructor for the timer class. It creates a new text to speech object and a new
   * timeline.
   */
  public Timer() {
    textToSpeech = new TextToSpeech();
    // Everyone second the timerlabels in the different scenes are updated
    timeline =
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
  }

  /** Starts the timer. It is called when the game starts. */
  public void startTimer() {
    counter =
        GameState.totalTime; // Setting value to the number of time choosen at the start of the game
    counter--;
    Task<Void> backgroundTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            while (counter > 0 && !GameState.isTimeReached) {
              Thread.sleep(1000); // Wait for 1 second
              counter--;
              startTextToSpeech();
            }

            return null;
          }
        };

    // Create a new thread for the countdown and start it
    countdownThread = new Thread(backgroundTask);
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

  /**
   * Sets the timer labels in the classroom scene.
   *
   * @param lab the classroom timer label
   * @param lab2 place to hide number in classroom
   * @param lab3 place to hide number in classroom
   */
  public void setClass(Label lab, Label lab2, Label lab3) {
    // Timerlabel in Classroom
    classroomLabel = lab;
    placesToHide.add(lab2);
    placesToHide.add(lab3);
    update();
  }

  /**
   * Sets the timer labels in the hallway scene.
   *
   * @param lab the hallway timer label
   * @param lab2 place to hide number in hallway
   * @param lab3 place to hide number in hallway
   */
  public void setHall(Label lab, Label lab2, Label lab3) {
    // Timerlabel in Hallway
    hallwayLabel = lab;
    placesToHide.add(lab2);
    placesToHide.add(lab3);
    update();
  }

  /**
   * Sets the timer labels in the gym scene.
   *
   * @param lab the gym timer label
   * @param lab2 place to hide number in gym
   * @param lab3 place to hide number in gym
   */
  public void setGym(Label lab, Label lab2, Label lab3) {
    // Timerlabel in Gym
    gymLabel = lab;
    placesToHide.add(lab2);
    placesToHide.add(lab3);
    update();
  }

  public void setLocker(Label lab) {
    lockerLabel = lab;
    update();
  }

  /** Updates the timer labels in the different scenes. */
  public void update() {
    // If all three rooms have been loaded the timeline is started synching there label changes
    if (classroomLabel != null && hallwayLabel != null && gymLabel != null && lockerLabel != null) {
      int randomNum1 = (int) Math.floor(Math.random() * (2 - 0 + 1) + 0);
      int randomNum2 = (int) Math.floor(Math.random() * (5 - 3 + 1) + 3);
      System.out.println("first val: " + randomNum1);
      System.out.println("sec val: " + randomNum2);
      placesToHide.get(randomNum1).setText(Integer.toString(GameState.numbersToFind.get(0)));
      placesToHide.get(randomNum2).setText(Integer.toString(GameState.numbersToFind.get(1)));
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
    }
  }

  /** Updates the timer labels in the different scenes. */
  public void updateLabels() {
    // Formatted time and then setting all the timer labels to that time
    String timeCurrent = String.format("%d:%02d", counter / 60, counter % 60);
    classroomLabel.setText(timeCurrent);
    hallwayLabel.setText(timeCurrent);
    gymLabel.setText(timeCurrent);
    lockerLabel.setText(timeCurrent);
  }

  /** Method that is called when the timer reaches 0. Switches to the end scene. */
  public void timeIsUp() {
    timeline.stop();
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
            e.printStackTrace();
          }
          currentScene.setRoot(SceneManager.getUiRoot(AppUi.END));
          currentScene.getWindow().sizeToScene();
          Stage stage = (Stage) currentScene.getWindow();
          stage.centerOnScreen();
        });
  }

  /** Method that controls the text to speech. */
  private void startTextToSpeech() {
    if (GameState.isMuted) {
      return;
    }
    if ((counter % 30 == 0 && counter != 0) || counter == 10 || counter == 5) {
      Task<Void> speakTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // The code for generating the sentence (as shown in the previous response)
              String sentence;

              int minutes = counter / 60;
              int seconds = counter % 60;

              String minuteString = (minutes == 1) ? "minute" : "minutes";

              if (minutes > 0 && seconds > 0) {
                sentence = String.format("%d %s %d seconds", minutes, minuteString, seconds);
              } else if (minutes > 0) {
                sentence = String.format("%d %s", minutes, minuteString);
              } else {
                sentence = String.format("%d seconds", seconds);
              }
              textToSpeech.speak(sentence);
              return null;
            }
          };

      // Start textToSpeech task in a new thread
      Thread speakThread = new Thread(speakTask);
      speakThread.setDaemon(true);
      speakThread.start();
    }
  }

  /** Resets the timer. It is called when the user restarts the game. */
  public void reset() {
    counter = -1;
    if (countdownThread != null && countdownThread.isAlive()) {
      countdownThread.interrupt();
    }
    if (timeline != null) {
      timeline.stop();
    }
  }

  /** Exits the game. It is called when the user wants to exit the game. */
  public void exitGame() {
    if (textToSpeech != null) {
      textToSpeech.terminate();
    }
  }
}
