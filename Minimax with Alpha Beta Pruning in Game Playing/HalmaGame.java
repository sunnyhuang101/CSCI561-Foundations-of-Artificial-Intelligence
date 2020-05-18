import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.*;

public class HalmaGame {
    public int scoreCalculation(int move_i, int move_j) {
        int score = 0;
        //white => maximize: the farer from the corner of its camp, the higher scores get
        //black =>  minimize: the closer to the corner of its opponent's camp, the lower scores get
        //white: 1, need to occupy the black camp
        //black: -1, need to occupy the white camp

        int diagonal;
        int straight;
        int i = 15 - move_i;
        int j = 15 - move_j;
        if ( i >= j){
            diagonal = j;
            straight = i - j;

        }
        else{
            diagonal = i;
            straight = j - i;
        }
        score = diagonal*14 + straight*10;
/*
        int dis_i = Math.abs(from_i - move_i);
        int dis_j = Math.abs(from_j - move_j);
        int dis = 0;
        if (dis_i >= dis_j){
            dis = dis_j*14 + (dis_i - dis_j)*10;
        }
        else{
            dis = dis_i*14 + (dis_j - dis_i)*10;
        }
        if (color.equals("WHITE")){//max
            score = score + dis;
        }
        else{//min
            score = score - dis;
        }

*/
        return score;
    }

