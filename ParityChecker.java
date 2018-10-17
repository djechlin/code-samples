/** Takes a string of numeric words in a configured language and calculates
 * whether the sum and product of the numbers is even or odd. String must
 * fit in memory and the coded numbers must fit in the Integer type. Note
 * these are not serious restrictions: this algorithm may be streamed and the
 * integer inputs may be reduced to their even-or-odd parity when input.
 * 
 * This class is made to demonstrate Daniel Echlin's proficiency and coding
 * style in modern Java. It specifically highlights:
 *   1. Use of immutables, specifically from the Guava library
 *   2. Use of dependency injection
 *   3. Use of Java 8 functional methods
 *   4. Unit testing
 *   5. Javadoc documentation
 * 
 * This class is incomplete in these ways;
 *   1. No main and tests not executed
 *   2. Imports are missing
 *   3. May be formatting mistakes in Javadoc and source
 *   4. For brevity only product is tested, not sum.
 *   5. Config, test, and source are separated in production.
 *   6. Tests use raw asserts, not expectqtions or fluent truth
 */
public class ParityChecker {
    private final Map<String, Integer> wordToValueMap;

    private static final Map<String, Integer> ENGLISH_MAP = ImmutableMap.builder()
    .add("one", 1)
    .add("two", 2)
    .add("three", 3)
    .add("four", 4)
    .add("five", 5)
    .add("six", 6)
    .add("seven", 7)
    .add("eight", 8)
    .add("nine", 9)
    .add("ten", 10)
    .build();
   
   private static final Map<String, Integer> SPANISH_MAP = ImmutableMap.builder()
   .add("uno", 1)
   .add("dos", 2)
   .add("tres", 3)
   .add("cuatro", 4)
   .add("cinco", 5)
   .add("seis", 6)
   .add("siete", 7)
   .add("ocho", 8)
   .add("nueve", 9)
   .add("diez", 10)
   .build();
   
   /**
    * Exception class for ParityChecker.
    */
   public static final class ParityCheckerException extends Exception {
       ParityCheckerException(String msg) { super(msg); }
   }
    
   public ParityChecker(ImmutableMap<String, Intetger> wordToValueMap) {
       this.wordToValueMap = wordToValueMap;
   }

  /**
    * @param input - whitespace-delimited input stream of words that represent
    * numeric values.
    * @return - true if the sum of the input numbers is even, false otherwise.
    * @throws ParityCheckerException - if one of the tokens is not a recognized
    * number.
    */
   public bool sumIsEven(String input) {
       // Count the number of odd elements, and check whether it is even.
       return toNumbers(input).filter(num -> num % 2 == 1).count() %2 == 0;
   }
   
   /**
    * @param input - whitespace-delimited input stream of words that represent
    * numeric values.
    * @return - true if the product of the input numbers is even, false
    * otherwise.
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
       if (wordToValueMap.contains(token)) {
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
       ParityChecker checker = new ParityChecker(ImmutableMap.of("one", 1, "two", 2, "three", 3));
       assert !checker.productIsEven("one three one three");
   }
   
   public void test__product_oneEvenIsOdd() {
       ParityChecker checker = new ParityChecker(ImmutableMap.of("one", 1, "two", 2, "three", 3));
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
       assert checker.productIsOdd("one     one  ");
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
