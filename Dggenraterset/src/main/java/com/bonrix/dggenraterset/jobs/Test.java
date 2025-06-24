package com.bonrix.dggenraterset.jobs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
    	
    	List<String> abc = new ArrayList<>();
    	abc.add("A");
    	abc.add("B");
    	abc.add("C");
    	
    	String string = "ATL865962059530316";
    	System.out.println(string.substring(4));
    	String ii="14B4";
    	String st="";
		for(int i=0;i<ii.length();i++)
		{
			int ll = Integer.parseInt(""+ii.charAt(i), 16);
			String kk = Integer.toBinaryString(ll);
			for (int j = kk.length(); j < 4; j++) 
				kk = "0" + kk;
			st+=kk;
		}
		int decimal=Integer.parseInt(ii,16);  

		System.out.println(decimal);
    }
    
}