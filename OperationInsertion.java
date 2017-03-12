import java.util.ArrayList;
import java.util.Arrays;

public class OperationInsertion {
	
	char[] operators = {'+', '-', '*'};
	
	public OperationInsertion(){
		int[] ints = Utils.stringToInts(Utils.getString());
		boolean printed = false;
		
		n : for (int e = 0; e < ints.length - 1; e++) {//place equals sign after e
			char[] lhs = toCharArray(Arrays.copyOfRange(ints, 0, e+1));
			char[] rhs = toCharArray(Arrays.copyOfRange(ints, e+1, ints.length));
			ArrayList<Utils.Pair<Double, String>> lhsCombs = getCombs(lhs);
			//System.out.println(Arrays.toString(rhs));
			ArrayList<Utils.Pair<Double, String>> rhsCombs = getCombs(rhs);
			
			for (int i = 0; i < lhsCombs.size(); i++) {
				for (int j = 0; j < rhsCombs.size(); j++) {
					if(lhsCombs.get(i).equalsFirst(rhsCombs.get(j), (a,b) -> Double.compare(a, b))){
						System.out.println(lhsCombs.get(i).second + " = " + rhsCombs.get(j).second);
						printed = true;
						break n;
					}
				}
			}
		}
		
		if(!printed){
			System.out.println("No equation possible.");
		}
	}
	
	public static void main(String[] args){
		new OperationInsertion();
	}
	
	ArrayList<Utils.Pair<Double, String>> getCombs(char[] expr){
		ArrayList<Utils.Pair<Double, String>> array = new ArrayList<Utils.Pair<Double, String>>();
		getCombinations(array, expr);
		return array;
	}
	
	void getCombinations(ArrayList<Utils.Pair<Double, String>> currentList, char[] expr){
		String str = charToStr(expr);
		if(expr.length==1){
			currentList.add(new Utils.Pair<Double, String>((double)(expr[0]+'0'), ""+expr[0]));
			return;
		}
		int index = str.indexOf(0);
		if(index==str.lastIndexOf(0)){
			for (char c : operators) {
				char[] newExpr = Arrays.copyOf(expr, expr.length);
				newExpr[index] = c;
				String fullStr = charToStr(newExpr);
				//System.out.println(fullStr);
				currentList.add(new Utils.Pair<Double, String>(Utils.evaluateExpression(fullStr, Utils.ShuntingEvaluationConfig.createDefault()), fullStr));
			}
		}
		else{
			for (char c : operators) {
				char[] newExpr = Arrays.copyOf(expr, expr.length);
				newExpr[index] = c;
				getCombinations(currentList, newExpr);
			}
		}
	}
	
	String charToStr(char[] array){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
		}
		return builder.toString();
	}
	
	char[] toCharArray(int[] array){//With spaces for operators
		StringBuilder builder = new StringBuilder("" + array[0]);
		for (int i = 1; i < array.length; i++) {
			builder.append('\0').append(""+array[i]);
		}
		return builder.toString().toCharArray();
	}
}