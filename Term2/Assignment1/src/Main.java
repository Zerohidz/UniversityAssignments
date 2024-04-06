import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static HashMap<String, Integer> valuableBalls;

    private static int score;
    private static String[][] board;
    private static String[][] firstBoard;
    private static String moveDirections;

    private static Point playerPosition;

    private static boolean playerFellIntoHole;

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point clone() {
            return new Point(this.x, this.y);
        }
    }

    public static void main(String[] args) {
        initializeValuableBalls();

        if (args.length < 2) {
            System.out.println("Bad input files!");
            return;
        }

        String[] boardLines;
        if ((boardLines = readLines(args[0])) != null
                && (moveDirections = readFirstLine(args[1])) != null) {
            board = new String[boardLines.length][];
            firstBoard = new String[boardLines.length][];
            for (int i = 0; i < boardLines.length; i++) {
                board[i] = boardLines[i].split(" ");
                firstBoard[i] = boardLines[i].split(" ");
            }
        } else {
            System.out.println("Bad input files!");
            return;
        }

        findPlayerPosition();
        play();
        saveOutput();
    }

    private static void findPlayerPosition() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals("*")) {
                    playerPosition = new Point(j, i);
                    return;
                }
            }
        }
    }

    private static void play() {
        for (String moveDirection : moveDirections.split(" ")) {
            if (!playerFellIntoHole)
                move(moveDirection);
        }
    }

    private static void initializeValuableBalls() {
        valuableBalls = new HashMap<String, Integer>();
        valuableBalls.put("R", 10);
        valuableBalls.put("Y", 5);
        valuableBalls.put("B", -5);
    }

    private static void move(String direction) {
        Point newPlayerPosition = playerPosition.clone();

        switch (direction) {
            case "U":
                newPlayerPosition.y = (playerPosition.y - 1 + board.length) % board.length;
                break;
            case "D":
                newPlayerPosition.y = (playerPosition.y + 1 + board.length) % board.length;
                break;
            case "L":
                newPlayerPosition.x = (playerPosition.x - 1 + board[0].length) % board[0].length;
                break;
            case "R":
                newPlayerPosition.x = (playerPosition.x + 1 + board[0].length) % board[0].length;
                break;
            default:
                System.out.println("Unexpected move: " + direction + "!");
                break;
        }

        String destinationChar = board[newPlayerPosition.y][newPlayerPosition.x];
        if (valuableBalls.containsKey(destinationChar)) {
            popBall(newPlayerPosition);
            score += valuableBalls.get(destinationChar);
        } else if (destinationChar.equals("W")) {
            move(getOppositeDirection(direction));
            return;
        } else if (destinationChar.equals("H")) {
            board[playerPosition.y][playerPosition.x] = " ";
            playerFellIntoHole = true;

            return;
        }
        
        swapBalls(playerPosition, newPlayerPosition);
        playerPosition = newPlayerPosition;
    }

    private static void popBall(Point pos) {
        board[pos.y][pos.x] = "X";
    }

    private static String getOppositeDirection(String direction) {
        if (direction.equals("U"))
            return "D";
        if (direction.equals("D"))
            return "U";
        if (direction.equals("L"))
            return "R";
        if (direction.equals("R"))
            return "L";
        else
            return null;
    }

    private static void swapBalls(Point oldPos, Point newPos) {
        String temp = board[oldPos.y][oldPos.x];
        board[oldPos.y][oldPos.x] = board[newPos.y][newPos.x];
        board[newPos.y][newPos.x] = temp;
    }

    private static void showBoard(String[][] board) {
        for (String[] row : board) {
            System.out.println(String.join(" ", row));
        }
    }

    public static String[] readLines(String path) {
        ArrayList<String> result = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to read file: " + path);
        }

        return result.toArray(new String[result.size()]);
    }

    public static String readFirstLine(String path) {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            line = reader.readLine();
        } catch (Exception e) {
            System.out.println("Failed to read file: " + path);
        }

        return line;
    }

    private static void saveOutput() {
        try {
            System.setOut(new PrintStream(new FileOutputStream("output.txt")));
        } catch (Exception e) {
            System.out.println("Could not save output.txt!");
            System.out.println("We were goning to save this:");
            System.out.println();
        }

        System.out.println("Game board:");
        showBoard(firstBoard);
        System.out.println();
        System.out.println("Your movement is:");
        System.out.println(moveDirections);
        System.out.println();
        System.out.println("Your output is:");
        showBoard(board);
        System.out.println();
        if (playerFellIntoHole)
            System.out.println("Game Over!");
        System.out.println("Score: " + score);
    }
}