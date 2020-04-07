import java.util.Random;
import java.util.*;
import java.lang.*;

/**
 * Treap: http://ja.wikipedia.org/wiki/Treap
 * 
 * @authors: Austin Nebel, Rithvik Menon
 * 
*/
public class Treap {

    public boolean DEBUG = true;
    public static final Random rand = new Random();
    public Node root;
    public int size, height;
    
    height = depthOfTree(this.root, 1);
    
    public Node getRoot() {
     return this.root;   
    }
    
    public int getSize() {
     return this.size;   
    }
    public int getHeight() {
     return this.height;   
    }
    

    public static void main(String[] args){

        Treap treap = new Treap();
        treap.add(new Interval(5,10));
        treap.add(new Interval(11,15));
        treap.add(new Interval(20,25));
        treap.add(new Interval(0,1));
        treap.add(new Interval(3,4));

        System.out.println(overlappingIntervals(new Interval(9, 21), treap.root, 1));
        //System.out.println(treap.root.toString());
        int d = depthOfTree(treap.root, 1);   
        printLevelOrder(treap.root, d); 
        
 }

    private void print(String message){
        if(DEBUG){
            System.out.println(message);
        }
    }

    public void add(Interval data) {
        root = intervalInsert(root, data);
    }

    /* Adds a node to the tree
     * 
     * Args:
     *      Node node: The root node to rotate
     *      Interval interv: The interval of the new node to be added
     * Returns:
     *      The node that was added to the tree.
     */
    private Node intervalInsert(Node root, Interval interv) {

        if (root == null){
            if(this.root == null){
                print(interv + "is now root");
            }
            return new Node(interv);
        }

        print("Insterting " + interv);
        //if data less than root data
        if (interv.low < root.interv.low) {

            print("Adding " + interv + "to left of root");

            //add data to the left 
            root.left = intervalInsert(root.left, interv);

            //sets root imax to whatever is larger
            if(root.left.imax > root.imax){
                root.imax = root.left.imax;
            }

            //if new node has higher priority than root, rotate new node 
            //right to take root node's positition
            if (root.left.priority < root.priority){
                return rotateRight(root);
            }
        //if data greater than root data
        } else if (interv.low > root.interv.low) {

            print("Adding " + interv + "to right of root");

            //add data to right
            root.right = intervalInsert(root.right, interv);

            //sets root imax to whatever is larger
            if(root.right.imax > root.imax){
                root.imax = root.right.imax;
            }

            //if new node has higher priority than root, rotate new node 
            //left to take root node's positition
            if (root.right.priority < root.priority){
                return rotateLeft(root);
            }
        }
        return root;
    }

    /* Rotates the treap with root 'node' to the right
     * 
     * Args:
     *      Node node: The root node to rotate
     */
    private Node rotateRight(Node root) {

        Node lnode = root.left;
        root.left = lnode.right;
        lnode.right = root;
        updateImax(root);
        updateImax(lnode);
        return lnode;
    }

    /* Rotates the treap with root 'node' to the left
     * 
     * Args:
     *      Node<T> node: The root node to rotate
     */
    private Node rotateLeft(Node root) {
        Node rnode = root.right;
        root.right = rnode.left;
        rnode.left = root;
        updateImax(root);
        updateImax(rnode);
        return rnode;
    }

    /**
     * Updates imax values to be correct. Should be done on rotations
     * and insertions.
     */
    public void updateImax(Node root){
        if(root == null){
            return;
        }
        if(root.right == null && root.left == null){
            root.imax = root.interv.high;
        }else if(root.right == null){
            root.imax = Math.max(root.interv.high, root.left.imax);
        }else if(root.left == null){
            root.imax = Math.max(root.interv.high, root.right.imax);
        }else{
            root.imax = Math.max(root.interv.high, Math.max(root.right.imax, root.left.imax));
        }        
    }

    /* Removes a node from the tree that has the specified data,
     * starting at the root.
     * 
     * Args:
     *      Interval data: The interval to be found and removed
     */
    public void remove(Interval interv) {
        root = intervalDelete(root, interv);
    }

    /* Removes a node from the treap. 
     * 
     * Args:
     *      Node<T> root: The root node to start searching from
     *      T data: The data of the node to be removed  
     */
    private Node intervalDelete(Node root, Interval interv) {

        if (root != null) {

            //Compares root note data to data we're looking for
            int compare = interv.compareTo(root.interv);

            //if data is less than root, recursive call to the left
            if (compare < 0) {
                root.left = intervalDelete(root.left, interv);

            //if data is greater than root, recursive call to the right
            } else if (compare > 0) {
                root.right = intervalDelete(root.right, interv);
            
            //if data is the same as root data
            } else {

                //if only right child exist, return it
                if (root.left == null) {
                    return root.right;

                //if only left child exist, return it
                } else if (root.right == null) {
                    return root.left;

                //if root is a leaf node
                } else {
                    root.interv = first(root.right);
                    root.right = intervalDelete(root.right, root.interv);
                }
            }
        }
        return root;
    }

    /* Returns whether the tree contains the specified data.
     * 
     * Returns:
     *      True if the tree contains the data, false otherwise.
     */
    public boolean intervalSearch(Interval interv) {

        Node node = root;

        while (node != null) {
            
            //iterates either left or right down the tree
            if (interv.low < node.interv.low){
                node = node.left;

            }else if(interv.low > node.interv.low){
                node = node.right;

            }else{
                return true;
            }
        }
        return false;
    }

