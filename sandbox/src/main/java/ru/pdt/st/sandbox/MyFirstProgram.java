package ru.pdt.st.sandbox;

public class MyFirstProgram {
  
  public static void main(String[] args) {
    hello("world");
    hello("user");
    hello("Alexei");

    Square s = new Square(5);
    System.out.println("Площадь квадрата со стороной " + s.l + " = " + s.area());

    Rectangle r = new Rectangle(4, 6);
    System.out.println("Площадь прямоугольника со сторонами " + r.a + " и " + r.b + " = " + r.area());

    Segment d = new Segment(5, 5, 5, 10);
    System.out.println("Расстояние между двумя точками с координатами C(" + d.x1 + "; " + d.y1
            + ") и D(" + d.x2 + "; " + d.y2 + ") = " + d.calculateLengthSegment());
  }

  public static void hello(String somebody) {
    System.out.println("Hello, " + somebody + "!");
  }

}