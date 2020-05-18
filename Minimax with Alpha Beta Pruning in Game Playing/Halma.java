import java.util.ArrayList;
import java.util.List;

public class Halma {
    public int scoreCalculation(int move_i, int move_j) {
        int score = 0;

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



        return score;
    }

    public List<int[]> minimax(int[][] board, int depth, String color){

        //List<int[]> result = new ArrayList<>();
        List<int[]> moves = new ArrayList<>();
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

       moves = findMoves(start_position_not_empty,  color, board);

        GameObject g;
        int[] p = new int[]{0, 0,0,0,0};
        List<int []> path = new ArrayList<>();
        path.add(p);

        if (color.equals("WHITE")){
           //g = maximize(depth, board);

            g = new GameObject(board, color, Integer.MIN_VALUE,path);

            for(int i = 0; i< moves.size();i++){
                moves.get(i);
                int[][] candidate_board = moving(board, moves.get(i), 1);
                //int tmpScore = scoreCalculation("WHITE", candidate_board);
                GameObject minGameObj; //= new GameObject(candidate_board, "BLACK", tmpScore);
                int[] tmp_p = moves.get(i);
                List<int[]> tmp_path = new ArrayList<>();
                tmp_path.add(tmp_p);
                //System.out.println(tmp_path.get(0)[2] + " " + tmp_path.get(0)[3]);
                minGameObj = minimize(depth - 1, candidate_board, tmp_path);

                if (minGameObj.getScore() > g.getScore()){
                    g.updateScore(minGameObj.getScore());
                    g.updateBoard(minGameObj.getBoard());
                    g.updatePath(minGameObj.getPath());
                }

            }
        }
        else{
           //g = minimize(depth, board);
            g = new GameObject(board, color, Integer.MAX_VALUE, path);

            for(int i = 0; i< moves.size();i++){
                moves.get(i);
                int[][] candidate_board = moving(board, moves.get(i), -1);
                //int tmpScore = scoreCalculation("BLACK", candidate_board);
                int[] tmp_p = moves.get(i);
                List<int[]> tmp_path = new ArrayList<>();
                tmp_path.add(tmp_p);
                GameObject maxGameObj; //= new GameObject(candidate_board, "BLACK", tmpScore);
                maxGameObj = maximize(depth - 1, candidate_board, tmp_path);

                if (maxGameObj.getScore() < g.getScore()){
                    g.updateScore(maxGameObj.getScore());
                    g.updateBoard(maxGameObj.getBoard());
                    g.updatePath(maxGameObj.getPath());
                }

            }
        }

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
            int score = scoreCalculation(path.get(path.size()-1)[3], path.get(path.size()-1)[4]);
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
        List<int[]> moves = findMoves(start_position_not_empty, "BLACK", board);


        for(int i = 0; i< moves.size();i++){
            moves.get(i);
            int[][] candidate_board = moving(board, moves.get(i), -1);
            //int tmpScore = scoreCalculation("BLACK", candidate_board);
            int[] tmp_p = moves.get(i);
            List<int[]> tmp_path = new ArrayList<>();
            tmp_path.add(tmp_p);
            GameObject maxGameObj; //= new GameObject(candidate_board, "BLACK", tmpScore);
            maxGameObj = maximize(depth - 1, candidate_board, tmp_path);

            if (maxGameObj.getScore() < g.getScore()){
                g.updateScore(maxGameObj.getScore());
                g.updateBoard(maxGameObj.getBoard());
                g.updatePath(maxGameObj.getPath());
            }

        }


        return g;
    }

