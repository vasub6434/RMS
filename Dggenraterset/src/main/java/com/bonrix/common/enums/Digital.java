package com.bonrix.common.enums;

import java.text.ParseException;
public class Digital {

	public static void main(String[] args) throws ParseException {
		System.out.println(Integer.parseUnsignedInt("004B0064", 16));
	}
	
	public static String hexto8bit(String ii) {
		//log.info("ii  :: " + ii);
		int ll = Integer.parseInt(ii, 16);

		String kk = Integer.toBinaryString(ll);

		for (int j = kk.length(); j < 8; j++) {
			kk = "0" + kk;
		}
	//	log.info("kk :  " + kk);
		return kk;
	}
	
	public  static String  intToBinary(int a) {
		String temp = Integer.toBinaryString(a);
		while(temp.length() !=8){ temp = "0"+temp; 
		} 
		//log.info("TEMP :: "+temp); 
		return temp;
		}
	
	public static String reverse(String str)
	{
StringBuilder sb = new StringBuilder();
        for(int i = str.length() - 1; i >= 0; i--)
            sb.append(str.charAt(i));
        return sb.toString();
	}
	public static String  hexto4bit(String ii) {
		String st="";
		for(int i=0;i<ii.length();i++)
		{
			int ll = Integer.parseInt(""+ii.charAt(i), 16);
			String kk = Integer.toBinaryString(ll);
			for (int j = kk.length(); j < 4; j++) 
				kk = "0" + kk;
			st+=kk;
		}
		return st;
	}

}
