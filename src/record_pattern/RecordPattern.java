package record_pattern;

/**
 * **Record Pattern Matching in JDK 21**
 *
 * <p>
 *   This feature enhances Java with **record patterns**, allowing for more sophisticated data
 *   navigation and processing when working with record classes.
 * </p>
 *
 * <p>
 *   **Key Enhancements:**
 *   <ul>
 *     <li>**Record Destructuring:** Enables direct extraction of record components (fields) within
 *         pattern matching constructs like `instanceof` and `switch`. </li>
 *     <li>**Nested Patterns:** Supports pattern matching on records nested within other records,
 *         facilitating composable data queries.</li>
 *   </ul>
 * </p>
 *
 * <p>
 *   **How it Works:**
 *   <ol>
 *     <li>**Type Check:** The pattern matching mechanism first verifies if the object is an instance
 *         of the specified record type.</li>
 *     <li>**Destructuring (if type check passes):** The record is deconstructed, and its components
 *         are assigned to corresponding variables within the pattern.</li>
 *   </ol>
 * </p>
 *
 * @see <a href="https://openjdk.org/jeps/440">JEP 440: Record Patterns</a> for detailed information.
 */

public class RecordPattern {

    static final Object a = "Suraj";
    static final Object person = new Person("Suraj" , 25);
    // nested record
    static final Object parent = new Parent(new Child("2222") , "1111");
    static final Object pair = new Pair("Pair1" , "Pair2");


    public static void main(String[] args) {

        // Before JDK-21
        usingInstanceofBeforeToJDK16(a);

        // As of JDK-16
        usingInstanceofWithJDK16(a);

        /**
         * Now lets move on to the Record Pattern matching feature.
         * This feature will not work with traditional java classes but only with Records in java.
         * @see Record
         */

        // Before the Record_Pattern feature was available
        usingPatternMatchingBeforeJDK21(person);

        //  With JDK-21 Record_Pattern feature
        usingPatternMatchingWithJDK21(person);

        // Using Record_Pattern feature with Nested Records
        usingPatternMatchingWithNestedRecords(parent);

        // Checking Record Pattern with diff type
        checkNestedPatternHavingDiffComponentType(pair);

    }

    /**
     * Demonstrates a scenario where a type check fails during record pattern matching.
     *
     * <p>
     * In this example, the <code>pair</code> object has both its components as <code>String</code> values.
     * However, the pattern matching attempts to deconstruct it as if it contained an <code>Integer</code>
     * leading to a type mismatch and failure.
     * </p>
     *
     * <p>
     * The recommended approach to avoid such issues is to use the <code>var</code> keyword.
     * This allows the compiler to infer the correct types at runtime,
     * ensuring flexibility and preventing type check failures.
     * </p>
     *
     * @param pair The record object to be type-checked and deconstructed.
     * @see #pair  // Reference to the `pair` object declaration.
     */

     private static void checkNestedPatternHavingDiffComponentType(Object pair){
        // The issue with diff type can be solved by using var and let compiler do the type check
        if(pair instanceof Pair(Integer s1 , String s2)){
               System.out.println(s1 + " " + s2);
        }else {
            System.out.println("Pattern matching failed!");
        }

        // Using var keyword for safe type inference
        if(pair instanceof Pair(var s1 , var s2)){
            System.out.println(s1 + " " + s2);
        }else {
            System.out.println("Pattern matching failed!");
        }
    }


    /**
     * Demonstrates nested record pattern matching.
     *
     * <p>
     *   This example showcases how to deconstruct a nested record structure.
     *   After the type check passes , the <code>parent</code> object is first destructured to extract its <code>Child</code>
     *   and <code>parentId</code> components. Subsequently, the <code>Child</code> component itself is further destructured
     *   to retrieve its <code>childId</code>.
     * </p>
     *
     * <p>
     *   **Important:** Type checking is performed at each level of the nested destructuring process.
     *   If a type mismatch occurs at any point, the entire pattern matching condition fails.
     * </p>
     *
     * @param parent The parent record object to be deconstructed.
     */

    private static void usingPatternMatchingWithNestedRecords(Object parent) {
          if(parent instanceof Parent(Child(String childId) , String parentId)){
                System.out.println("Child Id : " + childId);
                System.out.println("Parent Id : " + parentId);
          }
    }


    /**
     * Demonstrates traditional record handling without pattern matching.
     *
     * <p>
     *   This approach necessitates an explicit type check using <code>instanceof</code> to ensure
     *   compatibility with the <code>Person</code> record. The <code>name</code> and <code>age</code> components are then retrieved
     *   by invoking their respective accessor methods. An additional age validation check is performed.
     * </p>
     *
     * @see Person  // For details on the <code>Person</code> record structure and accessor methods.
     */

    private static void usingPatternMatchingBeforeJDK21(Object person) {

        if(person instanceof Person p){
            String name = p.name();
            int age = p.age();
            if(age > 18){
                System.out.println(name + " is Adult");
            }
        }
    }


    /**
     * Demonstrates record pattern matching with <code>instanceof</code>
     *
     * <p>
     *   This example showcases how to concisely deconstruct a record instance and extract its components
     *   within a single <code>if</code>  statement. The <code>person</code> object's <code>name</code> and <code>age</code>  components are directly
     *   initialized by calling the corresponding accessor methods during pattern matching.
     * </p>
     */

    private static void usingPatternMatchingWithJDK21(Object person) {
        if(person instanceof Person(var name , var age) && age > 18){
            System.out.println(name + " is Adult");
        }
    }

    /**
     * Demonstrates type-safe pattern matching with enhanced <code>instanceof</code>.
     *
     * <p>
     * In this example, if the parameter <code>a</code> is of type <code>String</code>, its value is directly extracted and assigned to
     * the pattern variable <code>b</code>. This eliminates the need for explicit type casting, resulting in cleaner and
     * more concise code.
     * </p>
     *
     * <p>
     * This approach represents an improvement over traditional <code>instanceof</code> checks
     * as seen in {@link #usingInstanceofBeforeToJDK16(Object)}.
     * </p>
     *
     * @param a The object to be type-checked and potentially cast to a String.
     */

    private static void usingInstanceofWithJDK16(Object a) {
         if(a instanceof String b){
             System.out.println(b);
         }
    }

    /**
     * Demonstrates traditional type checking and explicit casting.
     *
     * <p>
     * In this scenario, if the parameter <code>a</code> is confirmed to be of type <code>String</code>,
     * an explicit type cast is required to convert it to a <code>String</code>  variable for further use.
     * </p>
     *
     * @param a The object to be type-checked and potentially cast to a String.
     */
    private static void usingInstanceofBeforeToJDK16(Object a) {
        if(a instanceof String){
            String data = (String) a;
            System.out.println(data);
        }
    }

}
