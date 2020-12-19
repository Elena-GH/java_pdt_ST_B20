package ru.pdt.st.soap;

import com.lavasoft.*;
import com.lavasoft.GeoIPServiceHttpGet;
import com.lavasoft.GeoIPServiceSoap;
import org.testng.annotations.Test;

import static org.junit.Assert.assertTrue;

public class GeoIPServiceTests {

  @Test
  public void testMyIPPositive() {
    String geoIP = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("64.18.87.4");
    assertTrue(geoIP.contains("CA"));
  }

  @Test
  public void testMyIPNegative() {
    String geoIP = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("64.18.87.4");
    assertTrue(geoIP.contains("RU"));
  }

}
