import java.util.*;
import java.util.function.Function;
//Daniel Fedorin's file.
public class Utils {

    /**
     * Common Scanner used in all string inputs.
     * Opening a new one every time is just not worth it.
     */
    private static Scanner stdin = new Scanner(System.in);

    /**
     * A pair class, combining together two arbitrary types of elements
     * so that they may be more easily returned from functions.
     * @param <A> The first type of the pair.
     * @param <B> The second type of the pair.
     */
    public static class Pair <A, B>  {
        /**
         * The first value of the pair.
         */
        A first;
        /**
         * The second value of the pair.
         */
        B second;

        /**
         * Creates a new pair with no values.
         */
        public Pair(){
            first = null;
            second = null;
        }

        /**
         * Creates a pair with the given values.
         * @param first the first item in the pair.
         * @param second the second item in the pair.
         */
        public Pair(A first, B second){
            this.first = first;
            this.second = second;
        }

        /**
         * Compares the first items of two pairs.
         * @param first the first pair.
         * @param second the second pair.
         * @param comparator the comparator used to compare the two values.
         * @param <A> The first type of both pairs.
         * @return the result of the comparator, conventionally -1 if the first pair is smaller, 1 if it's bigger, or 0 if they're equal.
         */
        static <A> int compareFirst(Pair<A, ?> first, Pair<A, ?> second, Comparator<A> comparator){
            return comparator.compare(first.first, second.first);
        }

        /**
         * Compares the second items of the two pairs.
         * @param first the first pair.
         * @param second the second pair.
         * @param comparator the comparator used to the compare the two values.
         * @param <B> the second type of both pairs.
         * @return the result of the comparator, conventionally -1 if the first pair is smaller, 1 if it's bigger, or 0 if they're equal.
         */
        static <B> int compareSecond(Pair<?, B> first, Pair<?, B> second, Comparator<B> comparator){
            return comparator.compare(first.second, second.second);
        }

        /**
         * Checks if this and the other pair's first values are equivalent.
         * @param other the other pair to be compared against.
         * @param comparator the comparator to use to compare the values.
         * @return true if the values are equivalent by the comparator, false otherwise.
         */
        boolean equalsFirst(Pair<A, B> other, Comparator<A> comparator){
            return compareFirst(this, other, comparator) == 0;
        }

        /**
         * Checks if this and the other pair's second values are equivalent.
         * @param other the other pair to be compared against.
         * @param comparator the comparator to use to compare the values.
         * @return true if the values are equivalent by the comparator, false otherwise.
         */
        boolean equalsSecond(Pair<A, B> other, Comparator<B> comparator){
            return compareSecond(this, other, comparator) == 0;
        }

        /**
         * Checks if this and the other pair's first and second values are equivalent.
         * @param other the other pair to be compared against.
         * @param comparatorFirst the comparator to compare the first values.
         * @param comparatorSecond the comparator to compare the second values.
         * @return true if both the first and second values match, false if otherwise.
         */
        public boolean equals(Pair<A, B> other, Comparator<A> comparatorFirst, Comparator<B> comparatorSecond){
            return equalsFirst(other, comparatorFirst) && equalsSecond(other, comparatorSecond);
        }

    }

    /*
    Here are the functions that are used for simplifying standard input.
    getString,
     */

    /**
     * Reads a string from the console.
     * @return the string that's been read.
     */
    static String getString(){
        return stdin.nextLine();
    }

    /**
     * Reads a string from the console and converts it to a double.
     * @return the double read from the console.
     */
    static double getDouble(){
        return Double.parseDouble(getString());
    }

    /**
     * Reads a string from the console and converts it to a double.
     * @return the integer read from the console.
     */
    public static int getInt() {
        return Integer.parseInt(getString());
    }

    /*
    Boolean functions to check for different properties of data.
     */

