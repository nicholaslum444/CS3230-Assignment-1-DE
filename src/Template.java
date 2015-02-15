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
	
	static final int CUTOFF = 1000;
	
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
			
			// convert to array
			int maxLen = Math.max(v.length(), m.length());
			int[] vArr = toIntArray(v, maxLen);
			int[] mArr = toIntArray(m, maxLen);
			
			// karasuba
			int[] pArr = carrotSoba(vArr, mArr);
			
			// make back into string
			p = makeString(pArr);
			p = trimZeros(p);
			
			pw.write(p);
			pw.write("\n");
        }
        pw.close(); // do not forget to use this
        sc.close();
    }
    
    public static int[] splitHigh(int[] x, int r) {
    	// [0, 1, 2, 3, 4] splitHigh 2 = [2, 3, 4]
    	int resLen = x.length - r;
    	int[] res = new int[resLen];
    	for (int i = r, j = 0; i < x.length; i++, j++) {
    		res[j] = x[i];
    	}
    	
    	return res;
    }
    
    public static int[] splitLow(int[] x, int r) {
    	// [0, 1, 2, 3, 4] splitlow 2 = [0, 1]
    	int[] res = new int[r];
    	for (int i = 0, j = 0; i < r; i++, j++) {
    		res[j] = x[i];
    	}
    	
    	return res;
    }
    
    public static int[] carrotSoba(int[] x, int[] y) {
    	int maxLen = Math.max(x.length, y.length);
    	if (maxLen <= CUTOFF) {
    		int[] ans = multiply(x, y, 10);
    		return ans;
    	}
    	
    	// split the two arrays at maxlen/2
    	int r = maxLen / 2;
    	int[] xHigh = splitHigh(x, r);
    	int[] xLow = splitLow(x, r);
    	int[] yHigh = splitHigh(y, r);
    	int[] yLow = splitLow(y, r);
    	
    	// 3 recursives
    	int[] z0 = carrotSoba(xLow, yLow);
    	int[] z1 = carrotSoba(add(xLow, xHigh), add(yLow, yHigh));
    	int[] z2 = carrotSoba(xHigh, yHigh);
    	
    	// Res = Z2 * B^(2*R) + (Z1-Z2-Z0)*B^R + Z0
    	
    	// (Z1-Z2-Z0)
    	int[] zq = minus(minus(z1, z2), z0);
    	
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
    	// ensure the lower digit number below
    	if (x.length < y.length) {
    		return add(y, x);
    	}
    	
    	int[] res = new int[x.length+1];
    	
    	// i -> x, j -> y, y below
    	//  001
    	// +01
    	//  011
    	
    	// add each cell, ignore carry
    	for (int i = 0; i < x.length; i++) {
    		if (i >= y.length) {
    			res[i] = x[i];
    		} else {
    			res[i] = x[i] + y[i];
    		}
    	}
    	
    	// settle carry
    	int car = 0;
    	for (int i = 0; i < res.length; i++) {
    		int sum = res[i] + car;
    		int rem = sum % 10;
    		res[i] = rem;
    		car = sum / 10;
    	}
    	
    	// trim if leading digit is 0 else there will be ooloop
    	res = trimArrayLeadingZeros(res);
    	
		return res;
	}

	public static int[] minus(int[] x, int[] y) {
		// ensure the lower digit number below
    	if (x.length < y.length) {
    		return add(y, x);
    	}
    	
    	int[] res = new int[x.length+1];
    	
    	// i -> x, j -> y, y below
    	//  0  5  4
    	// -6  5
    	// -6  0  4
    	// +A -1+A -1
    	//  4  9  3
    	
    	// add each cell, ignore carry
    	for (int i = 0; i < x.length; i++) {
    		if (i >= y.length) {
    			res[i] = x[i];
    		} else {
    			res[i] = x[i] - y[i];
    		}
    	}
    	
    	// settle carry
    	int bor = 0;
    	for (int i = 0; i < res.length; i++) {
    		res[i] -= bor;
    		if (res[i] < 0) {
    			res[i] += 10;
    			bor = 1;
    		} else {
    			bor = 0;
    		}
    	}
    	
    	// trim if leading digit is 0 else there will be ooloop
    	res = trimArrayLeadingZeros(res);
    	
		return res;
	}
	
	public static int[] multiplyTens(int[] x, int n) {
		// 001 * 10^3 = 000,001
		int resLen = x.length + n;
		int[] res = new int[resLen];
		for (int i = n; i < res.length; i++) {
			res[i] = x[i-n];
		}
		return res;
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
    	
    	ans = trimArrayLeadingZeros(ans);
    	
    	return ans;
    }
    
    
    public static int[] trimArrayLeadingZeros(int[] res) {
    	//print(Arrays.toString(res));
    	int last = res.length-1;
		while (last > 1 && res[last] == 0) {
			last--;
		}
		res = Arrays.copyOfRange(res, 0, last+1);
		return res;
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
    
    private static String makeString(int[] a) {
		StringBuilder s = new StringBuilder();
		
		for (int i = 0; i < a.length; i++) {
			s.append(toDigit(a[i]));
		}
		
		return s.reverse().toString();
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
		
		/*if (left >= fp) {
			return "0" + input.substring(left,right+1);
		}*/
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
