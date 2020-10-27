package ru.pdt.st.sandbox;

public class CalculateDistance {

  public static void main(String[] args) {

    Point p1 = new Point(5, 5);
    Point p2 = new Point(5, 10);
    System.out.println("Расстояние между двумя точками с координатами A(" + p1.x + "; " + p1.y
            + ") и B(" + p2.x + "; " + p2.y + ") = " + distance(p1, p2));
  }

  public static double distance(Point p1, Point p2) {
    return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
  }

}


