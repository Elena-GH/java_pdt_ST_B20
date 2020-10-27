package ru.pdt.st.sandbox;

public class CalculateDistance {

  public static void main(String[] args) {

    Point p1 = new Point(1, 2);
    Point p2 = new Point(3, 4);
    System.out.println("Расстояние между двумя точками с координатами A(" + p1.x + "; " + p1.y
            + ") и B(" + p2.x + "; " + p2.y + ") = " + p2.distance(p1.x, p1.y));
    }

}


