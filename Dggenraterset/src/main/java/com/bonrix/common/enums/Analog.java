package com.bonrix.common.enums;

import java.text.ParseException;

	public class Analog {

		public static void main(String[] args) throws ParseException {
		String msg="23535030003F00643132333435363738393031323334353637383930003F524C4A4852414E32343232303138313832303039313900000000000600000000083F00000000012C3F3F000000000000013F021101410000003F000000000000000000040000000000000000000000010000000000000000000000000000000000000000033F011501170000003F443F366747051A3F404C3F3F42073333433F400000000000000000000000000000000000453F3F00453F180043520000441C40000000000000000000455D3F000000000045063F00000000000000000000000000453F70002E";
		//	String msg="23534630003F00643132333435363738393031323334353637383930003F524C4A4852414E323432323031383231313730393139000000000202000000000C3F0033033F012C013F033F00000000020C013F013A0000003F000000000000066B0850082C003F00000000013F00010000000000000000000000000000000000000000033F011501180000003F443F366747051A3F404C3F3F42073333433F4C3F00000000000000000000000000000000453F3F00453F180043520000441C40000000000000000000455D3F000000000045063F00000000000000000000000000453F70002E";
			String Alarm_STRING = msg.substring(104,116); 
			 System.out.println(Alarm_STRING);
			 System.out.println(hexto4bit(Alarm_STRING));
			 System.out.println(  reverse(hexto4bit(Alarm_STRING)));
			 String finalString= reverse(hexto4bit(Alarm_STRING));
			 System.out.println("Mains Fail ::: "+finalString.charAt(2));
			 System.out.println("Battery  ::: "+finalString.charAt(8));
			// String dd="831373039313";
			 String dd = msg.substring(80, 104);
				//log.info("BONRIX ::: datet ::::::::::: " + convertHexToString(datet));
			 System.out.println("DD :: "+dd);
System.out.println(convertHexToString(dd));
				String s = convertHexToString(dd);
				String date = s.substring(0, 2);
				String hour = s.substring(2, 4);
				String minut = s.substring(4, 6);
				String second = s.substring(6, 8);
				String month = s.substring(8, 10);
				String year = s.substring(10, 12);
				String datestr = date + month + year + hour + minut + second;
				System.out.println("datestr  ::: "+datestr);
			
		}
		
		public static String convertHexToString(String hex) {

			StringBuilder sb = new StringBuilder();
			StringBuilder temp = new StringBuilder();

			// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
			for (int i = 0; i < hex.length() - 1; i += 2) {

				// grab the hex in pairs
				String output = hex.substring(i, (i + 2));
				// convert hex to decimal
				int decimal = Integer.parseInt(output, 16);
				// convert the decimal to character
				sb.append((char) decimal);

				temp.append(decimal);
			}
			//log.info("Decimal : " + temp.toString());

			return sb.toString();
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