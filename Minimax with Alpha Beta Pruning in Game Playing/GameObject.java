import java.util.ArrayList;
import java.util.List;

public class GameObject {
    //private int depth;
   // private String scenario;
    private int[][] board;
    private String color;
    private int score;
    private List<int[]> path;

    GameObject(int[][] board, String color, int score, List<int[]> path) {
        //this.depth = depth;
       // this.scenario = scenario;
        this.board = board;
        this.color = color;
        this.score = score;
        this.path = path;//Jump/Empty, from_i from_j => to_i to_j
    }
    public void updatePath(List<int[]> newPath){
        this.path = newPath;
        /*
        for (int i = 0;i < 5;i++){
            this.path[i] = newPath[i];
        }
        */
    }

    public void updateScore(int newScore){
        this.score = newScore;
    }

    public void updateBoard(int[][] newBoard){
        this.board = newBoard;
    }

    public int getScore(){return this.score; }
    public int[][] getBoard() {return this.board; }
    public List<int[]> getPath(){return this.path;}


}
