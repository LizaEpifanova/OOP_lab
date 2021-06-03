import java.util.Scanner; //импортируем пакет
public class Palindrome {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in); // Объявляем Scanner
        String s = in.nextLine(); // читаем строку из консоли
        if (isPalindrome(s)){
            System.out.println("Слово является палиндромом.");
        } else{
            System.out.println("Слово не является палиндромом.");
        }
       // System.out.println(reverseString(s));
        in.close();
    }
    //метод, который переворачивает строку
    public static String reverseString(String Strings){
        String reverseString = "";
        for (int i=Strings.length()-1;i>-1;i--){
            reverseString = reverseString + Strings.charAt(i); 
        }
        return reverseString;
    }
    //метод, который проверяет, является ли строка палендромом
    public static boolean isPalindrome(String s){
        boolean isPalindrome = false; 
        String news = reverseString(s);
        if (s.equals(news)) {
            isPalindrome=true;
        }
        return isPalindrome;
    }

}
