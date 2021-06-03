import java.util.*;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;




public class Crawler {
    public static final String protocol = "https://";
    public static final String URL = "href=\"";
    public static final String END_URL = "\"";
    Date currentDate;
    

    public static void main(String[] args) throws IOException {

        Scanner scaner = new Scanner(System.in);
        System.out.println("Хост:");
        String firstUrl = scaner.nextLine();
        System.out.println("глубина поиска:");
        int maxDepth = 0;
        try {
            maxDepth = Integer.parseInt(scaner.nextLine());
            scaner.close();

        } catch (Exception e) {
            scaner.close();
            writeMessage("Ошибка. Глубина поиска введена неверно.");
            return;
        }
        if (maxDepth <= 0) {
            writeMessage("Ошибка. Глубина поиска введена неверно.");
            return;
        }

        LinkedList<URLDepthPair> myList_1 = new LinkedList<URLDepthPair>();
        LinkedList<URLDepthPair> myList_2 = new LinkedList<URLDepthPair>();
      
        String sUrl = protocol + firstUrl;

        URLDepthPair pairF = new URLDepthPair(sUrl, 0);
        Socket socket = new Socket();
        try{
            
            socket = new Socket(firstUrl, 443);
            socket.setSoTimeout(2000);
            PrintWriter myWriter = new PrintWriter(socket.getOutputStream(), true); //просим сайт отправить нам код
            myWriter.println("GET " + pairF.getUrl() + " HTTP/1.1");
            myWriter.println("Host: " + pairF.getWebHost());
            myWriter.println();
        }
        catch (Exception e){
        }

        LinkedList<String> urls = new LinkedList<String>();

        Crawler c = new Crawler();
        long startTime = c.startTimer(firstUrl);
        URLDepthPair urlDepthPair = new URLDepthPair(firstUrl, 0);
        urls.add(firstUrl); 
        myList_2.add(urlDepthPair); 
        while (myList_2.size() != 0) {
            URLDepthPair currentPair = myList_2.pop();
            myList_2.remove(currentPair);
            if (myList_1.contains(currentPair)) { //проверяет совпадение
                continue;
            }
            if (currentPair.getParseDeep() == maxDepth) {
                continue;
            }

            myList_1.add(currentPair);
            writeMessage("Глубина поиска " + currentPair.toString() + "хост был проиндексирован, его номер: " + myList_1.indexOf(currentPair) + "/" + Integer.toString(myList_1.size() + myList_2.size()));

            LinkedList<URLDepthPair> newURLDepthPairs = research(currentPair, urls);
            if (!(newURLDepthPairs == null)) {
                for (int i = 0; i < newURLDepthPairs.size(); i++) {
                    URLDepthPair pair = newURLDepthPairs.get(i);
                    if (!myList_2.contains(pair))
                        myList_2.add(pair);
                }
            }
        }
                
        socket.close();
        for (int i = 0; i < myList_1.size(); i++) {
            writeMessage(myList_1.get(i).getUrl());
        }
        writeMessage("Количество вех пар URl: " + Integer.toString(myList_1.size()));
        Date otherDate = new Date();
        long endTime = otherDate.getTime() / 1000;
        String timer = Long.toString(endTime - startTime);
        System.out.println("Время работы = " + timer + " секунды");
        
    }

    public static void writeMessage(String str) {
        System.out.println(str);
    }

    public static LinkedList<URLDepthPair> research(URLDepthPair currentPair, LinkedList<String> urls) {
        LinkedList<URLDepthPair> newURLDepthPairs = new LinkedList<URLDepthPair>();
        URLConnection connection;
        try {
            connection = new URL(currentPair.getUrl()).openConnection();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return newURLDepthPairs;
        }
        Scanner scanner;
        try {
            scanner = new Scanner(connection.getInputStream());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return newURLDepthPairs;
        }
        
            scanner.useDelimiter("\\Z");
            String line = "";
            if (scanner.hasNext()){
                line = scanner.next();
            }
            scanner.close();
            int startIndex = 0;
            int endIndex = 0;
            while (true){
                startIndex = line.indexOf(URL, endIndex);
                endIndex = line.indexOf(END_URL, startIndex + URL.length());
                if (startIndex == -1 || endIndex == -1){
                    break;
                }
                if (endIndex - startIndex > 200){
                    String temp = line.substring(startIndex, startIndex+URL.length());
                    line.replaceFirst(temp, "");
                    continue;
                }
                String newLink = line.substring(startIndex + 6, endIndex);
                if (!newLink.contains(protocol)){
                    continue;
                }
                boolean isBadLink = false;
                for (Object urlEnd: WRONG_URL_ENDS){
                    if (newLink.substring(newLink.length() - 4, newLink.length()).equals(urlEnd)){
                        isBadLink = true;
                        break;
                    }
                    
                }
                if (isBadLink){
                    continue;
                }
                if (urls.contains(newLink)){
                    continue;
                }
                urls.add(newLink);
                URLDepthPair newURLDepthPair = new URLDepthPair(newLink, currentPair.getParseDeep() + 1);
                newURLDepthPairs.add(newURLDepthPair);
            }
            return newURLDepthPairs;
            
        
    }

    public long startTimer(String firstUrl){
        currentDate = new Date();
        long startTime = currentDate.getTime() / 1000;
        return startTime;
    }
    public static final String[] WRONG_URL_ENDS = {".apk", ".ogv"};
}