import java.util.*;
public class UCS {
    private Node start;
    private long[][] map;
    private long max;

    UCS(Node start, long[][] map, long max){
        this.start = start;
        this.map = map;
        this.max = max;

    }

    public Node getStart(){return this.start; }
    public long[][] getMap(){return this.map; }
    public long getMax(){return this.max; }
/*
    private boolean queueContains(PriorityQueue<Node> olist, Node node){
        boolean same = false;
        for (Node n:olist){
            if (n.getX() == node.getX() && n.getY() == node.getY()){
                same = true;
                break;
            }
        }
        return same;
    }

*/

    public List<Node> findPath(Node target){
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Long.compare(o1.getG() , o2.getG()); //low to high //return (o1.getG() - o2.getG());
            }
        };

        PriorityQueue<Node> pqueue = new PriorityQueue<Node>(comparator);
        HashSet<List> check = new HashSet<>();

        int success = 0;
        int rows = this.map.length;
        int cols = this.map[0].length;
        boolean [][] visited = new boolean [rows][cols];
        for (int i = 0;i<rows;i++){
            for (int j = 0;j<cols;j++){
                visited[i][j] = false;
            }
        }
        this.start.nodeUpdate(0L, 0L, 0L);
        pqueue.add(this.start);

        List<Integer> tmp1 = new ArrayList<>();
        tmp1.add(this.start.getX());
        tmp1.add(this.start.getY());
        check.add(tmp1);


        Node tail = new Node(null, this.start.getX(), this.start.getY(), this.start.getZ());
        tail.nodeUpdate(Long.MAX_VALUE, 0L, 0L);


        while(pqueue.isEmpty() != true) {
            Node node = pqueue.poll();

            List<Integer> tmp2 = new ArrayList<>();
            tmp2.add(node.getX());
            tmp2.add(node.getY());
            check.remove(tmp2);

            //System.out.println("priorityqueue poll: "+ node.getX() + " " + node.getY());
            visited[node.getY()][node.getX()] = true;

            if (node.equals(target) ) {
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
                        neighbor.nodeUpdate(neighbor.getG(), neighbor.getH(), neighbor.getF());
                        //System.out.println( Math.abs(neighbor.getZ() - node.getZ()));
                        if (Math.abs(neighbor.getZ() - node.getZ()) <= this.max) {
                            //exoand
                            List<Integer> tmp3 = new ArrayList<>();
                            tmp3.add(neighbor.getX());
                            tmp3.add(neighbor.getY());

                            //queueContains(pqueue, neighbor) == false
                            if (visited[neighbor.getY()][neighbor.getX()] == false && check.contains(tmp3) == false) {
                                //System.out.println("add to queue");

                                visited[neighbor.getY()][neighbor.getX()] = true;
                                if (i != 0 && j != 0) {  //diagonal
                                    neighbor.nodeUpdate(node.getG() + 14L, 0L, 0L);
                                } else {
                                    neighbor.nodeUpdate(node.getG() + 10L, 0L, 0L);
                                }
                                pqueue.add(neighbor);
                                check.add(tmp3);

                            }//if shorter path is found for nodes in priority queue && diagonal
                            //queueContains(pqueue, neighbor) == true
                            else if (check.contains(tmp3) == true && (i != 0 && j != 0) && node.getG() + 14L < neighbor.getG()) {
                                //System.out.println("update queue");
                                pqueue.remove(neighbor);
                                neighbor.nodeUpdate(node.getG() + 14L, 0L, 0L);
                                pqueue.add(neighbor);

                                //queueContains(pqueue, neighbor) == true
                            } else if ( check.contains(tmp3) == true && node.getG() + 10L < neighbor.getG()) {
                                //System.out.println("update queue");
                                pqueue.remove(neighbor);
                                neighbor.nodeUpdate(node.getG() + 10L, 0L, 0L);
                                pqueue.add(neighbor);
                            }
                            //System.out.println( neighbor.getG());
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
