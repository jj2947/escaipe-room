package nz.ac.auckland.se206;

import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.HallwayController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.Timer;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates whether time is up. */
  public static boolean isTimeReached = false;

  /** Represents number of hints left, dependant on used and difficulty */
  public static int numberOfHints;

  /** The time choosen by the user, set early for some reason cant remember rn */
  public static int totalTime = 10000;

  public static Timer timer;

  public static ChatController chatController;

  public static HallwayController hallController;

  public static RoomController roomController;

  public static boolean chatInRoom = false;

  public static boolean chatInHall = false;;

  public static boolean chatInLocker = false;

  public static boolean chatInGym = false;

  public static boolean isChatOpen = false;
}
