package com.bonrix.common.utils;

public class CheckSum {

	static String data="ATL861693031524309,$GPRMC,000128.021,V,,,,,0000,0.00,060180,,,N*54,#01111011000100,0,0,0,0.01,0,4.38,15,404,24,3a9d,3f5,0,,1.0_enr_mtr,,INTERNET,FFFFFFFFFFFFFFFF,ATL' ATL861693031524309,$GPRMC,000224.020,V,,,,,0000,0.00,060180,,,N*5A,#01111011000100,0,0,0,0.01,0,4.38,10,404,24,3a9d,4df0,0,,1.0_enr_mtr,,INTERNET,FFFFFFFFFFFFFFFFATL"; 
	//3B
	public byte chksum_gprs(byte[] p)
	{
	 int i = 0;
	byte chksum=0;
		for(i=0;i< p.length-1 && p[i]!= 0x00;i++)
		{
		chksum ^= p[i]++;
		}
		return chksum;
	}
	public static void main(String[] args) {
		System.out.println(new CheckSum().chksum_gprs(data.getBytes()));
	}
}