    /**
     * Checks whether a string is a palindrome.
     * @param s the string to be tested.
     * @return true if the string is a palindrome, false if it is not.
     */
    static boolean isPalindrome(String s) {
        return isPalindrome(s, String::toLowerCase);
    }
    /**
     * Checks whether a string is a palindrome.
     * @param s the string to be tested.
     * @param convertFunction a function to apply to a string before it's tested.
     * @return true if the string is a palindrome, false if it is not.
     */
    static boolean isPalindrome(String s, Function<String, String> convertFunction){
        char[] chars = convertFunction.apply(s).toCharArray();
        boolean palindrome = true;
        for(int i = 0; i < chars.length / 2 + 1; i++){
            palindrome &= chars[i] == chars[chars.length - 1 - i];
        }
        return palindrome;
    }


    /*
    Functions to convert from one type of data to another.
     */

    /**
     * Converts an array of elements to a set.
     * @param array the array to be convertd.
     * @param <T> the type of the array.
     * @return the resulting set.
     */
    static <T> HashSet<T> arrayToSet(T... array){
        HashSet<T> hashSet = new HashSet<>();
        Collections.addAll(hashSet, array);
        return hashSet;
    }
    /**
     * Converts a string to an array of integers.
     * @param s the string to convert.
     * @return the resulting array of integers.
     */
    static int[] stringToInts(String s){
        return Arrays.stream(s.split("[^0-9^-]+")).mapToInt(Integer::parseInt).toArray();
    }
    /**
     * Converts a string to an array of doubles.
     * @param s the string to convert.
     * @return the resulting array of doubles.
     */
    public static double[] stringToDoubles(String s){
        return Arrays.stream(s.split("[^0-9^.-]+")).mapToDouble(Double::parseDouble).toArray();
    }

    /*
    Functions and classes for the Expression Evaluator.
     */

    /**
     * A single generic token to be pushed to the output of Shunting Yard.
     */
    private static class ShuntingToken {
        /**
         * Represents the token as a string.
         * @return the string representation of the token.
         */
        public String asString(){
            return "(null)";
        }
    }

    /**
     * A token that represents an integer.
     */
    private static class ShuntingInt extends ShuntingToken {
        /**
         * The value of the integer.
         */
        int value;

        /**
         * Creates an empty integer token.
         */
        ShuntingInt(){
            value = 0;
        }

        @Override
        public String asString() {
            return Integer.toString(value);
        }
    }

    /**
     * A token that represents an operation.
     */
    private static class ShuntingChar extends ShuntingToken {
        /**
         * The operation that is represented by this token.
         */
        char operation;

        /**
         * Creates an empty operation token.
         */
        ShuntingChar(){
            operation = '\0';
        }

        @Override
        public String asString() {
            return "" + operation;
        }
    }

    /**
     * A token that represents a string.
     */
    private static class ShuntingString extends ShuntingToken {
        /**
         * The string that is represented by this token.
         */
        String string;

        /**
         *  Creates an empty operation token.
         */
        ShuntingString() { string = ""; }

        @Override
        public String asString() { return string; }
    }

    /**
     * A node in the Binary Tree representation of a
     * mathematical expression.
     */
    private static class ShuntingTreeNode {
        /**
         * The token represented by this node.
         */
        ShuntingToken data;
        /**
         * The left child node of this node.
         */
        ShuntingTreeNode left;
        /**
         * The right child node of this node.
         */
        ShuntingTreeNode right;

