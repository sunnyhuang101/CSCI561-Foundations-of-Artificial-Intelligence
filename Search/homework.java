import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.*;
import java.util.*;

public class homework {





    public static void main(String args[]) throws FileNotFoundException  {

        FileInputStream in = new FileInputStream("src/input1-12.txt");
        Scanner scanner = new Scanner(in);
        String algo = scanner.nextLine();
        int col = scanner.nextInt(); //w: columns
        int row = scanner.nextInt(); //h: rows
        int landx = scanner.nextInt();
        int landy = scanner.nextInt();
        long maxz = scanner.nextLong();
        int n = scanner.nextInt();


        int ridx = 0;
        int[][] targets; //targets indices

        targets = new int[n][2];


        while(ridx < n){
            targets[ridx][0] = scanner.nextInt();
            targets[ridx][1] = scanner.nextInt();
            ridx++;
        }
        //scanner.hasNextLine()
        long[][] map = new long[row][col];

        for(int i =0;i<row;i++) {

            for (int j = 0; j < col; j++) {
                map[i][j] = scanner.nextLong();
               // System.out.println(map[i][j]);
            }

        }

        scanner.close();

        Node startnode = new Node(null, landx, landy, map[landy][landx]);
        List<Node> result = new ArrayList<>();
        List<String> result_strings = new ArrayList<>();

        if (algo.compareTo("BFS") == 0) {
            // System.out.println("BFS");
            BFS bfs = new BFS(startnode, map, maxz);
            for (int i = 0; i < n; i++) {
                //System.out.println(map[targets[0][0]][targets[0][1]]);


                Node t = new Node(null, targets[i][0], targets[i][1], map[targets[i][1]][targets[i][0]]);
                //notice the difference between map storage and how we read coordinates!!!
                result = bfs.findPath(t);

                String ans = "";
                if (result.isEmpty() == true) {
                    ans = "FAIL";
                } else {
                    for (int j = result.size() - 1; j > 0; j--) {
                        ans = ans + result.get(j).getX() + "," + result.get(j).getY() + " ";
                        //System.out.println(result.get(j).getX() + "," + result.get(j).getY() + " ");
                    }
                    if (i != n - 1) {
                        ans = ans + result.get(0).getX() + "," + result.get(0).getY() + "\n";
                    } else {
                        ans = ans + result.get(0).getX() + "," + result.get(0).getY();
                    }
                    //System.out.println(result.get(0).getX() + "," + result.get(0).getY() );
                }
                result_strings.add(ans);

            }


        } else if (algo.compareTo("UCS") == 0) {
            // System.out.println("UCS");
            UCS ucs = new UCS(startnode, map, maxz);
            for (int i = 0; i < n; i++) {
                Node t = new Node(null, targets[i][0], targets[i][1], map[targets[i][1]][targets[i][0]]);
                //notice the difference between map storage and how we read coordinates!!!
                result = ucs.findPath(t);
                String ans = "";
                if (result.isEmpty() == true) {
                    ans = "FAIL";
                } else {
                    for (int j = result.size() - 1; j > 0; j--) {
                        ans = ans + result.get(j).getX() + "," + result.get(j).getY() + " ";
                        // System.out.println(result.get(j).getX() + "," + result.get(j).getY() + " ");
                    }
                    if (i != n - 1) {
                        ans = ans + result.get(0).getX() + "," + result.get(0).getY() + "\n";
                    } else {
                        ans = ans + result.get(0).getX() + "," + result.get(0).getY();
                    }
                    //System.out.println(result.get(0).getX() + "," + result.get(0).getY() );
                }
                result_strings.add(ans);

            }
        } else if (algo.compareTo("A*") == 0) {

            Astar astar = new Astar(startnode, map, maxz);

            for (int i = 0; i < n; i++) {
                //System.out.println(map[targets[0][0]][targets[0][1]]);
                Node t = new Node(null, targets[i][0], targets[i][1], map[targets[i][1]][targets[i][0]]);
                //notice the difference between map storage and how we read coordinates!!!
                result = astar.findPath(t);
                String ans = "";
                if (result.isEmpty() == true) {
                    ans = "FAIL";
                } else {
                    for (int j = result.size() - 1; j > 0; j--) {
                        ans = ans + result.get(j).getX() + "," + result.get(j).getY() + " ";
                        // System.out.println(result.get(j).getX() + "," + result.get(j).getY() + " ");
                    }
                    if (i != n - 1) {
                        ans = ans + result.get(0).getX() + "," + result.get(0).getY() + "\n";
                    } else {
                        ans = ans + result.get(0).getX() + "," + result.get(0).getY();
                    }
                    //System.out.println(result.get(0).getX() + "," + result.get(0).getY() );
                }
                result_strings.add(ans);

            }


        }



        File out=new File("src/output.txt");
        try{
            //out.createNewFile();
            FileWriter fwriter=new FileWriter(out);
            for (int i = 0;i < result_strings.size();i++){
                fwriter.write(result_strings.get(i));
            }

            fwriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
