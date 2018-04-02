package demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class BasicTest {
  @Test
  public void tryItOut() {
    assertEquals(2 + 3, 5, "2 + 3 should be 5!");
  }
}