    public GameObject maximize(int depth, int[][] board, List<int[]> path){ //white
        GameObject g = new GameObject(board, "WHITE", Integer.MIN_VALUE, path);
        if (depth == 0){
            int score = scoreCalculation(path.get(path.size()-1)[3], path.get(path.size()-1)[4]);
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
        List<int[]> moves = findMoves(start_position_not_empty, "WHITE", board);

        for(int i = 0; i< moves.size();i++){
            moves.get(i);
            int[][] candidate_board = moving(board, moves.get(i), 1);
            //int tmpScore = scoreCalculation("WHITE", candidate_board);
            int[] tmp_p = moves.get(i);
            List<int[]> tmp_path = new ArrayList<>();
            tmp_path.add(tmp_p);
            GameObject minGameObj; //= new GameObject(candidate_board, "BLACK", tmpScore);
            minGameObj = minimize(depth - 1, candidate_board, tmp_path);

            if (minGameObj.getScore() > g.getScore()){
                g.updateScore(minGameObj.getScore());
                g.updateBoard(minGameObj.getBoard());
                g.updatePath(minGameObj.getPath());
            }

        }


        return g;
    }

    public int[][] moving(int[][] board, int[] move, int piece_type){
        int from_i = move[1]; //y
        int from_j = move[2]; //x
        int to_i = move[3]; //y
        int to_j = move[4]; //x
        int jump = move[0]; //empty: 0, jump: 1
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

    public List<int[]> findMoves(boolean start_position_not_empty, String color, int[][] board) {

        //need more directions
        //first check if start positions empty or not
        //start position black:(0,0)(1,0)(2,0)(3,0)(4,0), (0,1)(1,1)(2,1)(3,1)(4,1), (0,2)(1,2)(2,2)(3, 2), (0,3)(1,3)(2,3), (0,4)(1,4)
        //start position white:(14,11)(15,11), (13,12)(14,12)(15,12), (12,13)(13,13)(14,13)(15,13), (11,14)(12,14)(13,14)(14,14)(15,14), (11,15)(12,15)(13,15)(14,15)(15,15)
        List<int[]> moves = new ArrayList<>();

        int i;

        if (color.equals("WHITE"))
            i = 15;
        else
            i = 0;

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





        while(i >= 0 && i < 16) {
            if (start_position_not_empty == true && ((color.equals("BLACK") && i > 4 ) || (color.equals("WHITE") && i < 11)))
                break;
            for (int j = 0; j < 16; j++) {
                int[] m = new int[5];//from x, y to x, y
                m[0] = 0; //default: adjacent empty 0, jump 1
                m[1] = i;
                m[2] = j;

                if ((start_position_not_empty == true) && ((color.equals("BLACK") && (i <= 1 && j <= 4) || (i == 2 && j <= 3) || (i == 3 && j <= 2) || (i == 4 && j <= 1)) || (color.equals("WHITE") && (i == 11 && j >= 14) || (i == 12 && j >= 13) || (i == 13 && j>= 12) || (i >= 14 && j >= 11)))) {

                    if (color.equals("BLACK") && board[i][j] == -1) {
                        for (int[] empty: black_empty_moves){
                            if (i+empty[0] < 16 && j+empty[1] < 16 && board[i+empty[0]][j+empty[1]] == 0){
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }

/*
                        if (i + 1 < 16 && j + 1 < 16) { //adjacent empty
                            if (board[i + 1][j] == 0) {
                                m[3] = i + 1;
                                m[4] = j;
                                moves.add(m);
                            }
                            if (board[i][j + 1] == 0) {
                                m[3] = i;
                                m[4] = j + 1;
                                moves.add(m);
                            }
                            if (board[i + 1][j + 1] == 0) {
                                m[3] = i + 1;
                                m[4] = j + 1;
                                moves.add(m);
                            }
                        }
*/
                        for (int[] empty: black_jump_moves){
                            if (i+empty[0] < 16 && j+empty[1] < 16 && board[i+empty[0]][j+empty[1]] == 0 && board[i+empty[2]][j+empty[3]] != 0){
                                m[0] = 1;
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }
/*
                        if (i + 2 < 16 && j + 2 < 16) { //jump
                            if (board[i + 1][j] != 0 && board[i + 2][j] == 0) {
                                m[0] = 1;
                                m[3] = i + 2;
                                m[4] = j;
                                moves.add(m);
                            }
                            if (board[i][j + 1] != 0 && board[i][j + 2] == 0) {
                                m[0] = 1;
                                m[3] = i;
                                m[4] = j + 2;
                                moves.add(m);
                            }
                            if (board[i + 1][j + 1] != 0 && board[i + 2][j + 2] == 0) {
                                m[0] = 1;
                                m[3] = i + 2;
                                m[4] = j + 2;
                                moves.add(m);
                            }

                        }
*/
                    } else if (color.equals("WHITE") && board[i][j] == 1){ //WHITE

                        for (int[] empty: white_empty_moves){
                            if (i+empty[0] >= 0 && j+empty[1] >= 0 && board[i+empty[0]][j+empty[1]] == 0){
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }
/*
                        if (i - 1 >= 0 && j - 1 >= 0) { //adjacent empty
                            if (board[i - 1][j] == 0) {
                                m[3] = i - 1;
                                m[4] = j;
                                moves.add(m);
                            }
                            if (board[i][j - 1] == 0) {
                                m[3] = i;
                                m[4] = j - 1;
                                moves.add(m);
                            }
                            if (board[i - 1][j - 1] == 0) {
                                m[3] = i - 1;
                                m[4] = j - 1;
                                moves.add(m);
                            }
                        }
*/
                        for (int[] empty: white_jump_moves){
                            if (i+empty[0] >= 0 && j+empty[1] >= 0 && board[i+empty[0]][j+empty[1]] == 0 && board[i+empty[2]][j+empty[3]]!=0){
                                m[0] = 1;
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }
/*
                        if (i - 2 >= 0 && j - 2 >= 0) { //jump
                            if (board[i - 1][j] != 0 && board[i - 2][j] == 0) {
                                m[0] = 1;
                                m[3] = i - 2;
                                m[4] = j;
                                moves.add(m);
                            }
                            if (board[i][j - 1] != 0 && board[i][j - 2] == 0) {
                                m[0] = 1;
                                m[3] = i;
                                m[4] = j - 2;
                                moves.add(m);
                            }
                            if (board[i - 1][j - 1] != 0 && board[i - 2][j - 2] == 0) {
                                m[0] = 1;
                                m[3] = i - 2;
                                m[4] = j - 2;
                                moves.add(m);
                            }


                        }
   */
                    }
                }
                else if (start_position_not_empty == false){ //start_position_empty == false
                    if (color.equals("BLACK") && board[i][j] == -1) {
                        for (int[] empty : black_empty_moves) {
                            if (i + empty[0] < 16 && j + empty[1] < 16 && board[i + empty[0]][j + empty[1]] == 0) {
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }
                        for (int[] empty: black_jump_moves){
                            if (i+empty[0] < 16 && j+empty[1] < 16 && board[i+empty[0]][j+empty[1]] == 0 && board[i+empty[2]][j+empty[3]] != 0){
                                m[0] = 1;
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }

                    }
                    else if (color.equals("WHITE") && board[i][j] == 1){
                        for (int[] empty: white_empty_moves){
                            if (i+empty[0] >= 0 && j+empty[1] >= 0 && board[i+empty[0]][j+empty[1]] == 0){
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
                            }
                        }
                        for (int[] empty: white_jump_moves){
                            if (i+empty[0] >= 0 && j+empty[1] >= 0 && board[i+empty[0]][j+empty[1]] == 0 && board[i+empty[2]][j+empty[3]]!=0){
                                m[0] = 1;
                                m[3] = i + empty[0];
                                m[4] = j + empty[1];
                                moves.add(m);
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
/*
        for (int k = 0; k < moves.size();k++){
            System.out.print(color+ " ");
            System.out.print(moves.get(k)[0] + " " + moves.get(k)[1] + " " + moves.get(k)[2] + " " + moves.get(k)[3] + " "+ moves.get(k)[4]);
        }
        System.out.print('\n');
*/
        return moves;
    }


}



