public class Rectangle {
    private double width;
    private double height;

    public Rectangle() {
        this.width = 1;
        this.height = 1;
    }

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    //Method to calculate the area
    public double getArea() {
        return width * height;
    }

    //Method to calculate perimeter
    public double getPerimeter() {
        return 2 * (width + height);
    }
}
