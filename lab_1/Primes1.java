public class Primes1 {
    public static void main(String[] args) {
        for (int i =2; i<100; i++){
            if (isPrime(i)){
                System.out.println(i + " является простым");
            }
        }
    } 
    // метод, который проверяет является ли число простым
    public static boolean isPrime(int n){
        boolean isPrime = true;
        for (int i= 2; i<n;i++){
            if (n % i==0){
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }
}
