package ru.pdt.st.sandbox;

public class Segment {

  double x1;
  double y1;
  double x2;
  double y2;

  public Segment(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public double calculateLengthSegment() {
    return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
  }
}
