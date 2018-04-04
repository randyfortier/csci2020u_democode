package demo;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.Duration.ofMillis;

import org.junit.jupiter.api.*;

import java.io.IOException;

public class DemoTests {
  private String returnsNull() { return null; }
  private String doesntReturnNull() { return "abc"; }

  @Test
  public void testFPEquality() {
    assertEquals(3.141592, Math.PI, 0.0001, "Pi is not accurate enough");
  }

  @Test
  public void testForNull() {
    assertNull(returnsNull(), "returnsNotNull() should return null");
    assertNotNull(doesntReturnNull(), "doesntReturnNull() should not return null");
  }

  @Test
  public void testExceptions() {
    Throwable exception = assertThrows(IOException.class, () -> {
      throw new IOException("Unable to open file");
    });
    assertEquals(exception.getMessage(), "Unable to open file", "Exception message invalid");
  }

  @Test
  @Tag("slow")
  public void takesTooLong() {
    assertTimeout(ofMillis(100), () -> {
      Thread.sleep(2000);
    }, "This code took too long to complete");
  }

  @Test
  @Tag("slow")
  public void takesTooLongInterrupt() {
    assertTimeoutPreemptively(ofMillis(100), () -> {
      Thread.sleep(10000);
    }, "This code took too long to complete, but was interrupted");
  }










}
