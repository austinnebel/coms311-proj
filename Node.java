
public static class Node {

    public Node right, left, parent;
    public Interval interv; //the nodes interval
    public int imax; //the biggest max interval in subtree
    public int priority = rand.nextInt(1000); //the nodes priority

    public Node(Interval interv) {
        this.interv = interv;
        this.imax = interv.getHigh();
    }
    
    public Node getParent() {
     return this.parent;   
    }
    
    public Node getLeft() {
     return this.left;   
    }
    
    public Node getRight() {
     return this.right;   
    }
    
    public Interval getInterv() {
     return this.interv;   
    }
    
    public int getImax() {
     return this.imax;   
    }
    
    public int getPriority() {
     return this.priority;   
    }

    @Override
    public String toString() {
        return "Node{" +
                "interv=" + interv +
                ", priority=" + priority +
                ", imax=" + imax +
                //",\n      left=" + left +
                //",\n      right=" + right +
                '}';
    }


}