    public List<int[]> minimax(int[][] board, int depth, String color){

        //List<int[]> result = new ArrayList<>();
        Map<String, List<List<int[]>>> map = new HashMap<String, List<List<int[]>>>();

        boolean start_position_not_empty = false;

        if (color.equals("WHITE")) { //maximize
            for (int i = 11;i < 16;i++){ //check if we remove all pieces from start positions
                if (start_position_not_empty == true) break;
                for (int j = 11;j < 16;j++){
                    if ((i == 11 && j >= 14) || (i == 12 && j >= 13) || (i == 13 && j>= 12) || (i >= 14 && j >= 11)){
                        if (board[i][j] == 1 ) {
                            start_position_not_empty = true;
                            break;
                        }
                    }

                }
            }
            //System.out.println(start_position_not_empty);
        }
        else{ //black, minimize
            for (int i = 0;i < 5;i++){ //check if we remove all pieces from start positions
                if (start_position_not_empty == true) break;
                for (int j = 0;j < 5;j++){
                    if ((i <= 1 && j <= 4) || (i == 2 && j <= 3) || (i == 3 && j <= 2) || (i == 4 && j <= 1)){
                        if (board[i][j] == -1 ) {
                            start_position_not_empty = true;
                            break;
                        }
                    }

                }
            }
        }

        //List<int[]> moves = new ArrayList<>();
        map = findMoves(start_position_not_empty,  color, board);
/*
        for(Map.Entry<String, List<List<int[]>>> e : map.entrySet()) {
            for (List<int[]> moves : e.getValue()) {
                if (e.getKey().equals("E")) {
                    //System.out.println(moves.size());
                    System.out.println("E "+moves.get(0)[0] + "," + moves.get(0)[1] + "->" + moves.get(0)[2] + "," + moves.get(0)[3]);
                }
                else{
                    for(int i = 0; i < moves.size();i++){
                        System.out.println("J " + moves.get(i)[0] + "," + moves.get(i)[1]);
                    }
                }
            }
        }
*/
        if (start_position_not_empty == true && map.isEmpty() == true){
            map = findMoves(false,  color, board);
        }

        GameObject g = new GameObject(board, color, 0,new ArrayList<int[]>());;
        int[] p = new int[]{0, 0,0,0,0};
        List<int []> path = new ArrayList<>();
        path.add(p);

        if (color.equals("WHITE")){
            //g = maximize(depth, board);

            g = new GameObject(board, color, Integer.MIN_VALUE,path);

            for(Map.Entry<String, List<List<int[]>>> e : map.entrySet()) {
                for(List<int[]> moves : e.getValue()) {
                    if (e.getKey().equals("E")){
                        int[][] candidate_board = moving(board, moves.get(0), 1);
                        GameObject minGameObj;
                        int[] tmp_p = moves.get(0);
                        List<int[]> tmp_path = new ArrayList<>();
                        tmp_path.add(new int[]{0});
                        tmp_path.add(tmp_p);
                        //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                        minGameObj = minimize(depth - 1, candidate_board, tmp_path);
                        if (minGameObj.getScore() > g.getScore()) {
                            g.updateScore(minGameObj.getScore());
                            g.updateBoard(minGameObj.getBoard());
                            g.updatePath(minGameObj.getPath());
                        }

                    }

                    else if (e.getKey().equals("J")){
                        int[] jump = new int[]{moves.get(0)[0], moves.get(0)[1], moves.get(moves.size()-1)[0], moves.get(moves.size()-1)[1]};
                        int[][] candidate_board = moving(board, jump, 1);
                        GameObject minGameObj;
                        //int[] tmp_p = moves.get(0);
                        List<int[]> tmp_path = new ArrayList<>();
                        tmp_path.add(new int[]{1});
                        tmp_path.addAll(moves);
                        //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                        minGameObj = minimize(depth - 1, candidate_board, tmp_path);
                        if (minGameObj.getScore() > g.getScore()) {
                            g.updateScore(minGameObj.getScore());
                            g.updateBoard(minGameObj.getBoard());
                            g.updatePath(minGameObj.getPath());
                        }
                    }

                }
            }
        }
        else if (color.equals("BLACK")){
            //g = minimize(depth, board);
            g = new GameObject(board, color, Integer.MAX_VALUE, path);
            for(Map.Entry<String, List<List<int[]>>> e : map.entrySet()) {
                for(List<int[]> moves : e.getValue()) {
                    if (e.getKey().equals("E")){
                        int[][] candidate_board = moving(board, moves.get(0), 1);
                        GameObject maxGameObj;
                        int[] tmp_p = moves.get(0);
                        List<int[]> tmp_path = new ArrayList<>();
                        tmp_path.add(new int[]{0});
                        tmp_path.add(tmp_p);
                        //System.out.println(tmp_path.get(1)[0] + " " + tmp_path.get(1)[1] + "->" +tmp_path.get(1)[2] + " " + tmp_path.get(1)[3] );
                        maxGameObj = maximize(depth - 1, candidate_board, tmp_path);
                        //System.out.println("score: " + maxGameObj.getScore());
                        if (maxGameObj.getScore() < g.getScore()) {
                            g.updateScore(maxGameObj.getScore());
                            g.updateBoard(maxGameObj.getBoard());
                            g.updatePath(maxGameObj.getPath());
                        }

                    }

                    else if (e.getKey().equals("J")){
                        //System.out.println("J " + moves.get(1)[0] + "," + moves.get(1)[1]);
                        int[] jump = new int[]{moves.get(0)[0], moves.get(0)[1], moves.get(moves.size()-1)[0], moves.get(moves.size()-1)[1]};
                        int[][] candidate_board = moving(board, jump, 1);
                        GameObject maxGameObj;
                        //int[] tmp_p = moves.get(0);
                        List<int[]> tmp_path = new ArrayList<>();
                        tmp_path.add(new int[]{1});
                        tmp_path.addAll(moves);
                        //System.out.println(tmp_path.get(1)[0] + " " + tmp_path.get(1)[1] + "->" + tmp_path.get(tmp_path.size()-1)[0] + " " + tmp_path.get(tmp_path.size()-1)[1]);
                        maxGameObj = maximize(depth - 1, candidate_board, tmp_path);
                        //System.out.println("score: " + maxGameObj.getScore());
                        //System.out.println("score: " + g.getScore());
                        if (maxGameObj.getScore() < g.getScore()) {
                            g.updateScore(maxGameObj.getScore());
                            g.updateBoard(maxGameObj.getBoard());
                            g.updatePath(maxGameObj.getPath());
                        }
                    }

                }
            }


        }
        //System.out.println("yo");
        /*
        for(int i = 0;i<16;i++){
            for(int j = 0;j<16;j++){
                System.out.print(g.getBoard()[i][j]);
            }
            System.out.print('\n');
        }
        */
        //System.out.println(g.getPath().get(0)[2]);

            return g.getPath();

    }

