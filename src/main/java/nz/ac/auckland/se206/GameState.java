package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates whether time is up. */
  public static boolean isTimeReached = false;

  /** Indicates whether room has been loaded */
  public static boolean roomIsLoaded = false;

  /** Indicated whether chat has already been loaded */
  public static boolean chatIsLoaded = false;

  /** Indicates whether hallway has been loaded */
  public static boolean hallwayIsLoaded = false;

  /** Indicates whether gymnasium has been loaded */
  public static boolean gymnasiumIsLoaded = false;

  /** Represents number of hints left, dependant on used and difficulty */
  public static int numberOfHints;

  /** The time choosen by the user */
  public static int totalTime;
}
