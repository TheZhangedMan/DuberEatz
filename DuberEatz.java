import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * DuberEatz.java
 * Ethan Zhang
 * November 6, 2019
 * This code solve mazes with one or more destinations, finding the least amount of moves required
 * to maximize tips with microwaves taken into account and then creating a PPM file for the solved maze.
 */

public class DuberEatz {
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
    for (int k = 0; k < length; k++) {
      lines[k] = fileInput.nextLine();
    }
    String[][] map = new String[length][width];
    System.out.print("Unsolved Map:");
    for (int l = 0; l < length; l++) {
      System.out.print("\n");
      for (int m = 0; m < width; m++) {
        map[l][m] = lines[l].substring(m, m + 1);
        if (map[l][m].equals("S")) { //Starting position coordinates.
          yCoor = l;
          xCoor = m;
        }
        System.out.print(map[l][m]);
      }
    }
    int result = solve(map, visits, yCoor, xCoor, 0, 0, 0);
    outputPPM.print("P3\n" + (width * 10) + " " + (length * 10) + "\n255\n");
    for (int d = 0; d < length; d++) {
      for (int e = 0; e < 10; e++) { //The length of each PPM map tile is ten pixels.
        for (int f = 0; f < width; f++) {
          for (int g = 0; g < 10; g++) { //The width of each PPM map tile is ten pixels.
            if (optimal[d][f].equals("U")) {
              optimal[d][f] = "M";
            } else if (trail[d][f] == true) { //If a tile has been visited, mark it as part of the trail to compensate for erased trails.
              optimal[d][f] = "x";
            }
            if (optimal[d][f].equals("S")) {
              outputPPM.print("100 220 120 ");
            } else if (optimal[d][f].equals("X")) {
              outputPPM.print("100 160 220 ");
            } else if (optimal[d][f].equals("x")) {
              outputPPM.print("240 50 50 ");
            } else if (optimal[d][f].equals(" ")) {
              outputPPM.print("160 160 160 ");
            } else if (optimal[d][f].equals("M")) {
              outputPPM.print("250 230 30 ");
            } else {
              outputPPM.print("0 0 0 ");
            }
          }
        }
      }
    }
    System.out.print("\n\nSolved Map:");
    for (int h = 0; h < length; h++) {
      System.out.print("\n");
      for (int i = 0; i < width; i++) {
        System.out.print(optimal[h][i]);
      }
    }
    System.out.print("\nThe quickest route with maximized tips takes " + minimum + " moves, earning " + maximum + " tips.");
    outputPPM.close();
    input.close();
    fileInput.close();
    
 /**
 * solve
 * This method recursively solves the provided maze, saving the optimal path for tips with microwaves taken into account and its trail to the global variables.
 * The method returns a zero if it reaches a dead-end and recursively calls itself if it is able to take a step in any direction.
 * @Param map, a two-dimensional string array that stores the entire map at the current moment.
 * @Param visits, a two-dimensional boolean array that stores the tiles that have been visited.
 * @Param yCoor, an integer that stores the current y-coordinate of Duber.
 * @Param xCoor, an integer that stores the current x-coordinate of Duber.
 * @Param moves, an integer that stores the number of steps that can be influenced by microwaves taken by Duber.
 * @Param total, an integer that stores the total number of steps taken from the starting position by Duber.
 * @Param tips, an integer that stores the total number of tips earned by Duber.
 * @Return 0 if Duber reaches a dead-end and recursively call otherwise.
 */
    
  }
  public static int solve(String[][] map, boolean[][] visits, int yCoor, int xCoor, int moves, int total, int tips) {
    int up, down, left, right;
    boolean identity = false; //For determining if the current tile is a destination.
    String[][] dupe = copy(map); //Duplicate map each step in order to try all possibilities.
    boolean[][] track = duplicate(visits); //Keep track of visited tiles.
    for (int r = 0; r < 10; r++) {
      if (dupe[yCoor][xCoor].equals(Integer.toString(r))) {
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
      for (int s = 0; s < dupe.length; s++) {
        for (int t = 0; t < dupe[s].length; t++) {
          for (int u = 0; u < 10; u++) {
            if (dupe[s][t].equals(Integer.toString(u))) {
              completion = false;
            }
          }
        }
      }
      if (completion == true) {
        if (initialization == true) { //First initialization of the least amount of moves and most amount of tips, acting as a comparison point.
          minimum = moves;
          maximum = tips;
          for (int v = 0; v < dupe.length; v++) {
            for (int w = 0; w < dupe[v].length; w++) {
              optimal[v][w] = dupe[v][w];
              trail[v][w] = track[v][w];
            }
          }
          initialization = false;
        } else {
          if (tips > maximum) { //Search for most tips instead of least moves.
            minimum = moves;
            maximum = tips;
            for (int x = 0; x < dupe.length; x++) {
              for (int y = 0; y < dupe[x].length; y++) {
                optimal[x][y] = dupe[x][y];
                trail[x][y] = track[x][y];
              }
            }
          }
        }
      } else { //If the map is still incomplete, clear the map of trails to prevent the program from thinking it has hit a dead-end.
        for (int z = 0; z < dupe.length; z++) {
          for (int a = 0; a < dupe[z].length; a++) {
            if (dupe[z][a].equals("x")) {
              dupe[z][a] = " ";
            }
          }
        }
        return solve(dupe, track, yCoor, xCoor, moves, total, tips); //Then, recursively call itself again with its position at the reached destination.
      }
    }
    if (dupe[yCoor][xCoor].equals("M")) {
      dupe[yCoor][xCoor] = "U"; //Mark the microwave as used.
      for (int b = 0; b < dupe.length; b++) { //Clear the map of trails to prevent the program from thinking it has hit a dead-end.
        for (int c = 0; c < dupe.length; c++) {
          if (dupe[b][c].equals("x")) {
            dupe[b][c] = " ";
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
 * @Param map, a two-dimensional string array that stores the current map.
 * @Return the copied map.
 */
  
  public static String[][] copy(String[][] map) {
    String[][] replica = new String[map.length][map[0].length];
    for (int n = 0; n < map.length; n++) {
      for (int o = 0; o < map[n].length; o++) {
        replica[n][o] = map[n][o];
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
    for (int p = 0; p < visits.length; p++) {
      for (int q = 0; q < visits[p].length; q++) {
        replica[p][q] = visits[p][q];
      }
    }
    return replica;
  }
}