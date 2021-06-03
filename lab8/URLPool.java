import java.util.*;

/** 
 * A URL pool class to store a list of URLs to be searched with depth.
 * Stored as a URLDepthPair instance.
 */
public class URLPool {

    private LinkedList<URLDepthPair> indexingSites;
    public LinkedList<URLDepthPair> remainingSites;
    private LinkedList<String> urls = new LinkedList<String>();
    public int waitingThreads;
    private int maxDepth;

    public URLPool(int maxDepth) {
        waitingThreads = 0;
        indexingSites = new LinkedList<URLDepthPair>();
        remainingSites = new LinkedList<URLDepthPair>();
        this.maxDepth = maxDepth;
    }
    
    public int getMaxDepth(){
        return maxDepth;
    }
    /** Synchronized method to get the number of waiting threads. */
    public synchronized int getWaitThreads() {
        return waitingThreads;
    }
    // public synchronized void addWaitThreads() {
    //     waitingThreads += 1;
    // }
    
    /** Synchronized method to return the size of the pool. */
    public synchronized int sizeRemaing() {
        return remainingSites.size();
    }
    public synchronized int sizeInd() {
        return indexingSites.size();
    }
    
    public synchronized boolean isIndexed(URLDepthPair Pair){
        if (indexingSites.contains(Pair)){
            return true;
        }
        return false;
    }

    public synchronized int getIndexNumber(URLDepthPair Pair){
        return indexingSites.indexOf(Pair);
    }

    public synchronized void indexing(URLDepthPair Pair){
        indexingSites.add(Pair);
    }
    /** Synchronized method to add a depth pair to the pool. */

    public synchronized void putPairList(LinkedList<URLDepthPair> list){
        if (list.size() != 0){
            for (URLDepthPair pair: list){
                if (!remainingSites.contains(pair)) {
                    remainingSites.addLast(pair); 
                }
            }
            waitingThreads--;
            this.notify();
        }   
    }
    public synchronized void put(URLDepthPair Pair) {

        

        if (!remainingSites.contains(Pair)) {
            remainingSites.addLast(Pair);
            // added = true;
        }

        // return added;
        }

    /**
     * A synchronized method to get the next depth pair from the pool.
     */
    public synchronized URLDepthPair get() {

        URLDepthPair currentPair;

        if (remainingSites.size() == 0) {
            waitingThreads++;
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                // System.err.println("MalformedURLException: " + e.getMessage());
                
            }
            return null;
        } 

        currentPair = remainingSites.pop();
        remainingSites.remove(currentPair);
        return currentPair;
    }

    public synchronized LinkedList<String> getSeenList() {
        return urls;
    }
    public synchronized void putInSeenList(String url) {
        urls.add(url);
    }

    public void writeAllIndexed(){
        for (int i = 0; i < indexingSites.size(); i++) {
            System.out.println(indexingSites.get(i).getUrl());
        }
        System.out.println("All indexed ursl: " + Integer.toString(indexingSites.size()));
    }
}