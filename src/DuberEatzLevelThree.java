import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * DuberEatzLevelThree.java
 * Ethan Zhang
 * November 6, 2019
 * This code solve mazes with one or more destinations, finding the least amount of steps required and printing the solved maze with a trail.
 * This code is incompatible with destinations that are not the number one.
 */

public class DuberEatzLevelThree {
  public static boolean initialization = true; //For setting the very first amount of steps to be compared to.
  public static int minimum; //Minimum steps.
  public static String[][] optimal; //Optimal path.
  public static boolean[][] trail; //For printing trail.
  public static void main(String[] args) throws Exception {
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
    int result = solve(map, visits, yCoor, xCoor, 0);
    System.out.print("\n\nSolved Map:");
    for (int i = 0; i < length; i++) {
      System.out.print("\n");
      for (int j = 0; j < width; j++) {
        if (trail[i][j] == true) { //If a tile has been visited, mark it as part of the trail to compensate for erased trails.
          optimal[i][j] = "x";
        }
        System.out.print(optimal[i][j]);
      }
    }
    System.out.print("\nThe quickest route takes " + minimum + " moves.");
    input.close();
    fileInput.close();
  }
  
/**
 * solve
 * This method recursively solves the provided maze, saving the optimal path and its trail to the global variables.
 * The method returns a zero if it reaches a dead-end and recursively calls itself if it is able to take a step in any direction.
 * @Param map, a two-dimensional string array that stores the entire map at the current moment.
 * @Param visits, a two-dimensional boolean array that stores the tiles that have been visited.
 * @Param yCoor, an integer that stores the current y-coordinate of Duber.
 * @Param xCoor, an integer that stores the current x-coordinate of Duber.
 * @Param moves, an integer that stores the number of steps taken by Duber.
 * @Return 0 if Duber reaches a dead-end and recursively call otherwise.
 */
  
  public static int solve(String[][] map, boolean[][] visits, int yCoor, int xCoor, int moves) {
    int up, down, left, right;
    String[][] dupe = copy(map); //Duplicate map each step in order to try all possibilities.
    boolean[][] track = duplicate(visits); //Keep track of visited tiles.
    if (dupe[yCoor][xCoor].equals("1")) {
      boolean completion = true;
      dupe[yCoor][xCoor] = "X";
      for (int i = 0; i < dupe.length; i++) {
        for (int j = 0; j < dupe[i].length; j++) {
          if (dupe[i][j].equals("1")) { //Check if the map has any remaining destinations.
            completion = false;
          }
        }
      }
      if (completion == true) {
        if (initialization == true) { //First initialization of the least amount of moves, acting as a comparison point.
          minimum = moves;
          for (int i = 0; i < dupe.length; i++) {
            for (int j = 0; j < dupe[i].length; j++) {
              optimal[i][j] = dupe[i][j];
              trail[i][j] = track[i][j];
            }
          }
          initialization = false;
        } else {
          if (moves < minimum) {
            minimum = moves;
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
        return solve(dupe, track, yCoor, xCoor, moves); //Then, recursively call itself again with its position at the reached destination.
      }
    }
    if (!((dupe[yCoor][xCoor].equals("X")) || (dupe[yCoor][xCoor].equals("S")))) { //Trailing.
      track[yCoor][xCoor] = true; //Set tile as visited.
      dupe[yCoor][xCoor] = "x";
    }
    if (!((dupe[yCoor - 1][xCoor].equals("#")) || (dupe[yCoor - 1][xCoor].equals("x")) || (dupe[yCoor - 1][xCoor].equals("X")) || (dupe[yCoor - 1][xCoor].equals("S")))) {
      up = solve(dupe, track, yCoor - 1, xCoor, moves + 1); //Traverse to tile above.
    }
    if (!((dupe[yCoor + 1][xCoor].equals("#")) || (dupe[yCoor + 1][xCoor].equals("x")) || (dupe[yCoor + 1][xCoor].equals("X")) || (dupe[yCoor + 1][xCoor].equals("S")))) {
      down = solve(dupe, track, yCoor + 1, xCoor, moves + 1); //Traverse to tile below.
    }
    if (!((dupe[yCoor][xCoor - 1].equals("#")) || (dupe[yCoor][xCoor - 1].equals("x")) || (dupe[yCoor][xCoor - 1].equals("X")) || (dupe[yCoor][xCoor - 1].equals("S")))) {
      left = solve(dupe, track, yCoor, xCoor - 1, moves + 1); //Traverse to left tile.
    }
    if (!((dupe[yCoor][xCoor + 1].equals("#")) || (dupe[yCoor][xCoor + 1].equals("x")) || (dupe[yCoor][xCoor + 1].equals("X")) || (dupe[yCoor][xCoor + 1].equals("S")))) {
      right = solve(dupe, track, yCoor, xCoor + 1, moves + 1); //Traverse to right tile.
    }
    return 0;
  }

/**
 * copy
 * This method creates copies of the current map so that the recursive method is able to try all possibilities.
 * This method returns a copy of the current map with trails.
 * @Param map, a two-dimensional string array that stores the current map.
 * @Return the copied map.
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
 * @Param visits, a two-dimensional boolean array that stores visited tiles.
 * @Return the copied visited tiles.
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