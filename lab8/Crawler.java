import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Crawler {
// https://www.nytimes.com
// https://apache.org/

    public static void main(String[] args) {

        Scanner scaner = new Scanner(System.in);
        System.out.println("Хост:");
        String firstUrl = scaner.nextLine();
        System.out.println("глубина поиска:");
        String maxDepthS = scaner.nextLine();
        System.out.println("количество потоков (от 1 до 100)");
        String threadsS = scaner.nextLine();
        scaner.close();
        int numThreads;
        int maxDepth;
        try {
            maxDepth = Integer.parseInt(maxDepthS);

        } catch (Exception e) {
            System.out.println("Depth must be number");
            return;
        }

        if (maxDepth <= 0 || maxDepth > 10) {
            System.out.println("Depth must be from 1 to 100");
            return;
        }

        try {
            numThreads = Integer.parseInt(threadsS);

        } catch (Exception e) {
            System.out.println("Threads must be number");
            return;
        }

        if (numThreads <= 0 || numThreads > 100) {
            System.out.println("Threads must be from 1 to 100");
            System.out.println("Threads set to 50");
            numThreads = 50;
        }
        if (maxDepth < 3){
            numThreads = 10;
        }

        URLPool mainPool = new URLPool(maxDepth);
        LinkedList <Thread> allThreads = new LinkedList<Thread>();;
        URLDepthPair urlDepthPair = new URLDepthPair(firstUrl, 0);
        mainPool.put(urlDepthPair);
        mainPool.putInSeenList(firstUrl);
        
        int initialActive = Thread.activeCount();
        int dopTime = 0;
        Date currentDate = new Date();
        long startTime = currentDate.getTime() / 1000;
        while(mainPool.getWaitThreads() != numThreads && dopTime < 300) {
            int threadCount = Thread.activeCount();
            if (threadCount - initialActive < numThreads) {
                    dopTime = 0;
                    CrawlerThread crawler = new CrawlerThread(mainPool);
                    allThreads.add(crawler);
                    crawler.start();
            }
            else {
                try {
                    Thread.sleep(100);  // 0.1 second
                    dopTime++;
                }
                catch (InterruptedException ie) {
                    System.out.println("Caught unexpected " +"InterruptedException, ignoring...");
                }

            }

        } 
        mainPool.writeAllIndexed();
        for(Thread thread: allThreads){
            try{
                thread.interrupt();
            }
            catch(Exception e){
                System.out.println("Caught unexpected " + "InterruptedException, ignoring...");
            }
        }
        Date otherDate = new Date();
        long endTime = otherDate.getTime() / 1000;
        String timer = Long.toString(endTime - startTime);
        System.out.println("Время работы = " + timer + " секунды");


    }

}