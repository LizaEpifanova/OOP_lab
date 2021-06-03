public class Location {
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }
    @Override
public boolean equals(Object obj) {

    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;  
    Location one = (Location) obj;
    //проверяем у координату
    if (yCoord != one.yCoord) return false;
    //проверяем х координату
    return xCoord==one.xCoord;
    
    }
    
    @Override
    //метод hashCode() возвращает числовой код объекта
    public int hashCode(){

        int result= 17;

        result = 31 * result + (int)xCoord;
        result = 31 * result + (int)yCoord;
        return result;
    
    }
}
