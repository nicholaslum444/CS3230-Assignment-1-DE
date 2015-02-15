/**
 *  This is a Java template for CS3230 - Programming Assignment 1 - Part 1
 *  (January-2015)
 *
 *  You are not required to follow the template. Feel free to modify any part.
 *
 *  Comment your code!
 */


import java.io.*;
import java.util.*;


class Template { 
	
	static final int CUTOFF = 5005;
	
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); 
        
        int t, b;
		String v, m, p; 
        t = sc.nextInt();
		
        for (int i = 1; i <= t; ++i) {
            b = sc.nextInt();
			sc.nextLine();
			
			v = sc.nextLine(); 
			m = sc.nextLine();
			p = ""; // p is the momentum
			
			// Insert solution here.
			int vDecimals = getDecimals(v);
			int mDecimals = getDecimals(m);
			int pDecimals = vDecimals + mDecimals;
			
			// string concats are scary monsters.
			
			v = removeDot(v);
			m = removeDot(m);
			
			int[] vArr = toIntArray(v, Math.max(v.length(), m.length()));
			int[] mArr = toIntArray(m, Math.max(v.length(), m.length()));
			
			int[] pArr = carrotSoba(vArr, mArr);
			
			// make back into string
			p = makeString(pArr);
			// add decimal point
			p = installDot(p, pDecimals);
			// trim zeros
			// remove trailing dot
			p = trimZeros(p);
			
			pw.write(p);
			pw.write("\n");
        }
        pw.close(); // do not forget to use this
        sc.close();
    }
    
    public static int[] splitHigh(int[] x, int r) {
    	// [0, 1, 2, 3, 4] splitHigh 2 = [2, 3, 4]
    	
    	return null;
    }
    
    public static int[] splitLow(int[] x, int r) {
    	// [0, 1, 2, 3, 4] splitlow 1 = [0, 1]
    	
    	return null;
    }
    
    public static int[] carrotSoba(int[] x, int[] y) {
    	int maxLen = Math.max(x.length, y.length);
    	if (maxLen <= CUTOFF) {
    		return multiply(x, y, 10);
    	}
    	
    	// split the two arrays at maxlen/2
    	int r = maxLen / 2;
    	int[] xHigh = splitHigh(x, r);
    	int[] xLow = splitLow(x, r-1);
    	int[] yHigh = splitHigh(y, r);
    	int[] yLow = splitLow(y, r-1);
    	
    	// 3 recursives
    	int[] z0 = carrotSoba(xLow, yLow);
    	int[] z1 = carrotSoba(add(xLow, xHigh), add(yLow, yHigh));
    	int[] z2 = carrotSoba(xHigh, yHigh);
    	
    	// Res = Z2 * B^(2*R) + (Z1-Z2-Z0)*B^R + Z0
    	
    	// (Z1-Z2-Z0)
    	int[] zq = minus(z1, minus(z2, z0));
    	
    	// (Z1-Z2-Z0)*B^R
    	int[] zqn = multiplyTens(zq, r);
    	
    	// Z2 * B^(2*R)
    	int[] z2n = multiplyTens(z2, 2 * r);
    	
    	// Z2 * B^(2*R) + (Z1-Z2-Z0)*B^R + Z0
    	int[] res = add(z2n, add(zqn, z0));
    	
    	//return 
    	return res;
    }

	public static int[] add(int[] x, int[] y) {
		
		return null;
	}

	public static int[] minus(int[] x, int[] y) {
		
		return null;
	}

	public static int[] multiply(int[] x, int[] y, int b) {
    	// ensure the lower digit number below
    	if (x.length < y.length) {
    		return multiply(y, x, b);
    	}
    	
    	int[] ans = new int[x.length + y.length];
    	
    	// do long mult by placing each subsequent mult on top of the below one
    	for (int i = 0; i < y.length; i++) {
    		for (int j = 0; j < x.length; j++) {
    			ans[j + i] = x[j] * y[i] + ans[j + i];
    		}
    	}
    	
    	// carry over 
    	int car = 0;
    	for (int i = 0; i < ans.length; i++) {
    		int sum = ans[i] + car;
    		int rem = sum % b;
    		ans[i] = rem;
    		car = sum / b;
    	}
    	
    	return ans;
    }
    
    
    public static int[] multiplyTens(int[] x, int n) {
		
		return null;
	}

	private static int[] toIntArray(String s, int size) {
    	s = new StringBuilder(s).reverse().toString();
    	
    	int[] array = new int[size];
    	boolean passedDot = false;
    	for (int i = 0; i < s.length(); i++) {
    		if (s.charAt(i) == '.') {
    			passedDot = true;
    			continue;
    		} else {
    			int j = i;
    			if (passedDot) {
    				j--;
    			}
    			array[j] = parseDigit(s.charAt(i));
    		}
    	}
    	
    	return array;
    }
    
    private static String removeDot(String s) {
    	String ans = s;
    	if (s.contains(".")) {
    		String[] sArr = s.split("\\.");
    		StringBuilder ansB = new StringBuilder(sArr[0]);
    		ansB.append(sArr[1]);
    		ans = ansB.toString();
    	}
    	
    	return ans;
    }
    
    private static String makeString(int[] a) {
		StringBuilder s = new StringBuilder();
		
		for (int i = 0; i < a.length; i++) {
			s.append(toDigit(a[i]));
		}
		
		return s.toString();
	}

	private static String installDot(String s, int pos) {
		String ans = "";
		
		if (pos == 0) {
			StringBuilder sb = new StringBuilder(".");
			sb.append(s);
			ans = sb.toString();
		} else {
			StringBuilder sb = new StringBuilder(s.substring(0, pos));
			sb.append(".");
			sb.append(s.substring(pos, s.length()));
			ans = sb.toString();
		}
		
		ans = new StringBuilder(ans).reverse().toString();
		
		return ans;
	}

	private static int getDecimals(String s) {
    	int decimals = 0;
    	
    	if (s.contains(".")) {
    		String[] sArr = s.split("\\.");
    		decimals = sArr[1].length();
    	}
    	
    	return decimals;
    }
	
	
	/**
	 * Use to trim leading and trailing zeros on a result string.
	 */
	private static String trimZeros(String input) {
		int left = 0;
		int right = input.length()-1;
		int fp = input.indexOf('.');
		if (fp == -1) {
			fp = input.length();
		}
		
		while(left < fp-1) {
			if (input.charAt(left) != '0')
				break;
			left++;
		}
		
		while (right >= fp) {
			if (input.charAt(right) != '0') {
				if (input.charAt(right) == '.')
					right--;
				break;
			}
			right--;
		}
		
		if (left >= fp)
			return "0" + input.substring(left,right+1);
		return input.substring(left,right+1);
	}
    
	/**
	 * Convert digit to int (for reading)
	 */
	private static int parseDigit(char c) {
		if (c <= '9') {
			return c - '0';
		} 
		return c - 'A' + 10;
	}
	
	/**
	 * Convert int to digit. (for printing)
	 */
	private static char toDigit(int digit) {
		if (digit <= 9) {
			return (char)(digit + '0');
		} 
		return (char)(digit - 10 + 'A');
	}
	
	private static void print(Object o) {
		//System.err.println(o);
	}
}
