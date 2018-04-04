package validation;

public class Validator {
  public boolean validatePhone(String toCheck) {
    String pattern = "([0-9]{3}(-)?)?[0-9]{3}(-)?[0-9]{4}";
    return toCheck.matches(pattern);
  }

  public static void main(String[] args) {
    Validator validator = new Validator();
    String phone1 = "905-721-8668";
    System.out.println("Validating " + phone1 + ": " +
                       validator.validatePhone(phone1));
   String phone2 = "721-8668";
   System.out.println("Validating " + phone2 + ": " +
                      validator.validatePhone(phone2));
  }
}
