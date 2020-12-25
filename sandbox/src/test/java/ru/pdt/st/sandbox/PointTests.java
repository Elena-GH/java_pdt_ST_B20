package ru.pdt.st.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

  @Test
  public void testDistance() {
    Point P1 = new Point(1, 2);
    Point P2 = new Point(3, 4);
    Assert.assertEquals(P2.distance(P1), Math.sqrt(8) );
  }

  @Test
  public void testDistancePositive() {
    Point P1 = new Point(1, 2);
    Point P2 = new Point(3, 4);
    Assert.assertEquals(P2.x - P1.x, 2 );
  }

  @Test (enabled = false)
  public void testDdistanceNegative() {
    Point P1 = new Point(1, 2);
    Point P2 = new Point(3, 4);
    Assert.assertEquals(P2.x - P1.x, 20 );
  }

}
