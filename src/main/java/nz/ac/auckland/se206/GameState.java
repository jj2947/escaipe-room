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
}
