/** Takes a string of numeric words in a configured language and calculates
 * whether the sum and product of the numbers is even or odd. This demonstrates
 * use of Java immutables, dependency injection, Javadoc, and shows a unit test
 * suite.
 */
public class ParityChecker {
    private final Map<String, Integer> wordToValueMap;

    private static final Map<String, Integer> ENGLISH_MAP = ImmutableMap.of(
        "one", 1,
        "two", 2,
        "three", 3,
        "four", 4,
        "five", 5,
        "six", 6,
        "seven", 7,
        "eight", 8,
        "nine", 9,
        "ten", 10);
   
   private static final Map<String, Integer> SPANISH_MAP = ImmutableMap.of(
       "uno", 1,
       "dos", 2,
       "tres", 3,
       "cuatro", 4,
       "cinco", 5,
       "seis", 6,
       "siete", 7,
       "ocho", 8,
       "nueve", 9,
       "diez", 10);
   
   /**
    * Exception class for ParityChecker.
    */
   public static final class ParityCheckerException extends Exception {
       ParityCheckerException(String msg) { super(msg); }
   }
    
   public ParityChecker(ImmutableMap<String, Integer> wordToValueMap) {
       this.wordToValueMap = wordToValueMap;
   }

  /**
    * @param input - whitespace-delimited input stream of words that represent
    * numeric values
    * @return - true if the sum of the input numbers is even
    * @throws ParityCheckerException - if one of the tokens is not a recognized
    * number
    */
   public bool sumIsEven(String input) {
       int oddElementsCount = toNumbers(input).filter(num -> num % 2 == 1).count();
       return oddElementsCount % 2 == 0;
   }
   
   /**
    * @param input - whitespace-delimited input stream of words that represent
    * numeric values.
    * @return - true if the product of the input numbers is even, false
    * otherwise. An empty string is considered odd, by mathematical convention
    * that it represents 1.
    * @throws ParityCheckerException - if one of the tokens is not a recognized
    * number.
    */
   public bool productIsEven(String input) {
       // Check whether any number is even.
       return toNumbers(input).any(num -> num % 2 == 0);
   }
    /**
    * Validates that token is in the word map.
    * @return - the input, unmodified, if it is validated
    * @throws ParityCheckerException - if the input token is not a recognized
    * number.
    */
   private String validateToken(String token) {
       if (!wordToValueMap.contains(token)) {
           throw new ParityCheckerException(String.format("%s is not recognized", token));
       }
       return token;
   }
   
   /**
    * Splits input on whitespace and converts to a stream of numbers.
    * @throws ParityCheckerException if one of the tokens fails to validate.
    */
   private Stream<Integer> toNumbers(String input) {
       return input.split("\\s+")
       .stream()
       .map(ParityChecker::validateToken)
       .map(wordToValueMap::get);
   }
   
   public void test__product_allOddIsOdd() {
       ParityChecker checker = new ParityChecker(
           ImmutableMap.of("one", 1, "two", 2, "three", 3));
       assert !checker.productIsEven("one three one three");
   }
   
   public void test__product_oneEvenIsOdd() {
       ParityChecker checker = new ParityChecker(
           ImmutableMap.of("one", 1, "two", 2, "three", 3));
       assert checker.productIsEven("one three three one two");
   }
   
   public void test__product_emptyStringIsOdd() {
       ParityChecker checker = new ParityChecker(ImmutableMap.of());
       assert checker.productIsOdd("");
   }

   public void test__product_whitespaceOnlyStringIsOdd() {
       ParityChecker checker = new ParityChecker(ImmutableMap.of());
       assert checker.productIsOdd("\t\t\t");
   }
   
   public void test__product_extraWhitespaceIgnored() {
       ParityChecker checker = new ParityChecker(ImmutableMap.of("one", 1));
       assert checker.productIsOdd(" one     three  ");
   }
   
   public void test__product_exceptionOnUnrecognizedWord() {
       ParityChecker checker = new ParityChecker(ImmutableMap.of("one", 1));
       // JUnit supports this with @expectThrows and other options.
       bool caught = false;
       try {
           checker.productIsOdd("one fish two fish");
       } catch (ParityCheckerException e) {
           caught = true;
       }
       assert caught;
   }
}
