package Lab_2;
import java.util.Scanner;

public class Lab1 {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("координаты первой точки: ");
        double x1 = in.nextInt();
        double y1 =in.nextInt();
        double z1 =in.nextInt();
        System.out.println("координаты второй точки: ");
        double x2 =in.nextInt();
        double y2 =in.nextInt();
        double z2 =in.nextInt();
        System.out.println("координаты третьей точки: ");
        double x3 =in.nextInt();
        double y3 =in.nextInt();
        double z3 =in.nextInt();
        in.close();
        Point3d point1= new Point3d(x1, y1, z1);
        Point3d point2= new Point3d(x2, y2, z2);
        Point3d point3= new Point3d(x3, y3, z3);
        if ((checkPoints(point1, point2, point3))){
        System.out.println(computeArea(point1, point2, point3));
        } else {
            System.out.println("Точки имеют одинаковые координаты.");
        }

    }
    public static double computeArea(Point3d a,Point3d b,Point3d c){
        double dist_a = a.distanceTo(b); // вычисление стороны ab
        double dist_b = b.distanceTo(c); // вычисление стороны bc
        double dist_c = c.distanceTo(a); // вычисление стороны ac
        double p=((dist_a+dist_b+dist_c)/2); //полупериметр
        double S = Math.sqrt(p*(p-dist_a)*(p-dist_b)*(p-dist_c));
        return Math.round(S*100)/100D;

    }
    public static boolean checkPoints(Point3d a, Point3d b, Point3d c) //Проверка на совпадение точек
    {
        if (((a.getX() == b.getX()) && (a.getY() == b.getY()) && (a.getZ() == b.getZ())) ||
        ((b.getX() == c.getX()) && (b.getY() == c.getY()) && (b.getZ() == c.getZ())) ||
        ((a.getX() == c.getX()) && (a.getY() == c.getY()) && (a.getZ() == c.getZ())))
        return false;
        else 
        return true;
    }


}
