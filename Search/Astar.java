import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.lang.Math;
import java.util.*;


public class Astar {
    private Node start;
    private long[][] map;
    private long max;

    Astar(Node start, long[][] map, long max){
        this.start = start;
        this.map = map;
        this.max = max;
    }

    public Node getStart(){return this.start; }
    public long[][] getMap(){return this.map; }
    public long getMax(){return this.max; }
/*
    private boolean listContains(List<Node> clist, Node node){
        boolean same = false;
        for (Node n:clist){
            if (n.getX() == node.getX() && n.getY() == node.getY()){
                same = true;
                break;
            }
        }
        return same;

    }
    private boolean queueContains(PriorityQueue<Node> olist, Node node){

        Iterator<Node> iterator = olist.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next().getX());
            if (iterator.next().getX() == nei.getX() && iterator.next().getY() == nei.getY() ){
                same = true;
                break;
            }
        }*/
/*
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

    public List<Node> findPath(Node target) {
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Long.compare(o1.getF() , o2.getF()); //low to high
            } //return (o1.getF() - o2.getF()
        };
        List<Node> closelist = new ArrayList<>();
        PriorityQueue<Node> openlist = new PriorityQueue<Node>(comparator);

        HashSet<List> ocheck = new HashSet<>();
        HashSet<List> ccheck = new HashSet<>();

        this.start.nodeUpdate(0L, 0L, 0L);
        openlist.add(this.start);
        int success = 0;
        int rows = this.map.length;
        int cols = this.map[0].length;

        List<Integer> tmp1 = new ArrayList<>();
        tmp1.add(this.start.getX());
        tmp1.add(this.start.getY());
        ocheck.add(tmp1);


        while (openlist.isEmpty() == false) {

            Node node = openlist.poll();

            List<Integer> tmp2 = new ArrayList<>();
            tmp2.add(node.getX());
            tmp2.add(node.getY());
            ocheck.remove(tmp2);
            //System.out.println("priorityqueue poll: "+ node.getX() + " " + node.getY());
            closelist.add(node);

            ccheck.add(tmp2);


            if (node.equals(target)) {
                success = 1;
                break;
            }

            //add neighbors to openlist
            for (int i = -1;i<=1;i++){
                for (int j = -1;j<=1;j++){
                    if ((i != 0 || j != 0) && (node.getX() + i) >= 0 && (node.getX() + i) < cols
                            && (node.getY() + j)>= 0 && (node.getY() + j) < rows){
                        Node neighbor = new Node(node, node.getX() + i, node.getY() + j, map[node.getY()+j][node.getX()+i]);
                        neighbor.nodeUpdate(neighbor.getG(), neighbor.getH(), neighbor.getF());

                        List<Integer> tmp3 = new ArrayList<>();
                        tmp3.add(neighbor.getX());
                        tmp3.add(neighbor.getY());

                        //listContains(closelist, neighbor) == false
                        if (Math.abs(neighbor.getZ() - node.getZ())<=this.max  && ccheck.contains(tmp3)==false) {
                            int x_distance = Math.abs(target.getX() - neighbor.getX());
                            int y_distance = Math.abs(target.getY() - neighbor.getY());
                            long z_distance = Math.abs(target.getZ() - neighbor.getZ());
                            long h;
                            //System.out.println(Math.min(2147483647L, 2147483647L)*2L);
                            h =  z_distance + Long.valueOf(Math.min(x_distance, y_distance))*14L + Long.valueOf(Math.abs(x_distance - y_distance))*10L;

                            //check if it's walkable or it's not in closelist
                            //queueContains(openlist, neighbor) == true
                            if (ocheck.contains(tmp3)){
                               // System.out.println("InOpend");
                                if (i!=0 && j!=0){  //diagonal

                                    if (( node.getG()+ 14L + Math.abs(neighbor.getZ() - node.getZ())) < neighbor.getG() ){
                                        openlist.remove(neighbor);
                                        //Node neighbor2 = new Node(node, neighbor.getX(), neighbor.getY(), neighbor.getZ());
                                        long g = 14L + Math.abs(neighbor.getZ() - node.getZ());
                                       // int h = 10* (Math.abs(target.getX() - neighbor.getX()) + Math.abs(target.getY() - neighbor.getY()));
                                        long f = g + h;
                                        neighbor.nodeUpdate(g, h, f );
                                        openlist.add(neighbor);
                                    }

                                }
                                else{
                                    if ((node.getG() + 10L + Math.abs(neighbor.getZ() - node.getZ())) < neighbor.getG() ){
                                        openlist.remove(neighbor);
                                       // Node neighbor2 = new Node(node, neighbor.getX(), neighbor.getY(), neighbor.getZ());
                                        long g = 10L + Math.abs(neighbor.getZ() - node.getZ());
                                        //int h = 10* (Math.abs(target.getX() - neighbor.getX()) + Math.abs(target.getY() - neighbor.getY()));
                                        long f = g + h;
                                        neighbor.nodeUpdate(g, h, f );
                                        openlist.add(neighbor);
                                    }
                                }
                            }
                            else{
                                if (i!= 0 && j!= 0){ //diagonal
                                    //System.out.println(neighbor.getG());
                                    long g = 14L + Math.abs(neighbor.getZ() - node.getZ());
                                    //int h = 10* (Math.abs(target.getX() - neighbor.getX()) + Math.abs(target.getY() - neighbor.getY()));
                                    long f = g + h;
                                    neighbor.nodeUpdate(g, h, f );

                                }
                                else{
                                    //System.out.println(neighbor.getG());
                                    long g = 10L + Math.abs(neighbor.getZ() - node.getZ());
                                    //int h = 10* (Math.abs(target.getX() - neighbor.getX()) + Math.abs(target.getY() - neighbor.getY()));
                                    long f = g + h;
                                    neighbor.nodeUpdate(g, h, f );
                                }
                                openlist.add(neighbor);
                                ocheck.add(tmp3);
                               // System.out.println("AddOpen: "+ neighbor.getX() + " " + neighbor.getY());
                            }
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
            Node tail = closelist.get(closelist.size() - 1);
            Node cur = tail;
            while (cur != null){
                result.add(cur);
                cur = cur.getParent();
            }


            return result;
        }

    }
}