        /**
         * Creates an empty tree node.
         */
        ShuntingTreeNode(){
            this(null);
        }
        /**
         * Creates a tree node with no children and the specified data.
         * @param data the data to set.
         */
        ShuntingTreeNode(ShuntingToken data){
            this(data, null, null);
        }
        /**
         * Creates a tree node with the given data, and the given left and right nodes.
         * @param data the data to set.
         * @param left the left child node.
         * @param right the right child node.
         */
        ShuntingTreeNode(ShuntingToken data, ShuntingTreeNode left, ShuntingTreeNode right){
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
    /**
     * An interface used to implement an operation.
     */
    private interface ShuntingEvaluator {
        double eval(double a, double b);
    }

    /**
     * An enum that holds an operation's associativity.
     */
    private enum ShuntingAssociativity {
        LEFT,
        RIGHT
    }

    /**
     * A class to hold various information about a single operation, such as:
     * Its precedence (precedence('+') < precedence('*'))
     * Its associativity (associativity('^') == RIGHT, associativity('*') == LEFT)
     * ITs evaluator (what this operation does when it is evaluated as infix)
     */
    private static class ShuntingOperation {
        /**
         * The precedence of the operation.
         */
        int precedence;
        /**
         * The evaluator to perform this operation on numbers.
         */
        ShuntingEvaluator evaluator;
        /**
         * The associativity of the operation.
         */
        ShuntingAssociativity associativity;

        /**
         * Creates a new operation, setting its values to ones given here.
         * @param precedence the precedence of the operation.
         * @param evaluator the evaluator of the operation.
         * @param associativity the associativity of the operation.
         */
        ShuntingOperation(int precedence, ShuntingEvaluator evaluator, ShuntingAssociativity associativity){
            this.precedence = precedence;
            this.evaluator = evaluator;
            this.associativity = associativity;
        }
    }

    /**
     * A configuration used in evaluating expressions.
     * Holds infix operation characters mapped to their data (see ShuntingOperation)
     */
    static class ShuntingEvaluationConfig {
        /**
         * The map of operation characters to their properties.
         */
        HashMap<Character, ShuntingOperation> operations;
        /**
         * The variables to use in evaluating the expression.
         */
        HashMap<String, Double> variables;

        /**
         * Creates a new evaluation configuration, with no operation.
         */
        ShuntingEvaluationConfig(){
            operations = new HashMap<>();
            variables = new HashMap<>();
        }

        /**
         * Registers a single operation with this configuration.
         * @param operation the operation to register.
         * @param precedence the precedence of the new operation.
         * @param evaluator the evaluator of the new operation.
         * @param associativity the associativity of the new operation.
         * @return the configuration, for chaining.
         */
        ShuntingEvaluationConfig register(char operation, int precedence,
                                          ShuntingEvaluator evaluator, ShuntingAssociativity associativity){
            operations.put(operation, new ShuntingOperation(precedence, evaluator, associativity));
            return this;
        }

        /**
         * Registers a variable to be used in evaluation.
         * @param var the variable to store.
         * @param value the variable's new value.
         * @return the configuration, for chaining.
         */
        ShuntingEvaluationConfig variable(String var, double value){
            variables.put(var, value);
            return this;
        }

        /**
         * Creates a default configuration.
         * @return the default configuration.
         */
        static ShuntingEvaluationConfig createDefault(){
            return new ShuntingEvaluationConfig()
                    .register('+', 1, (a, b) -> a + b, ShuntingAssociativity.LEFT)
                    .register('-', 1, (a, b) -> a - b, ShuntingAssociativity.LEFT)
                    .register('*', 2, (a, b) -> a * b, ShuntingAssociativity.LEFT)
                    .register('/', 2, (a, b) -> a / b, ShuntingAssociativity.LEFT)
                    .register('^', 3, Math::pow, ShuntingAssociativity.RIGHT);
        }

    }

    /**
     * Evaluates an expression tree using the given operations.
     * @param root the root of the tree to be evaluated.
     * @param config the configuration file to pull info about operations from.
     * @return the result of the evaluation.
     */
    private static double evaluateTree(ShuntingTreeNode root, ShuntingEvaluationConfig config){
        if(root.data instanceof ShuntingInt){
            return ((ShuntingInt) root.data).value;
        } else if(root.data instanceof ShuntingString) {
            ShuntingString shuntingString = (ShuntingString) root.data;
            return (config.variables.containsKey(shuntingString.string)) ?
                    config.variables.get(shuntingString.string) : 0;
        } else {
            return config.operations.get(((ShuntingChar) root.data).operation).evaluator.eval(
                    evaluateTree(root.left, config),
                    evaluateTree(root.right, config));
        }
    }
    /**
     * Build a tree out of the given shunting yard tokens.
     * @param tokens the tokens to use to build the tree.
     * @return the resulting tree.
     */
    private static ShuntingTreeNode buildtree(Stack<ShuntingToken> tokens){
        ShuntingToken currentToken = tokens.pop();
        ShuntingTreeNode right = (currentToken instanceof ShuntingChar) ? buildtree(tokens) : null;
        ShuntingTreeNode left = (currentToken instanceof ShuntingChar) ? buildtree(tokens) : null;
        return new ShuntingTreeNode(currentToken, left, right);
    }
    /**
     * Performs the Shunting-Yard algorithm to reorder the given string, in infix, into postfix notation.
     * @param expression the expression to reorder.
     * @param config the configuration from which to pull the operation data.
     * @return a stack of ShuntingTokens, which represent the tokens extracted from the string, pushed onto the stack in their resulting order.
     */
    private static Stack<ShuntingToken> performShuntingYard(String expression, ShuntingEvaluationConfig config) throws ArithmeticException {
        char[] array = expression.toCharArray();
        int index = 0;
        Stack<ShuntingChar> shuntingStack = new Stack<>();
        Stack<ShuntingToken> outputStack = new Stack<>();

        while(index < array.length){
            if(Character.isDigit(array[index])){
                ShuntingInt newInt = new ShuntingInt();
                while(index < array.length && Character.isDigit(array[index])){
                    newInt.value = newInt.value * 10 + (array[index] - '0');
                    index++;
                }
                outputStack.push(newInt);
            } else if(config.operations.containsKey(array[index])){
                ShuntingOperation operation = config.operations.get(array[index]);
                ShuntingChar newChar = new ShuntingChar();
                newChar.operation = array[index];
                while(!shuntingStack.empty() &&
                        config.operations.containsKey(shuntingStack.peek().operation) &&
                        ((operation.associativity == ShuntingAssociativity.LEFT  &&
                                operation.precedence <= config.operations.get(shuntingStack.peek().operation).precedence) ||
                                (operation.associativity == ShuntingAssociativity.RIGHT &&
                                        operation.precedence < config.operations.get(shuntingStack.peek().operation).precedence)
                        )){
                    outputStack.push(shuntingStack.pop());
                }
                shuntingStack.push(newChar);
                index++;
            } else if(array[index] == '('){
                ShuntingChar newParenth = new ShuntingChar();
                newParenth.operation = '(';
                shuntingStack.push(newParenth);
                index++;
            } else if(array[index] == ')'){
                while(!shuntingStack.empty() && shuntingStack.peek().operation != '('){
                    outputStack.push(shuntingStack.pop());
                }
                if(shuntingStack.empty()){
                    throw new ArithmeticException("Invalid infix expression: " + expression);
                } else {
                    shuntingStack.pop();
                }
                index++;
            } else if(Character.isAlphabetic(array[index])){
                ShuntingString shuntingString = new ShuntingString();
                StringBuilder stringBuffer = new StringBuilder();
                while(Character.isAlphabetic(array[index])) {
                    stringBuffer.append(array[index]);
                    index++;
                }
                shuntingString.string = stringBuffer.toString();
                outputStack.push(shuntingString);
            } else {
                index++;
            }
        }
        while(!shuntingStack.empty()){
            if(shuntingStack.peek().operation == '(') {
                throw new ArithmeticException("Invalid infix expression: " + expression);
            } else {
                outputStack.push(shuntingStack.pop());
            }
        }

        return outputStack;
    }

    /**
     * Evaluates an expression by converting it to Shunting-Yard, building a tree, and evaluating that tree.
     * @param expression the expression to convert.
     * @param config the configuraton to use to convert the expression.
     * @return the result of converting the expression.
     * @throws ArithmeticException if the expression is incorrectly formatted.
     */
    static double evaluateExpression(String expression, ShuntingEvaluationConfig config) throws ArithmeticException {
        Stack<ShuntingToken> tokens = performShuntingYard(expression, config);
        ShuntingTreeNode tree = buildtree(tokens);
        return evaluateTree(tree, config);
    }
}
