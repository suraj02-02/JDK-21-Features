package pattern_matching_with_switch;

import java.math.BigDecimal;

/**
 * <p>
 *  <b> ** Pattern Matching With Switch Case **** </b>
 * </p>
 *  <p>
 *    This is just an extension to record pattern matching which allows an expression to
 *    be checked against number of diff patterns. Earlier the switch case were meant to compare / match the
 *    value of constants or enums only.
 *  </p>
 *  <p>
 *    As of JDK-21 the capability of switch case got enhanced to handle the matching based on types as well.
 *    So , now at runtime the type of object is analysed and based on that the matching case is executed.
 *  </p>
 *
 *  @see <a href="https://openjdk.org/jeps/441">JEP 441: Record Pattern Matching With Switch Statements </a> for detailed information.
 *
 */


public class PatternMatchingWithSwitch {

    private static final Shape shape = new Circle();
    private static final Object object = 10;
    private static final Routers router = new Rdk(1 , "This is an RDK router");

    public static void main(String[] args) {

        // prior to JDK-21
        multiplePatternsMatchingBeforeJDK21(shape);

        // As of JDK-21
        multiplePatternMatchingUsingJDK21SwitchCase(shape);
        identifyWrapperType(object);
        pattenMatchingWithRecords(router);

    }

    /**
     * <p>
     *  Below is also an implementation where we are using the benefits of
     *  pattern matching in switch statement.
     * </p>
     * <p>
     *     Here we have used Records instead of traditional classes to get benefits of <b> record destructuring </b>.
     * </p>
     * <p>
     *   Java has introduced a new keyword <code>when</code> which is specifically meant to be used
     *   inside switch statements.It provides an extra conditions for a single case.
     * </p>
     *
     * @param router
     */
    private static void pattenMatchingWithRecords(Routers router) {

        switch (router){
            case Hdm(int id , String name) when id == 0 -> System.out.println(name);
            case Rdk(int id , String name) when id > 0 -> System.out.println(name);
            case Rhino(int id , String name) when id < 0 -> System.out.println(name);
            default -> throw new IllegalStateException("Unexpected value: " + router);
        }
    }

    /**
     * Identifies the wrapper type of an object and prints its value.
     *
     * <p>
     * This method utilizes pattern matching in a <code>switch</code> statement to efficiently determine the specific wrapper type of
     * the provided <code>object</code>. It handles the following wrapper types:
     * </p>
     *
     * <p>
     * If the object is of one of these recognized types, its value is extracted and printed along with the corresponding type
     * information. For any other object type, an `IllegalStateException` is thrown indicating an unexpected value.
     * </p>
     *
     * @param object The object whose wrapper type needs to be identified.
     * @throws IllegalStateException If the `object` is not one of the supported wrapper types.
     */

    private static void identifyWrapperType(Object object){

        switch (object){
            case Integer i -> System.out.println("Integer type with value : " + i);
            case Double d -> System.out.println("Double type with value : " + d);
            case Float f -> System.out.println("Float type with value : " + f);
            case BigDecimal bd -> System.out.println("BigDecimal type with value : " + bd);
            case String st -> System.out.println("String type with value : " + st);
            case CharSequence cs -> System.out.println("CharSequence type with value : " + cs); // try moving this above String case
            default -> throw new IllegalStateException("Unexpected value: " + object);
        }
    }

    /**
     * <p>
     *   <b> Below is another example of how pattern matching is applied over switch statement. </b>
     * </p>
     *
     * <p>
     *   Here the super type <code>Shape</code> is matched with the exact implementation and on
     *   successful matching the <code>shape</code> object is type cast to its specific
     *   implementation type along with a pattern variable to fetch further info from object.
     * </p>
     * <p>
     *    Below implementation makes the code much concise and clean<
     * </p>
     *
     * @param shape
     */

    private static void multiplePatternMatchingUsingJDK21SwitchCase(Shape shape) {
        switch (shape) {
            case Triangle t -> System.out.println("This shape is of type : Triangle");
            case Square s -> System.out.println("This shape is of type : Square");
            case Circle c -> System.out.println("This shape is of type : Circle");
            case null, default -> System.out.println("No shape type found!");
        }
    }


    /**
     * <p>
     *   Below implementation is the most common way to check the specific implementation type before we had Pattern matching
     *   in switch statements. Multiple if else blocks makes the code verbose.
     * </p>
     *
     * <p>
     *  <b> Pattern matching provides benefits : </b>
     *     <ol>Less verbosity in code</ol>
     *     <ol>No explicit type casting required</ol>
     * </p>
     *
     * @param shape
     */
    private static void multiplePatternsMatchingBeforeJDK21(Shape shape) {

        if(shape instanceof Triangle t){
            System.out.println("This shape is of type : Triangle");
        }
        else if (shape instanceof Circle){
            Circle c = (Circle) shape; // explicit type casting , traditional way
            System.out.println("This shape is of type : Circle");
        }
        else if (shape instanceof Square){
            System.out.println("This shape is of type : Square");
        }
        else {
            System.out.println("No shape type found!");
        }
    }

}
