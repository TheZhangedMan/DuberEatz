# DuberEatz
 A recursive pathfinding algorithm for solving mazes, simulating food delivery.
* Mazes are text-based
    * The first two values of the text file of a maze will always be length and width of the maze respectively
    * Hashtags represent walls
    * S represents the starting point
    * Numbers represent destinations: lower numbers have higher priority
    * M represents a microwave, a tile that reduces move count
* The algorithm iterates through all possibilities and outputs the best path for tips and its details
    * x represents a tile that has been visited
    * X represents a destination that has been visited
    * Most tips end up being negative due to the specified method of calculating tips
* A PPM file is also created after solving to visualize the best path