package Lab_2;

/**
* двумерный класс точки.
**/
public class Point2d {
    private double xCoord; // координата X 
    private double yCoord; // координата Y 
    //Конструктор инициализации с двумя параметрами
    public Point2d ( double x, double y) {
    xCoord = x;
    yCoord = y;
    }
    //Конструктор по умолчанию. 
    public Point2d () {
        this(0, 0);
    }
    //Возвращение координаты X *
    public double getX () {
    return xCoord;
    }
    // Возвращение координаты Y 
    public double getY () {
    return yCoord;
    }
    //Установка значения координаты X. 
    public void setX ( double val) {
    xCoord = val;
    }
    //Установка значения координаты Y. 
    public void setY ( double val) {
    yCoord = val;
    }
    }
    
