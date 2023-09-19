package nz.ac.auckland.se206.controllers;

import java.util.Map;

public class CountryChoice {
  protected static String countryChoice;
  private int random;

  // Initialising Map of countries
  private Map<Integer, String> countries =
      Map.of(
          1, "New Zealand",
          2, "Australia",
          3, "USA",
          4, "Canada",
          5, "Russia",
          6, "China",
          7, "Brazil",
          8, "India",
          9, "Greenland",
          10, "Argentina");

  public String chooseCountry() {
    if (countryChoice == null) {
      random = (int) (Math.random() * 10) + 1; // Getting random number between 1 and 10
      countryChoice = countries.get(random); // Getting country name from map
      return countryChoice;
    }
    return null;
  }
}
