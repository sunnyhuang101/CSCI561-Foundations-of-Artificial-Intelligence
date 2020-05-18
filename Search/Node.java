import static java.lang.Integer.MAX_VALUE;
public class Node{
    private int x, y;
    private long z;
    private Node parent;
    private long f, g, h;




    Node(Node parent, int x, int y, long z){
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.z = z;

       // this.f = Long.valueOf(Integer.MAX_VALUE);
        //this.g = Long.valueOf(Integer.MAX_VALUE);
        //this.h = Long.valueOf(Integer.MAX_VALUE);

    }

    public void nodeUpdate(long g, long h, long f){
        this.g = g;
        this.f = f;
        this.h = h;
        return;
    }

    public int getX(){return this.x; }
    public int getY(){return this.y; }
    public long getZ(){return this.z; }

    public Node getParent(){return this.parent;}
    public long getG(){return this.g; }
    public long getF(){return this.f; }
    public long getH(){return this.h; }

    public boolean equals(Node t){
        return (this.x == t.getX() && this.y ==t.getY());
    }
}