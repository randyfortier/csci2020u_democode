package validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

//import validation.Validator;

@Tag("important")
public class ValidatorTest {
  private static Validator validator = null;

  @BeforeAll
  public static void setUp() {
    validator = new Validator();
  }

  @AfterAll
  public static void tearDown() {
    validator = null;
  }

  @Test
  @DisplayName("Testing phone validator")
  public void testPhoneValidation(TestInfo testInfo) {
    assertEquals(validator.validatePhone("905-721-8668"), true, "905-721-8668 is a valid phone number");
    assertTrue(validator.validatePhone("721-8668"), "721-8668 is a valid phone number");
    assertFalse(validator.validatePhone("(905)721-8668"), "(905)721-8668 is not a valid phone number");
    assertTrue(validator.validatePhone("9057218668"), "9057218668 is a valid phone number");
  }





}
