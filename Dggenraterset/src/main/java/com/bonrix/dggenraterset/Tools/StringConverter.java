package com.bonrix.dggenraterset.Tools;


public class StringConverter {

      public String convertto8bit(String convert)
    {
       int ll=Integer.parseInt(convert);
      String kk=Integer.toBinaryString(ll);

      for(int j=kk.length();j<8;j++)
      {
          kk="0"+kk;
      }

      return kk;
  }
         public String hexto8bit(String ii)
    {
       int ll=Integer.parseInt(ii,16);

      String kk=Integer.toBinaryString(ll);

      for(int j=kk.length();j<8;j++)
      {
          kk="0"+kk;
      }

      return kk;
  }


}