    public GameObject minimize(int depth, int[][] board, List<int[]> path){ //black
        //int score = scoreCalculation("BLACK", board);
        GameObject g = new GameObject(board, "BLACK", Integer.MAX_VALUE, path);

        if (depth == 0){
            //System.out.println(g.getPath().get(0)[2] + " " + g.getPath().get(0)[3]);
            int score;
            if (path.get(0)[0] == 0){ // empty
               score = scoreCalculation(path.get(path.size()-1)[2], path.get(path.size()-1)[3]);
            }
            else{
                score = scoreCalculation(path.get(path.size()-1)[0], path.get(path.size()-1)[1]);
            }

            g.updateScore(score);
            return g;
        }


        boolean start_position_not_empty = false;

        for (int i = 0;i < 5;i++){ //check if we remove all pieces from start positions
            if (start_position_not_empty == true) break;
            for (int j = 0;j < 5;j++){
                if ((i <= 1 && j <= 4) || (i == 2 && j <= 3) || (i == 3 && j <= 2) || (i == 4 && j <= 1)){
                    if (board[i][j] == -1 ) {
                        start_position_not_empty = true;
                        break;
                    }
                }

            }
        }
        //int min = Integer.MAX_VALUE;
        Map<String, List<List<int[]>>> map = new HashMap<String, List<List<int[]>>>();
        //List<int[]> moves
          map      = findMoves(start_position_not_empty, "BLACK", board);
        if (start_position_not_empty == true && map.isEmpty() == true){
            map = findMoves(false,  "BLACK", board);
        }

        for(Map.Entry<String, List<List<int[]>>> e : map.entrySet()) {
            for(List<int[]> moves : e.getValue()) {
                if (e.getKey().equals("E")){
                    int[][] candidate_board = moving(board, moves.get(0), 1);
                    GameObject maxGameObj;
                    int[] tmp_p = moves.get(0);
                    List<int[]> tmp_path = new ArrayList<>();
                    tmp_path.add(new int[]{0});
                    tmp_path.add(tmp_p);
                    //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                    maxGameObj = maximize(depth - 1, candidate_board, tmp_path);
                    if (maxGameObj.getScore() < g.getScore()) {
                        g.updateScore(maxGameObj.getScore());
                        g.updateBoard(maxGameObj.getBoard());
                        g.updatePath(maxGameObj.getPath());
                    }

                }

                else if (e.getKey().equals("J")){
                    int[] jump = new int[]{moves.get(0)[0], moves.get(0)[1], moves.get(moves.size()-1)[0], moves.get(moves.size()-1)[1]};
                    int[][] candidate_board = moving(board, jump, 1);
                    GameObject maxGameObj;
                    //int[] tmp_p = moves.get(0);
                    List<int[]> tmp_path = new ArrayList<>();
                    tmp_path.add(new int[]{1});
                    tmp_path.addAll(moves);
                    //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                    maxGameObj = maximize(depth - 1, candidate_board, tmp_path);
                    if (maxGameObj.getScore() < g.getScore()) {
                        g.updateScore(maxGameObj.getScore());
                        g.updateBoard(maxGameObj.getBoard());
                        g.updatePath(maxGameObj.getPath());
                    }
                }

            }
        }


        return g;
    }

