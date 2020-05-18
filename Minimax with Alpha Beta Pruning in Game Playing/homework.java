import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalTime;
import java.util.*;

public class homework {
    public static void main(String args[]) throws FileNotFoundException {
        //LocalTime start_time = LocalTime.now();
        //System.out.println(start_time);

        int[][] original_board = new int[16][16];

        FileInputStream in = new FileInputStream("src/input1-5.txt");
        Scanner scanner = new Scanner(in);
        String scenario = scanner.nextLine();
        String color = scanner.nextLine();
        String tmp_time = scanner.nextLine();
        float time = Float.parseFloat(tmp_time);
        /*
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(3, 1);
        map.put(5, 2)
;       map.put(1, 3);
        map.put(6,3);
        map.put(2, 2);
        for(Map.Entry e: map.entrySet()){
            System.out.println(e.getKey() + " " + e.getValue());
        }
        */
        //System.out.println(scanner.nextLine().toCharArray());
        /*
        int ii = 0;

        while(scanner.hasNextLine() && ii < 16){
            char[] c_array = scanner.nextLine().toCharArray();
            System.out.println(c_array);
            for(int j = 0; j< 16;j++){ //char c: c_array
                char c = c_array[j];
                if (c == 'B'){

                    board[ii][j] = -1;//Black: -1, White =  1;
                }
                else if (c == 'W'){
                    board[ii][j] = 1;
                }
                else{
                    board[ii][j] = 0; //.
                }

                System.out.print(board[ii][j]);

            }
            ii++;
            System.out.print('\n');
        }*/

        //scanner.nextLine();
        for(int i = 0;i < 16;i++){
            char[] c_array = scanner.nextLine().toCharArray();
           // System.out.println(c_array);
            int j = 0;
            for(char c : c_array){ //char c: c_array
                //System.out.println(c_array[j]);
                //char c = c_array[j];
                if (c == 'B'){

                    original_board[i][j] = -1;//Black: -1, White =  1;
                }
                else if (c == 'W'){
                    original_board[i][j] = 1;
                }
                else{
                    original_board[i][j] = 0; //.
                }

           // System.out.print(original_board[i][j]);
                j++;
            }
         //System.out.print('\n');
        }
        /*
        for (int x =0;x<16;x++){
            for(int y = 0;y<16;y++){
                System.out.print(original_board[x][y]);
            }
            System.out.print('\n');
        }
        */
        /*
        char[] c_array = scanner.nextLine().toCharArray();
        for(int j = 0;j<16;j++){
            if (c_array[j] == 'B'){

                board[15][j] = -1;//Black: -1, White =  1;
            }
            else if (c_array[j] == 'W'){
                board[15][j] = 1;
            }
            else{
                board[15][j] = 0; //.
            }
           System.out.print(board[15][j]);
        }
*/

        scanner.close();
        List<int[]> result = new ArrayList<>();
        int depth = 1; // according to left time
        List<String> result_strings = new ArrayList<>();

        if (scenario.equals("SINGLE")){
            //Halma halma = new Halma();
            //result = halma.minimax(original_board, 1, color);
            HalmaGame halmagame = new HalmaGame();
            result = halmagame.minimax(original_board, 1, color);
        }
        else{
            HalmaGame halmagame = new HalmaGame();
            result = halmagame.minimax(original_board, 2, color);
        }

        //GameObject game = new GameObject(depth, scenario, board, color);

       // System.out.println(halma.scoreCalculation(color,board));


        //halma.minimize(game, depth);

/*
        for (int i = 0; i< result.size();i++){
            String result_str = "";
            if (result.get(i)[0] == 0){
                result_str = "E ";
            }
            else {
                result_str = "J ";
            }
            result_str = result_str + result.get(i)[2] + "," + result.get(i)[1] + " " +  result.get(i)[4] + "," +  result.get(i)[3];
            result_strings.add(result_str);
            System.out.println(result_str);
        }
*/

        File out=new File("src/output.txt");
        try{
            //out.createNewFile();
            FileWriter fwriter=new FileWriter(out);
            //for (int i = 0;i < result_strings.size();i++){
              //  fwriter.write(result_strings.get(i));
           // }

            if (result.get(0)[0] == 0){
                String result_str = "";
                //result_str = "E ";
                for (int i = 1; i< result.size();i++){

                    result_str = "E "+result.get(i)[1] + "," + result.get(i)[0] + " " +  result.get(i)[3] + "," +  result.get(i)[2];
                    //result_strings.add(result_str);
                    System.out.println(result_str);
                    fwriter.write(result_str); //result_strings.get(i)
                }
            }
            else {
                //result_str = "J ";
                for (int i = 1; i< result.size() - 2;i++){
                    String result_str = "";
                    result_str = "J "+ result.get(i)[1] + "," + result.get(i)[0] + " " +  result.get(i+1)[1] + "," +  result.get(i+1)[0] + "\n";
                    //result_strings.add(result_str);
                    System.out.println(result_str);
                    fwriter.write(result_str); //result_strings.get(i)
                }
                String result_str = "";
                result_str = "J "+ result.get(result.size() - 2)[1] + "," + result.get(result.size() - 2)[0] + " " +  result.get(result.size() - 1)[1] + "," +  result.get(result.size() - 1)[0];
                System.out.println(result_str);
                fwriter.write(result_str);
                //System.out.println(result.size());
            }

            //LocalTime end_time = LocalTime.now();
            //System.out.println(start_time);
            //System.out.println(end_time);
            //System.out.println(end_time.getNano() - start_time.getNano());
            fwriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
