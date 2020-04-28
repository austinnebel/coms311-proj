
public static class Interval{

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
        if(this.getHigh() < comp.getLow()){
            return -1;
        }
        //this is greater than comp
        else if(this.getLow() > comp.getHigh()){
            return 1;
        }
        //one is inside the other
        else{
            return 0;
        }
    }
}