package com.bonrix.common.test;

public class MODBUSRTU {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String strmsg="ATL865293041111167,250521,093039,1,000001:3B44,000002:9BA6,000003:0000,000004:0000,000005:3B03,000006:126F,000007:0000,000008:0000,000009:3FD4,000010:DD2F,000011:3F8C,000012:CCCD,000013:4227,000014:EC8B,000015:402C,000016:CCCD,000017:4253,000018:0A3D,,,,,,,,,,,,,,,,,,,,,,,ATL#";
		String[] msgary = strmsg.split(",");
		String ch1=msgary[4].split(":")[1]+msgary[5].split(":")[1];
		String ch2=msgary[8].split(":")[1]+msgary[9].split(":")[1];
		String ch3=msgary[12].split(":")[1]+msgary[13].split(":")[1];
		String ch4=msgary[16].split(":")[1]+msgary[17].split(":")[1];
		String volt=msgary[20].split(":")[1]+msgary[21].split(":")[1];
		String kWh_of_CH1=ConcertMODBUSRTUValue(ch1);
		String kWh_of_CH2=ConcertMODBUSRTUValue(ch2);
		String kWh_of_CH3=ConcertMODBUSRTUValue(ch3);
		String kWh_of_CH4=ConcertMODBUSRTUValue(ch4);
		String Voltage=ConcertMODBUSRTUValue(volt);
		System.out.println(ch1);
		System.out.println(ch2);
		System.out.println(ch3);
		System.out.println(ch4);
		System.out.println(volt);
		System.out.println(kWh_of_CH1);
		System.out.println(kWh_of_CH2);
		System.out.println(kWh_of_CH3);
		System.out.println(kWh_of_CH4);
		System.out.println(Voltage);
		
	}
	
	static String ConcertMODBUSRTUValue(String hexVal)
	{
        Long i1 = Long.parseLong(hexVal, 16);
        Float f1 = Float.intBitsToFloat(i1.intValue());
        return String.valueOf(f1);
	}

}
