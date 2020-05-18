public class JumpObject {

    private JumpObject parent;
    private int move_i;
    private int move_j;
    JumpObject(JumpObject parent, int move_i, int move_j){
        this.parent = parent;
        this.move_i = move_i;
        this.move_j = move_j;
    }
    public int getMoveI(){return this.move_i;}
    public int getMoveJ(){return this.move_j;}
    public JumpObject getParent(){return this.parent;}
}
