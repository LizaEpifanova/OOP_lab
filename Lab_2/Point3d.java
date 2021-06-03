package Lab_2;


public class Point3d extends Point2d {
    private double zCoord; //координата z
    public Point3d(double x, double y, double z){ //конструктор с тремя параметрами
        super(x,y); //вызываем конструктор базового класса
        zCoord=z;
    }
    //Конструктор по умолчанию. 
    public Point3d () {
        this(0.0, 0.0, 0.0);
    }
    public double getZ () {  // Возвращение координаты z
        return zCoord;
        } 
    public void setZ ( double val) { //Установка значения координаты z
        zCoord = val;
        }
    public boolean equalii(Point3d a, Point3d b){
        return (a.getX()==b.getX())&&(a.getY()==b.getY())&&(a.getZ()==b.getZ());
    }
    public double distanceTo(Point3d a){
        if (equalii(a, this)==false){
            double X = Math.pow(this.getX() - a.getX(), 2.0);
            double Y = Math.pow(this.getY() - a.getY(), 2.0);
            double Z = Math.pow(this.getZ() - a.getZ(), 2.0);
            double dis = Math.sqrt(X + Y+ Z);
            dis = Math.round(dis*100)/100;
            return dis;
        }
        return 0.0;
    }
}