    /* Finds the node furthest to the left in the tree, starting at
     * the root of the tree.
     * 
     * Returns:
     *      The leftmost node in the tree.
     */
    public Interval first() {
        return first(root);
    }

    /* Finds the node furthest to the left in the tree, starting at
     * the specified root.
     *
     * Args:
     *      Node root: The node to start searching from
     * 
     * Returns:
     *      The interval at the end of the tree
     */
    private Interval first(Node root) {

        Node node = root;
        while (node.left != null){
            node = node.left;
        }

        return node.interv;
    }

    @Override
    public String toString() {

        return "Treap{" +
                "root=" + root +
                '}';
    }


    
    static int depthOfTree(Node root, int d) {
        if(root == null) {
            return d;
        }
        int left = d;
        int right = d;
        if(root.left != null) {
            left = depthOfTree(root.left, d+1);
        }
        if(root.right != null) {
            right = depthOfTree(root.right, d+1);
        }
        return Math.max(left, right);
    }
      
    static void printLevelOrder(Node root, int depth)
    {
        if(root == null)
            return;
    
        Queue<Node> q =new LinkedList<Node>();
    
        q.add(root);            
        while(true)
        {               
            int nodeCount = q.size();
            if(nodeCount == 0)
                break;
            for(int i=0; i<depth; i++) {
            System.out.print("       ");
            }
            while(nodeCount > 0)
            {    
                Node node = q.peek();
                System.out.print(Integer.toString(node.imax) + node.interv);
    
                q.remove();
    
                if(node.left != null)
                    q.add(node.left);
                if(node.right != null)
                    q.add(node.right);
    
                if(nodeCount>1){
                    System.out.print("         ");
                }
                nodeCount--;    
            }
            depth--;
            System.out.println();
        }
    }       

    /**
     * Does level order traversal of tree and returns node
     * if it matches exactly with the param interv.
     * 
     * Args:
     *      Interval interv: Interval to search
     *      Node root: root node to search from
     *      int depth: The depth of the current node
     * 
     * Returns:
     *      The node if found, null otherwise
     */
    static Node intervalSearchExactly(Interval interv, Node root, int depth)
    {
        if(root == null)
            return null;
    
        Queue<Node> q =new LinkedList<Node>();
    
        q.add(root);            
        while(true)
        {               
            int nodeCount = q.size();
            if(nodeCount == 0)
                break;

            while(nodeCount > 0)
            {    
                Node node = q.peek();
    
                if(node.interv.low == interv.low && node.interv.high == interv.high){
                    return node;
                }
                q.remove();
    
                if(node.left != null)
                    q.add(node.left);
                if(node.right != null)
                    q.add(node.right);
    
                nodeCount--;    
            }
            depth--;
        }
        return null;
    }       

    /**
     * Does level order traversal of tree and returns a list of overlapping intervals.
     * 
     * Args:
     *      Interval interv: Interval to search
     *      Node root: root node to search from
     *      int depth: The depth of the current node
     * 
     * Returns:
     *      ArrayList<Node> list of all overlapping intervals
     */
    static ArrayList<Node> overlappingIntervals(Interval interv, Node root, int depth)
    {
        if(root == null)
            return null;
    
        ArrayList<Node> list = new ArrayList<Node>();

        Queue<Node> q =new LinkedList<Node>();
    
        q.add(root);            
        while(true)
        {               
            int nodeCount = q.size();
            if(nodeCount == 0)
                break;

            while(nodeCount > 0)
            {    
                Node node = q.peek();
    
                if(node.interv.low < interv.low && node.interv.high > interv.low) {
                    list.add(node);
                }

                else if(node.interv.low > interv.low && node.interv.low < interv.high) {
                    list.add(node);
                }


                q.remove();
    
                if(node.left != null)
                    q.add(node.left);
                if(node.right != null)
                    q.add(node.right);
    
                nodeCount--;    
            }
            depth--;
        }
        return list;
    }     

    private static class Node {

        public Node right, left, parent;
        public Interval interv; //the nodes interval
        public int imax; //the biggest max interval in subtree
        public int priority = rand.nextInt(1000); //the nodes priority

        public Node(Interval interv) {
            this.interv = interv;
            this.imax = interv.high;
        }
        public Node(Integer a, Integer b) {
            this.interv = new Interval(a, b);
            this.imax = this.interv.high;

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

    private static class Interval{

        public int low, high;

        public Interval(Integer low, Integer high){
            this.low = low;
            this.high = high;
        }
        
        public int getLow() {
         return this.low;   
        }
        
        public int getHigh() {
         return this.high;   
        }

        @Override
        public String toString() {
            return "I{"+ low +"-"+ high +"}";
        }        
        
        /**
         * Returns an integer variable based on the interval overlap.
         * 
         * Returns:
         *      -1 when this is less than comp
         *              |----this----|
         *                             |-----comp-----|
         *       0 when one of these overlaps eachother
         *                 |-----either------|
         *                   |----either----|
         *       1 when this is larger than comp
         *                              |----this----|
         *            |-----comp-----|
         */
        public int compareTo(Interval comp){

            //this is less than comp
            if(this.high < comp.low){
                return -1;
            }
            //this is greater than comp
            else if(this.low > comp.high){
                return 1;
            }
            //one is inside the other
            else{
                return 0;
            }
        }
    }
}

