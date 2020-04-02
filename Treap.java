import java.util.Random;





/**
 * Treap: http://ja.wikipedia.org/wiki/Treap
 * 
 * @authors: Austin Nebel, Rithvik Menon
 * 
*/
public class Treap {

    public static void main(String[] args){

        Treap treap = new Treap();
        treap.add(new Interval(5,10));
        treap.add(new Interval(11,15));

        System.out.println(treap.root.toString());
    }

    private static final Random rand = new Random();
    private Node root;

    public void add(Interval data) {
        root = add(root, data);
    }

    /* Adds a node to the tree
     * 
     * Args:
     *      Node node: The root node to rotate
     * 
     * Returns:
     *      The node that was added to the tree.
     */
    private Node add(Node root, Interval interv) {

        if (root == null)
            return new Node(interv);

        int compare = interv.compareTo(root.interv);

        //if data less than root data
        if (compare < 0) {

            //add data to the left 
            root.left = add(root.left, interv);

            //if the root has greater priority than newly added node, rotate right
            if (root.imax > root.left.imax){
                return rotateRight(root);
            }
        //if data greater than root data
        } else if (compare > 0) {

            //add data to right
            root.right = add(root.right, interv);

            //if the root has greater imax than newly added node, rotate lsft
            if (root.imax > root.right.imax){
                return rotateLeft(root);
            }
        }
        return root;
    }

    /* Rotates the treap with root 'node' to the right
     * 
     * Args:
     *      Node<T> node: The root node to rotate
     */
    private Node rotateRight(Node node) {

        Node lnode = node.left;
        node.left = lnode.right;
        lnode.right = node;
        return lnode;
    }

    /* Rotates the treap with root 'node' to the left
     * 
     * Args:
     *      Node<T> node: The root node to rotate
     */
    private Node rotateLeft(Node node) {
        Node rnode = node.right;
        node.right = rnode.left;
        rnode.left = node;
        return rnode;
    }

    /* Removes a node from the tree that has the specified data,
     * starting at the root.
     * 
     * Args:
     *      Interval data: The interval to be found and removed
     */
    public void remove(Interval interv) {
        root = remove(root, interv);
    }

    /* Removes a node from the treap. 
     * 
     * Args:
     *      Node<T> root: The root node to start searching from
     *      T data: The data of the node to be removed  
     */
    private Node remove(Node root, Interval interv) {

        if (root != null) {

            //Compares root note data to data we're looking for
            int compare = interv.compareTo(root.interv);

            //if data is less than root, recursive call to the left
            if (compare < 0) {
                root.left = remove(root.left, interv);

            //if data is greater than root, recursive call to the right
            } else if (compare > 0) {
                root.right = remove(root.right, interv);
            
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
                    root.right = remove(root.right, root.interv);
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
    public boolean contains(Interval interv) {

        Node node = root;

        while (node != null) {
            int compare = interv.compareTo(node.interv);

            //iterates either left or right down the tree
            if (compare < 0){
                node = node.left;

            }else if(compare > 0){
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

    /////////////////Nothing above this line has been modified///////////////
    
    public void intervalInsert() {

    }

    public void intervalDelete() {
        
    }

    public void intervalSearch(int i) {
    }

    private static class Node {

        public Node right, left;
        public Interval interv; //the nodes interval
        public int imax = rand.nextInt(); //the nodes priority

        public Node(Interval interv) {
            this.interv = interv;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "item=" + interv.toString() +
                    ", priority=" + imax +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    private static class Interval{

        public int low, high;

        public Interval(Integer low, Integer high){
            this.low = low;
            this.high = high;
        }

        @Override
        public String toString() {
            return "Interval{"+ low +"-"+ high +"}";
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

