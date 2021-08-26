// Names: Jennifer Kim, Matthew Vilaysack
// x500s: kim00954, vilay016

import java.util.Random;
import java.util.*;

public class MyMaze {

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell();
            }
        }
        this.startRow = startRow;
        this.endRow = endRow;
    }
public static int search(int[][] a, int row, int col) {
    boolean[][] visitedArray = new boolean[a.length][a[0].length];
    int count = 0;
    Queue<int[]> queue = new LinkedList<int[]>();
    queue.add(new int[]{row, col}); // add the first index [row,col]
    for (int i = 0; i < a.length; i++) {
        for (int j = 0; j < a[0].length; j++) {

            if (!visitedArray[i][j] && a[i][j] > 0) {
                while (!queue.isEmpty()) {
                    int[] removedIndicies = queue.remove(); // remove the last integer pair []
                    int r = removedIndicies[0];
                    int c = removedIndicies[1];
                    visitedArray[r][c] = true; // visited

                    if (r - 1 >= 0  && !visitedArray[r-1][c] && a[r - 1][c] > 0) {
                        queue.offer(new int[]{r - 1, c}); // add to the end
                        visitedArray[r-1][c] = true;
                        count++;

                    }
                    if (r + 1 < a.length && !visitedArray[r+1][c] && a[r + 1][c] > 0) {
                        queue.offer(new int[]{r + 1, c});
                        visitedArray[r+1][c] = true;
                        count++;
                    }
                    if (c + 1 < a[0].length  && !visitedArray[r][c+1] && a[r][c+1] > 0) {
                        queue.offer(new int[]{r, c + 1});
                        visitedArray[r][c+1] = true;
                        count++;
                    }
                    if (c - 1 >= 0 && !visitedArray[r][c-1] && a[r][c - 1] > 0) {
                        queue.offer(new int[]{r, c - 1}); // add to the end
                        visitedArray[r][c-1] = true;
                        count++;
                    }

                }
            }
        }
    }
    return count;
}
    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow) {
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();
        stack.push(new int[]{startRow, 0});
        MyMaze myMaze1 = new MyMaze(rows, cols, startRow, endRow);
        myMaze1.maze[startRow][0].setVisited(true);

        while (!stack.isEmpty()) {
            int[] topElement = stack.top();
            int row = topElement[0];
            int col = topElement[1];
            Random rand = new Random();
            //check valid neighbors
            //randomly gen a dir until valid/all neighbors are checked

            List<int[]> listOfNeighbors = new ArrayList<>(); // create a list of reachable neighbors
            if (row - 1 >= 0 && !myMaze1.maze[row - 1][col].getVisited()) {
                listOfNeighbors.add(new int[]{row - 1, col});
            }
            if (row + 1 < myMaze1.maze.length && !myMaze1.maze[row + 1][col].getVisited()) {
                listOfNeighbors.add(new int[]{row + 1, col});
            }
            if (col + 1 < myMaze1.maze[0].length && !myMaze1.maze[row][col + 1].getVisited()) {
                listOfNeighbors.add(new int[]{row, col + 1});
            }
            if (col - 1 >= 0 && !myMaze1.maze[row][col - 1].getVisited()) {
                listOfNeighbors.add(new int[]{row, col - 1});
            }
            if (listOfNeighbors.size() == 0) { // if there are no reachable neighbors, pop the stack
                stack.pop();
            } else {
                int[] randomNeighbor = listOfNeighbors.get(rand.nextInt(listOfNeighbors.size()));
                int r = randomNeighbor[0];
                int c = randomNeighbor[1];
                stack.push(new int[]{r, c});
                myMaze1.maze[r][c].setVisited(true);
                // remove wall between current neighbor and current index
                if (r < row) { // neighbor is above
                    myMaze1.maze[row - 1][col].setBottom(false);
                    stack.push(new int[]{row - 1, col});
                } else if (row < r) { //neighbor is below
                    myMaze1.maze[row][col].setBottom(false); // remove bound between row and row+1
                    stack.push(new int[]{row + 1, col});
                } else if (col < c) { //neighbor is to the right
                    myMaze1.maze[row][col].setRight(false); // remove bound between col and col+1
                    stack.push(new int[]{row, col + 1});
                } else if (c < col) { //neighbor is to the left
                    myMaze1.maze[row][col - 1].setRight(false);
                    stack.push(new int[]{row, col - 1});
                }
            }


        }

        for (int i = 0; i < myMaze1.maze.length; i++) { //reset every cell's visited attribute to false
            for (int j = 0; j < myMaze1.maze[0].length; j++) {
                myMaze1.maze[i][j].setVisited(false);
            }
        }
        return myMaze1;
    }

    public void printMaze() {
        StringBuilder mazeString = new StringBuilder();
        int counter = 0;
        int k = 0;
        while (k <= maze[0].length * 2) { // top border
            if (counter % 2 == 0) {
                counter++;
                mazeString.append("|");
            } else {
                counter++;
                mazeString.append("---");
            }
            k++;
        }
        mazeString.append("\n");



        for (int i = 0; i < maze.length; i++) {
            String pattern = "---|";
            StringBuilder fillerRowString = new StringBuilder();
            fillerRowString.append("|");
            if (i==startRow) //space for entrance
                mazeString.append(" ");
            else mazeString.append("|");

            //cell visited vs not visited
            //--> check setright = false and check setBottom = false
            //every cell has four corners
            //alternate between "|" and "---" in filler rows
            for (int j = 0; j < maze[0].length; j++) {
                if (!maze[i][j].getVisited() && maze[i][j].getRight()) { // cell is not visited and there is a border on the right
                    if (i == endRow && j == maze[0].length-1) { // exit
                        mazeString.append("    "+ "\uD83D\uDC7D");
                    }
                    else{
                        mazeString.append("   |");
                    }
                }
                if (maze[i][j].getVisited() && maze[i][j].getRight()) {// cell is visited and there is a border on the right
                    if (i == endRow && j == maze[0].length-1) { // exit
                        mazeString.append(" *  "+"\uD83D\uDC7D");
                    }
                    else{
                        mazeString.append(" * |");
                    }
                }
                if (maze[i][j].getBottom()) {//there is a border on the bottom
                    fillerRowString.append("---|");
                }

                if (!maze[i][j].getVisited() && !maze[i][j].getRight()) { //cell is not visited and there is no border on the right
                    mazeString.append("    ");
                }
                if (maze[i][j].getVisited() && !maze[i][j].getRight()) { //cell is visited and there is no border on the right
                    mazeString.append(" *  ");
                }


                if (!maze[i][j].getBottom()) { //there is no border on the bottom
                    //take into account last row
                    if (i == maze.length - 1) {
                        fillerRowString.append(pattern);
                    }
                    else {
                        fillerRowString.append("   |");
                    }
                }
            }
            fillerRowString.append("\n");
            mazeString.append("\n");
            mazeString.append(fillerRowString);

        }
        System.out.println(mazeString);
    }

    public void solveMaze() {
        QGen<int[]> queue = new Q2Gen<int[]>();
        queue.add(new int[]{startRow, 0});
        printMaze();
        while (queue.length() != 0) {
            int[] currentCell = queue.remove();
            int row = currentCell[0];
            int col = currentCell[1];
            maze[row][col].setVisited(true);

            if (row == endRow && col == maze[0].length-1) {
                break; //maze is solved
            }
            if (row+1 < maze.length && !maze[row+1][col].getVisited() && !maze[row][col].getBottom()) {
                queue.add(new int[]{row+1,col});
            }
            if (row-1 >= 0 && !maze[row-1][col].getVisited() && !maze[row-1][col].getBottom()) {
                queue.add(new int[]{row-1,col});
            }
            if (col+1 < maze[0].length && !maze[row][col+1].getVisited() && !maze[row][col].getRight()) {
                queue.add(new int[]{row,col+1}); // right

            }
            if (col-1 >= 0 && !maze[row][col-1].getVisited() && !maze[row][col-1].getRight()) {
                queue.add(new int[]{row,col-1});
            }
        }

        printMaze();

    }
    private Cell[][] maze;
    private int startRow;
    private int endRow;

}
