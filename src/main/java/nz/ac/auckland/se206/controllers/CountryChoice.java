package nz.ac.auckland.se206.controllers;

import java.util.Map;

public class CountryChoice {
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
      random = (int) (Math.random() * 10) + 1; // Getting random number between 1 and 10
      String countryChoice = countries.get(random); // Getting country name from map
      return countryChoice;
  }
}