    public GameObject maximize(int depth, int[][] board, List<int[]> path){ //white
        GameObject g = new GameObject(board, "WHITE", Integer.MIN_VALUE, path);
        if (depth == 0){
            int score; //= scoreCalculation(path.get(path.size()-1)[2], path.get(path.size()-1)[3]);
            if (path.get(0)[0] == 0){ // empty
                score = scoreCalculation(path.get(path.size()-1)[2], path.get(path.size()-1)[3]);
            }
            else{
                score = scoreCalculation(path.get(path.size()-1)[0], path.get(path.size()-1)[1]);
            }
            //System.out.println(g.getPath().get(0)[2] + " " + g.getPath().get(0)[3]);
            g.updateScore(score);
            return g; //evaluation function
        }


        boolean start_position_not_empty = false;

        for (int i = 11;i < 16;i++){ //check if we remove all pieces from start positions
            if (start_position_not_empty == true) break;
            for (int j = 11;j < 16;j++){
                if ((i == 11 && j >= 14) || (i == 12 && j >= 13) || (i == 13 && j>= 12) || (i >= 14 && j >= 11)){
                    if (board[i][j] == 1 ) {
                        start_position_not_empty = true;
                        break;
                    }
                }

            }
        }
        //int max = Integer.MIN_VALUE;
        //List<int[]> moves =
        Map<String, List<List<int[]>>> map = new HashMap<String, List<List<int[]>>>();
        map = findMoves(start_position_not_empty, "WHITE", board);
        if (start_position_not_empty == true && map.isEmpty() == true){
            map = findMoves(false,  "WHITE", board);
        }

        for(Map.Entry<String, List<List<int[]>>> e : map.entrySet()) {
            for(List<int[]> moves : e.getValue()) {
                if (e.getKey().equals("E")){
                    int[][] candidate_board = moving(board, moves.get(0), 1);
                    GameObject minGameObj;
                    int[] tmp_p = moves.get(0);
                    List<int[]> tmp_path = new ArrayList<>();
                    tmp_path.add(new int[]{0});
                    tmp_path.add(tmp_p);
                    //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                    minGameObj = minimize(depth - 1, candidate_board, tmp_path);
                    if (minGameObj.getScore() > g.getScore()) {
                        g.updateScore(minGameObj.getScore());
                        g.updateBoard(minGameObj.getBoard());
                        g.updatePath(minGameObj.getPath());
                    }

                }

                else if (e.getKey().equals("J")){
                    int[] jump = new int[]{moves.get(0)[0], moves.get(0)[1], moves.get(moves.size()-1)[0], moves.get(moves.size()-1)[1]};
                    int[][] candidate_board = moving(board, jump, 1);
                    GameObject minGameObj;
                    //int[] tmp_p = moves.get(0);
                    List<int[]> tmp_path = new ArrayList<>();
                    tmp_path.add(new int[]{1});
                    tmp_path.addAll(moves);
                    //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                    minGameObj = minimize(depth - 1, candidate_board, tmp_path);
                    if (minGameObj.getScore() > g.getScore()) {
                        g.updateScore(minGameObj.getScore());
                        g.updateBoard(minGameObj.getBoard());
                        g.updatePath(minGameObj.getPath());
                    }
                }

            }
        }

        return g;
    }

    public int[][] moving(int[][] board, int[] move, int piece_type){
        int from_i = move[0]; //y
        int from_j = move[1]; //x
        int to_i = move[2]; //y
        int to_j = move[3]; //x
        //int jump = move[0]; //empty: 0, jump: 1
        //piece_type: white 1, black -1

        int[][] candidate_board = new int[16][16];
        for(int i = 0; i < 16; i++){
            for(int j = 0;j < 16;j++){
                candidate_board[i][j] = board[i][j];
            }
        }

        candidate_board[from_i][from_j] = 0;
        candidate_board[to_i][to_j] = piece_type;

        return candidate_board;

    }

