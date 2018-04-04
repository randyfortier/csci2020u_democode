package demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class BasicTest {
  @Test
  public void tryItOut() {
    assertEquals(2 + 3, 5, "2 + 3 should be 5!");
  }

  @Test
  @DisplayName("Test that always fails")
  public void alwaysFails() {
    assertNotNull(null, "This is not supposed to happen!  Up is down, down is up!");
  }

  @Test
  @DisplayName("Another test that always fails")
  public void alwaysFails2() {
    fail("This test should not happen!  Probability = 0");
  }
}
