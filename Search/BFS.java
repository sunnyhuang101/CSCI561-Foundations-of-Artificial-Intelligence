import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class BFS {
    private Node start;
    private long[][] map;
    private long max;

    BFS(Node start, long[][] map, long max){
        this.start = start;
        this.map = map;
        this.max = max;
    }

    public Node getStart(){return this.start; }
    public long[][] getMap(){return this.map; }
    public long getMax(){return this.max; }

    public List<Node> findPath(Node target){
        Queue<Node> queue = new LinkedList<Node> ();
        int rows = this.map.length;
        int cols = this.map[0].length;
        boolean [][] visited = new boolean [rows][cols];
        for (int i = 0;i<rows;i++){
            for (int j = 0;j<cols;j++){
                visited[i][j] = false;
            }
        }

        queue.offer(this.start);
        int success = 0;
        Node tail = null;


        while (queue.isEmpty() != true) {
            Node node = queue.poll();
            visited[node.getY()][node.getX()] = true;
            if (node.equals(target)) {
                success = 1;
                tail = node;
                break;
            }
            //iterate through neighbors
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((i != 0 || j != 0) && (node.getX() + i) >= 0 && (node.getX() + i) < cols
                            && (node.getY() + j) >= 0 && (node.getY() + j) < rows) {
                        Node neighbor = new Node(node, node.getX() + i, node.getY() + j, map[node.getY() + j][node.getX() + i]);
                        //System.out.println( Math.abs(neighbor.getZ() - node.getZ()));
                        if (Math.abs(neighbor.getZ() - node.getZ()) <= this.max && visited[neighbor.getY()][neighbor.getX()] == false) {
                            visited[neighbor.getY()][neighbor.getX()] = true;
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }



        if (success == 0) {
           // System.out.println("FAIL");
            return new ArrayList<>();
        }
        else{
            List<Node> result = new ArrayList<>();
            Node cur = tail;
            while (cur != null){
                result.add(cur);
                cur = cur.getParent();
            }


            return result;
        }

    }
}