    public Map<String, List<List<int[]>>> findMoves(boolean start_position_not_empty, String color, int[][] board) {

        //need more directions
        //first check if start positions empty or not
        //start position black:(0,0)(1,0)(2,0)(3,0)(4,0), (0,1)(1,1)(2,1)(3,1)(4,1), (0,2)(1,2)(2,2)(3, 2), (0,3)(1,3)(2,3), (0,4)(1,4)
        //start position white:(14,11)(15,11), (13,12)(14,12)(15,12), (12,13)(13,13)(14,13)(15,13), (11,14)(12,14)(13,14)(14,14)(15,14), (11,15)(12,15)(13,15)(14,15)(15,15)
        List<List<int[]>> e_moves = new ArrayList<List<int[]>>();
        List<List<int[]>> j_moves = new ArrayList<List<int[]>>();

        int i;

        if (color.equals("WHITE"))
            i = 15;
        else
            i = 0;


/*
        List<int[]> jump_moves = new ArrayList<>();
        jump_moves.add(new int[]{-2, -2, -1, -1});
        jump_moves.add(new int[]{0, -2, 0, -1});
        jump_moves.add(new int[]{-2, 0, -1, 0});

        jump_moves.add(new int[]{2, 2, 1, 1});
        jump_moves.add(new int[]{0, 2, 0, 1});
        jump_moves.add(new int[]{2, 0, 1, 0});
*/
        List<int[]> white_empty_moves = new ArrayList<>();
        List<int[]> black_empty_moves = new ArrayList<>();
        white_empty_moves.add(new int[]{-1, -1});
        white_empty_moves.add(new int[]{0, -1});
        white_empty_moves.add(new int[]{-1, 0});

        black_empty_moves.add(new int[]{1, 1});
        black_empty_moves.add(new int[]{0, 1});
        black_empty_moves.add(new int[]{1, 0});

        List<int[]> white_jump_moves = new ArrayList<>();
        List<int[]> black_jump_moves = new ArrayList<>();

        white_jump_moves.add(new int[]{-2, -2, -1, -1});
        white_jump_moves.add(new int[]{0, -2, 0, -1});
        white_jump_moves.add(new int[]{-2, 0, -1, 0});

        black_jump_moves.add(new int[]{2, 2, 1, 1});
        black_jump_moves.add(new int[]{0, 2, 0, 1});
        black_jump_moves.add(new int[]{2, 0, 1, 0});

        //int count = 0;
        while (i >= 0 && i < 16) {
            if (start_position_not_empty == true && ((color.equals("BLACK") && i > 4) || (color.equals("WHITE") && i < 11)))
                break;
            for (int j = 0; j < 16; j++) {
                //int[] m = new int[4];//from x, y to x, y
                //m[0] = 0; //default: adjacent empty 0, jump 1
                //m[0] = i;
                //m[1] = j;

                if ((start_position_not_empty == true) && ((color.equals("BLACK") && (i <= 1 && j <= 4) || (i == 2 && j <= 3) || (i == 3 && j <= 2) || (i == 4 && j <= 1)) || (color.equals("WHITE") && (i == 11 && j >= 14) || (i == 12 && j >= 13) || (i == 13 && j >= 12) || (i >= 14 && j >= 11)))) {

                    if (color.equals("BLACK") && board[i][j] == -1) {
                        for (int[] empty : black_empty_moves) {
                            if (i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0) {
                                int[] m = new int[4];
                                m[0] = i;
                                m[1] = j;
                                m[2] = i + empty[0];
                                m[3] = j + empty[1];
                                List<int[]> ml = new ArrayList<>();
                                ml.add(m);
                                e_moves.add(ml);
                                //if (e_moves.size() > 1) {
                                    //System.out.println("yoyo " + e_moves.get(e_moves.size() - 2).get(0)[2] + " " + e_moves.get(e_moves.size() - 2).get(0)[3]);
                                //}
                                //System.out.println("count " + count);
                                //count++;
                            }
                        }


                        for (int[] empty : black_jump_moves) {
                            if (i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("BLACK", board, i, j));
                            }
                        }

                        for (int[] empty : white_jump_moves) {
                            if (i + empty[0] >= 0 && j + empty[1] >= 0  && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("BLACK", board, i, j));
                            }
                        }

                    } else if (color.equals("WHITE") && board[i][j] == 1) { //WHITE

                        for (int[] empty : white_empty_moves) {
                            if (i + empty[0] >= 0 && j + empty[1] >= 0  && board[i + empty[0]][j + empty[1]] == 0) {
                                int[] m = new int[4];
                                m[0] = i;
                                m[1] = j;
                                m[2] = i + empty[0];
                                m[3] = j + empty[1];
                                List<int[]> ml = new ArrayList<>();
                                ml.add(m);
                                e_moves.add(ml);
                            }
                        }
                        for (int[] empty : black_jump_moves) {
                            if (i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("WHITE", board, i, j));
                            }
                        }
                        for (int[] empty : white_jump_moves) {
                            if (i + empty[0] >= 0 && j + empty[1] >= 0  && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("WHITE", board, i, j));
                            }
                        }

                    }
                } else if (start_position_not_empty == false){ //start_position_empty == false
                    if (color.equals("BLACK") && board[i][j] == -1) {
                        for (int[] empty : black_empty_moves) {
                            if ( i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0) {
                                int[] m = new int[4];
                                m[0] = i;
                                m[1] = j;
                                m[2] = i + empty[0];
                                m[3] = j + empty[1];
                                List<int[]> ml = new ArrayList<>();
                                ml.add(m);
                                e_moves.add(ml);
                            }
                        }
                        for (int[] empty : black_jump_moves) {
                            if (i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("BLACK", board, i, j));
                            }
                        }

                        for (int[] empty : white_jump_moves) {
                            if (i + empty[0] >= 0 && j + empty[1] >= 0  && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("BLACK", board, i, j));
                            }
                        }

                    } else if (color.equals("WHITE") && board[i][j] == 1) {
                        for (int[] empty : white_empty_moves) {
                            if (i + empty[0] >= 0 && j + empty[1] >= 0 && board[i + empty[0]][j + empty[1]] == 0) {
                                int[] m = new int[4];
                                m[0] = i;
                                m[1] = j;
                                m[2] = i + empty[0];
                                m[3] = j + empty[1];
                                List<int[]> ml = new ArrayList<>();
                                ml.add(m);
                                e_moves.add(ml);
                            }
                        }
                        for (int[] empty : black_jump_moves) {
                            if (i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("WHITE", board, i, j));
                            }
                        }
                        for (int[] empty : white_jump_moves) {
                            if (i + empty[0] >= 0 && j + empty[1] >= 0  && board[i + empty[0]][j + empty[1]] == 0 && board[i + empty[2]][j + empty[3]] != 0) {
                                j_moves.addAll(findJumpMoves("WHITE", board, i, j));
                            }
                        }
                    }
                }
            }
            if (color.equals("WHITE"))
                i--;
            else
                i++;
        }

        Map<String, List<List<int[]>>> map = new HashMap<String, List<List<int[]>>>();
        if (e_moves.isEmpty() == false){
            //System.out.println(e_moves.get(1).get(0)[2] + " " + e_moves.get(1).get(0)[3]);
            map.put("E", e_moves);
        }
        if (j_moves.isEmpty() == false){
            map.put("J", j_moves);
        }

