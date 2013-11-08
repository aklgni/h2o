package hex;

import org.junit.BeforeClass;
import org.junit.Test;
import water.Key;
import water.TestUtil;
import water.UKV;
import water.fvec.Frame;
import water.fvec.NFSFileVec;
import water.fvec.Vec;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class Summary2Test extends TestUtil {
  @BeforeClass
  public static void stall() { stall_till_cloudsize(1); }

  @Test public void testConstColumn() {
    Key key = Key.make("testConst.hex");
    Frame fr = parseFrame(key, "./smalldata/constantColumn.csv");

    Vec vec = fr.vecs()[0];
    Summary2 s = new Summary2(vec, "");
    s.add(vec.chunk(0));
    for (int i = 1; i < vec.nChunks(); i++) {
      Summary2 s1 = new Summary2(vec, ""); s1.add(vec.chunk(i)); s.add(s1);
    }
    s.finishUp();
    assertEquals(1, s.bin_cnts.length);
    assertEquals(528, s.bin_cnts[0]);
    assertEquals(0.1, s.bin_cnts[0], 0.00001);
    for (double pv : s._percentiles)
      assertEquals(0.1, pv, 0.00001);

    UKV.remove(key);
  }

  @Test public void testEnumColumn() {
    Key key = Key.make("cars.hex");
    Frame fr = parseFrame(key, "./smalldata/cars.csv");
    Vec vec = fr.vecs()[fr.find("name")];
    Summary2 s = new Summary2(vec, "");
    s.add(vec.chunk(0));
    for( int i = 1; i < vec.nChunks(); i++ )
      { Summary2 s1 = new Summary2(vec, ""); s1.add(vec.chunk(i)); s.add(s1); }
    s.toString();

    assertEquals(306, s.bin_cnts.length);
    UKV.remove(key);
  }

  @Test public void testIntColumn() {
    Key key = Key.make("cars.hex");
    Frame fr = parseFrame(key, "./smalldata/cars.csv");
    Vec vec = fr.vecs()[fr.find("cylinders")];
    Summary2 s = new Summary2(vec, "");
    s.add(vec.chunk(0));
    for( int i = 1; i < vec.nChunks(); i++ )
      { Summary2 s1 = new Summary2(vec, ""); s1.add(vec.chunk(i)); s.add(s1); }
    s.toString();

    assertEquals(0, s.bin_cnts[4]); // no 7 cylinder cars
    assertEquals(4, (int)s._percentiles[0]);
    assertEquals(8, (int)s._percentiles[s._percentiles.length - 1]);
    UKV.remove(key);
  }
}

