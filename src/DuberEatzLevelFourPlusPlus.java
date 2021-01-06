import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * DuberEatzLevelFourPlusPlus.java
 * Ethan Zhang
 * November 6, 2019
 * This code solve mazes with one or more destinations, finding the least amount of moves required
 * to maximize tips with microwaves taken into account and then creating a PPM file for the solved maze.
 */

public class DuberEatzLevelFourPlusPlus {
  public static boolean initialization = true; //For setting the very first amount of steps and tips to be compared to.
  public static int minimum, maximum; //Minimum steps and maximum tips.
  public static String[][] optimal; //Optimal path.
  public static boolean[][] trail; //For printing trail.
  public static void main(String[] args) throws Exception {
    File pixmap = new File("solution.ppm");
    PrintWriter outputPPM = new PrintWriter(pixmap);
    Scanner input = new Scanner(System.in);
    int yCoor = 0, xCoor = 0;
    System.out.print("Enter a file name: "); //Make sure to enter the file name exclusively; do not enter the file type.
    String text = input.next() + ".txt";
    File read = new File(text);
    Scanner fileInput = new Scanner(read);
    int length = fileInput.nextInt();
    int width = fileInput.nextInt();
    fileInput.nextLine();
    optimal = new String[length][width];
    trail = new boolean[length][width];
    boolean[][] visits = new boolean[length][width];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        visits[i][j] = false;
      }
    }
    String[] lines = new String[length];
    for (int i = 0; i < length; i++) {
      lines[i] = fileInput.nextLine();
    }
    String[][] map = new String[length][width];
    System.out.print("Unsolved Map:");
    for (int i = 0; i < length; i++) {
      System.out.print("\n");
      for (int j = 0; j < width; j++) {
        map[i][j] = lines[i].substring(j, j + 1);
        if (map[i][j].equals("S")) { //Starting position coordinates.
          yCoor = i;
          xCoor = j;
        }
        System.out.print(map[i][j]);
      }
    }
    int result = solve(map, visits, yCoor, xCoor, 0, 0, 0);
    outputPPM.print("P3\n" + (width * 10) + " " + (length * 10) + "\n255\n");
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < 10; j++) { //The length of each PPM map tile is ten pixels.
        for (int k = 0; k < width; k++) {
          for (int l = 0; l < 10; l++) { //The width of each PPM map tile is ten pixels.
            if (optimal[i][k].equals("U")) {
              optimal[i][k] = "M";
            } else if (trail[i][k] == true) { //If a tile has been visited, mark it as part of the trail to compensate for erased trails.
              optimal[i][k] = "x";
            }
            if (optimal[i][k].equals("S")) {
              outputPPM.print("100 220 120 ");
            } else if (optimal[i][k].equals("X")) {
              outputPPM.print("100 160 220 ");
            } else if (optimal[i][k].equals("x")) {
              outputPPM.print("240 50 50 ");
            } else if (optimal[i][k].equals(" ")) {
              outputPPM.print("160 160 160 ");
            } else if (optimal[i][k].equals("M")) {
              outputPPM.print("250 230 30 ");
            } else {
              outputPPM.print("0 0 0 ");
            }
          }
        }
      }
    }
    System.out.print("\n\nSolved Map:");
    for (int i = 0; i < length; i++) {
      System.out.print("\n");
      for (int j = 0; j < width; j++) {
        System.out.print(optimal[i][j]);
      }
    }
    System.out.print("\nThe quickest route with maximized tips takes " + minimum + " moves, earning " + maximum + " tips.");
    outputPPM.close();
    input.close();
    fileInput.close();
  }

 /**
 * solve
 * This method recursively solves the provided maze, saving the optimal path for tips with microwaves taken into account and its trail to the global variables.
 * The method returns a zero if it reaches a dead-end and recursively calls itself if it is able to take a step in any direction.
 * @param map, a two-dimensional string array that stores the entire map at the current moment.
 * @param visits, a two-dimensional boolean array that stores the tiles that have been visited.
 * @param yCoor, an integer that stores the current y-coordinate of Duber.
 * @param xCoor, an integer that stores the current x-coordinate of Duber.
 * @param moves, an integer that stores the number of steps that can be influenced by microwaves taken by Duber.
 * @param total, an integer that stores the total number of steps taken from the starting position by Duber.
 * @param tips, an integer that stores the total number of tips earned by Duber.
 * @return 0 if Duber reaches a dead-end and recursively call otherwise.
 */

  public static int solve(String[][] map, boolean[][] visits, int yCoor, int xCoor, int moves, int total, int tips) {
    int up, down, left, right;
    boolean identity = false; //For determining if the current tile is a destination.
    String[][] dupe = copy(map); //Duplicate map each step in order to try all possibilities.
    boolean[][] track = duplicate(visits); //Keep track of visited tiles.
    for (int i = 0; i < 10; i++) {
      if (dupe[yCoor][xCoor].equals(Integer.toString(i))) {
        identity = true;
      }
    }
    if (identity == true) {
      boolean completion = true;
      if ((Integer.parseInt(dupe[yCoor][xCoor]) - moves) > 0) {
        tips += (Integer.parseInt(dupe[yCoor][xCoor]) - moves) * 10;
      } else {
        tips += Integer.parseInt(dupe[yCoor][xCoor]) - moves;
      }
      dupe[yCoor][xCoor] = "X";
      for (int i = 0; i < dupe.length; i++) {
        for (int j = 0; j < dupe[i].length; j++) {
          for (int k = 0; k < 10; k++) {
            if (dupe[i][j].equals(Integer.toString(k))) {
              completion = false;
            }
          }
        }
      }
      if (completion == true) {
        if (initialization == true) { //First initialization of the least amount of moves and most amount of tips, acting as a comparison point.
          minimum = moves;
          maximum = tips;
          for (int i = 0; i < dupe.length; i++) {
            for (int j = 0; j < dupe[i].length; j++) {
              optimal[i][j] = dupe[i][j];
              trail[i][j] = track[i][j];
            }
          }
          initialization = false;
        } else {
          if (tips > maximum) { //Search for most tips instead of least moves.
            minimum = moves;
            maximum = tips;
            for (int i = 0; i < dupe.length; i++) {
              for (int j = 0; j < dupe[i].length; j++) {
                optimal[i][j] = dupe[i][j];
                trail[i][j] = track[i][j];
              }
            }
          }
        }
      } else { //If the map is still incomplete, clear the map of trails to prevent the program from thinking it has hit a dead-end.
        for (int i = 0; i < dupe.length; i++) {
          for (int j = 0; j < dupe[i].length; j++) {
            if (dupe[i][j].equals("x")) {
              dupe[i][j] = " ";
            }
          }
        }
        return solve(dupe, track, yCoor, xCoor, moves, total, tips); //Then, recursively call itself again with its position at the reached destination.
      }
    }
    if (dupe[yCoor][xCoor].equals("M")) {
      dupe[yCoor][xCoor] = "U"; //Mark the microwave as used.
      for (int i = 0; i < dupe.length; i++) { //Clear the map of trails to prevent the program from thinking it has hit a dead-end.
        for (int j = 0; j < dupe.length; j++) {
          if (dupe[i][j].equals("x")) {
            dupe[i][j] = " ";
          }
        }
      }
      return solve(dupe, track, yCoor, xCoor, total / 2, total, tips); //Then, recursively call itself again with its decreased move count.
    }
    if (!((dupe[yCoor][xCoor].equals("X")) || (dupe[yCoor][xCoor].equals("S")) || (dupe[yCoor][xCoor].equals("U")))) {
      track[yCoor][xCoor] = true; //Set tile as visited.
      dupe[yCoor][xCoor] = "x";
    }
    if (!((dupe[yCoor - 1][xCoor].equals("#")) || (dupe[yCoor - 1][xCoor].equals("x")) || (dupe[yCoor - 1][xCoor].equals("X")) || (dupe[yCoor - 1][xCoor].equals("S")))) {
      up = solve(dupe, track, yCoor - 1, xCoor, moves + 1, total + 1, tips); //Traverse to tile above.
    }
    if (!((dupe[yCoor + 1][xCoor].equals("#")) || (dupe[yCoor + 1][xCoor].equals("x")) || (dupe[yCoor + 1][xCoor].equals("X")) || (dupe[yCoor + 1][xCoor].equals("S")))) {
      down = solve(dupe, track, yCoor + 1, xCoor, moves + 1, total + 1, tips); //Traverse to tile below.
    }
    if (!((dupe[yCoor][xCoor - 1].equals("#")) || (dupe[yCoor][xCoor - 1].equals("x")) || (dupe[yCoor][xCoor - 1].equals("X")) || (dupe[yCoor][xCoor - 1].equals("S")))) {
      left = solve(dupe, track, yCoor, xCoor - 1, moves + 1, total + 1, tips); //Traverse to left tile.
    }
    if (!((dupe[yCoor][xCoor + 1].equals("#")) || (dupe[yCoor][xCoor + 1].equals("x")) || (dupe[yCoor][xCoor + 1].equals("X")) || (dupe[yCoor][xCoor + 1].equals("S")))) {
      right = solve(dupe, track, yCoor, xCoor + 1, moves + 1, total + 1, tips); //Traverse to right tile.
    }
    return 0;
  }
  
 /**
 * copy
 * This method creates copies of the current map so that the recursive method is able to try all possibilities.
 * This method returns a copy of the current map with trails.
 * @param map, a two-dimensional string array that stores the current map.
 * @return the copied map.
 */
  
  public static String[][] copy(String[][] map) {
    String[][] replica = new String[map.length][map[0].length];
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        replica[i][j] = map[i][j];
      }
    }
    return replica;
  }
  
 /**
 * duplicate
 * This method creates copies of all the current visited tiles, keeping track of the trail.
 * This method returns a copy of the current visited tiles.
 * @param visits, a two-dimensional boolean array that stores visited tiles.
 * @return the copied visited tiles.
 */
  
  public static boolean[][] duplicate(boolean[][] visits) {
    boolean[][] replica = new boolean[visits.length][visits[0].length];
    for (int i = 0; i < visits.length; i++) {
      for (int j = 0; j < visits[i].length; j++) {
        replica[i][j] = visits[i][j];
      }
    }
    return replica;
  }
}