/*
        for (int k = 0; k < moves.size();k++){
            System.out.print(color+ " ");
            System.out.print(moves.get(k)[0] + " " + moves.get(k)[1] + " " + moves.get(k)[2] + " " + moves.get(k)[3] + " "+ moves.get(k)[4]);
        }
        System.out.print('\n');
*/
        return map;
    }

    public List<List<int[]>> findJumpMoves(String color, int[][] board, int move_i, int move_j) {

        List<int[]> white_jump_moves = new ArrayList<>();
        List<int[]> black_jump_moves = new ArrayList<>();

        white_jump_moves.add(new int[]{-2, -2, -1, -1});
        white_jump_moves.add(new int[]{0, -2, 0, -1});
        white_jump_moves.add(new int[]{-2, 0, -1, 0});

        black_jump_moves.add(new int[]{2, 2, 1, 1});
        black_jump_moves.add(new int[]{0, 2, 0, 1});
        black_jump_moves.add(new int[]{2, 0, 1, 0});

        Stack<JumpObject> stack = new Stack<>();
        JumpObject start = new JumpObject(null, move_i, move_j);
        //System.out.println("jump from : " + move_i + ","+ move_j );
        stack.push(start);

        List<JumpObject> tmp_result = new ArrayList<>();
        List<List<int[]>> jump_result = new ArrayList<List<int[]>>();
        boolean[][] visited = new boolean[16][16];
        for(int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++){
                visited[i][j] = false;
            }
        }

        JumpObject finish = start;

        while(stack.isEmpty() == false){
            boolean last = false;
            JumpObject s = stack.pop();
            int s_i = s.getMoveI();
            int s_j = s.getMoveJ();
            if (visited[s.getMoveI()][s.getMoveJ()] == false){
                visited[s.getMoveI()][s.getMoveJ()] = true;
                //System.out.println("jump from : " + s_i + ","+ s_j );
                finish = s;
                last = true;
                for (int[] empty : black_jump_moves) {
                    if (s_i + empty[0] < 16 && s_j + empty[1] < 16 && board[s_i + empty[0]][s_j + empty[1]] == 0 && board[s_i + empty[2]][s_j + empty[3]] != 0 && visited[s_i+empty[0]][s_j+empty[1]] == false) {
                        JumpObject j = new JumpObject(s, s_i + empty[0], s_j + empty[1]);
                        //System.out.println("jump: " + (move_i + empty[2])+ ","+ (move_j + empty[3]));
                        //System.out.println("block: " +(move_i + empty[2])+ ","+ (move_j + empty[3] )+ "->" + board[move_i + empty[2]][move_j + empty[3]]);
                        //System.out.println("jump: " +(move_i + empty[0])+ ","+ (move_j + empty[1] )+ "->" + board[move_i + empty[0]][move_j + empty[1]]);
                        stack.push(j);
                        last = false;

                    }
                }
                //System.out.println("=======");
                for (int[] empty : white_jump_moves) {
                    if (s_i + empty[0] >= 0 && s_j + empty[1] >= 0 && board[s_i + empty[0]][s_j + empty[1]] == 0 && board[s_i + empty[2]][s_j + empty[3]] != 0 &&  visited[s_i+empty[0]][s_j+empty[1]] == false) {
                        JumpObject j = new JumpObject(s, s_i + empty[0], s_j + empty[1]);
                        //System.out.println("jump: " + (move_i + empty[0])+ ","+ (move_j + empty[1]));
                        //System.out.println("block: " +(move_i + empty[2])+ ","+ (move_j + empty[3] )+ "->" + board[move_i + empty[2]][move_j + empty[3]]);
                        //System.out.println("jump: " +(move_i + empty[0])+ ","+ (move_j + empty[1] )+ "->" + board[move_i + empty[0]][move_j + empty[1]]);
                        stack.push(j);
                        last = false;
                    }
                }

                if (last == true)
                    tmp_result.add(finish);

            }
            //neighbors





        }

        for (int i = 0; i < tmp_result.size(); i++){
            JumpObject j = tmp_result.get(i);
            Stack<int[]> path = new Stack<>();
            List<int[]> path_result = new ArrayList<>();

            while( j != null){
                path.push(new int[]{j.getMoveI(), j.getMoveJ()});
                j = j.getParent();
            }


            //System.out.println("========");
            int n = path.size();
            while(n>0){
                int[] p = path.pop();
                //System.out.println(p[1] + "," +p[0]);
                path_result.add(p);
                n--;
            }





            if (color.equals("BLACK")){//check distance to (0,0)
                int original_x = path_result.get(0)[0];
                int original_y = path_result.get(0)[1];
                int original_dis;
                if (original_x >= original_y){
                    original_dis = original_y*14 + (original_x - original_y)*10;
                }
                else{
                    original_dis = original_x*14 + (original_y - original_x)*10;
                }

                int cur = path_result.size()-1;
                while(cur >= 0){
                    int cur_x = path_result.get(cur)[0];
                    int cur_y = path_result.get(cur)[1];
                    int cur_dis;
                    if (cur_x >= cur_y){
                        cur_dis = cur_y*14 + (cur_x - cur_y)*10;
                    }
                    else{
                        cur_dis = cur_x*14 + (cur_y - cur_x)*10;
                    }

                    if (cur_dis > original_dis){
                        break;
                    }
                    else{
                        path_result.remove(cur);
                    }
                    cur--;
                }
            }
            else if(color.equals("WHITE")){
                int original_x = 15 - path_result.get(0)[0];
                int original_y = 15 - path_result.get(0)[1];
                int original_dis;
                //original_x = 15 - original_x;
                //original_y = 15 - original_y;

                if (original_x >= original_y){
                    original_dis = original_y*14 + (original_x - original_y)*10;
                }
                else{
                    original_dis = original_x*14 + (original_y - original_x)*10;
                }

                int cur = path_result.size()-1;
                while(cur >= 0){
                    int cur_x = 15 - path_result.get(cur)[0];
                    int cur_y = 15 - path_result.get(cur)[1];
                    int cur_dis;
                    if (cur_x >= cur_y){
                        cur_dis = cur_y*14 + (cur_x - cur_y)*10;
                    }
                    else{
                        cur_dis = cur_x*14 + (cur_y - cur_x)*10;
                    }

                    if (cur_dis > original_dis){
                        break;
                    }
                    else{
                        path_result.remove(cur);
                    }
                    cur--;
                }
            }

            if (path_result.isEmpty() == false){
                jump_result.add(path_result);
            }
        }

        return jump_result;

    }
}
