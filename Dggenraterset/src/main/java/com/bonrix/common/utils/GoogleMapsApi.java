package com.bonrix.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;


public class GoogleMapsApi {
	static Logger log=Logger.getLogger(GoogleMapsApi.class);
	
	public static void main(String[] args) {
		String[] arryy=GetLatLng(404,24,15005,1013);
		System.out.println(arryy[0]+" "+arryy[1]);
		/*double lat = 0;
		Double b1 = Double.parseDouble(arryy[0]);
		double lon = 0;
		Double b2 = Double.parseDouble(arryy[1]);*/
	}
	
	 static byte[] PostData(int MCC, int MNC, int LAC, int CID)
	    {
	        /* The shortCID parameter follows heuristic experiences:

	         * Sometimes UMTS CIDs are build up from the original GSM CID (lower 4 hex digits)

	         * and the RNC-ID left shifted into the upper 4 digits.

	         */
	        byte[] pd = new byte[] {

	    0x00, 0x0e,

	    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

	    0x00, 0x00,

	    0x00, 0x00,

	    0x00, 0x00,

	    0x1b,

	    0x00, 0x00, 0x00, 0x00, // Offset 0x11

	    0x00, 0x00, 0x00, 0x00, // Offset 0x15

	    0x00, 0x00, 0x00, 0x00, // Offset 0x19

	    0x00, 0x00,

	    0x00, 0x00, 0x00, 0x00, // Offset 0x1f

	    0x00, 0x00, 0x00, 0x00, // Offset 0x23

	    0x00, 0x00, 0x00, 0x00, // Offset 0x27

	    0x00, 0x00, 0x00, 0x00, // Offset 0x2b

	    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,

	    0x00, 0x00, 0x00, 0x00

	};
	    /*    if (shortCID)

	            CID &= 0xFFFF;      /* Attempt to resolve the cell using the

	                        GSM CID part */

	        if (CID > 65536) /* GSM: 4 hex digits, UTMS: 6 hex

	                        digits */

	            pd[0x1c] = 5;

	        else

	            pd[0x1c] = 3;

	        pd[0x11] = (byte)((MNC >> 24) & 0xFF);

	        pd[0x12] = (byte)((MNC >> 16) & 0xFF);

	        pd[0x13] = (byte)((MNC >> 8) & 0xFF);

	        pd[0x14] = (byte)((MNC >> 0) & 0xFF);

	        pd[0x15] = (byte)((MCC >> 24) & 0xFF);

	        pd[0x16] = (byte)((MCC >> 16) & 0xFF);

	        pd[0x17] = (byte)((MCC >> 8) & 0xFF);

	        pd[0x18] = (byte)((MCC >> 0) & 0xFF);

	        pd[0x27] = (byte)((MNC >> 24) & 0xFF);

	        pd[0x28] = (byte)((MNC >> 16) & 0xFF);

	        pd[0x29] = (byte)((MNC >> 8) & 0xFF);

	        pd[0x2a] = (byte)((MNC >> 0) & 0xFF);

	        pd[0x2b] = (byte)((MCC >> 24) & 0xFF);

	        pd[0x2c] = (byte)((MCC >> 16) & 0xFF);

	        pd[0x2d] = (byte)((MCC >> 8) & 0xFF);

	        pd[0x2e] = (byte)((MCC >> 0) & 0xFF);

	        pd[0x1f] = (byte)((CID >> 24) & 0xFF);

	        pd[0x20] = (byte)((CID >> 16) & 0xFF);

	        pd[0x21] = (byte)((CID >> 8) & 0xFF);

	        pd[0x22] = (byte)((CID >> 0) & 0xFF);

	        pd[0x23] = (byte)((LAC >> 24) & 0xFF);

	        pd[0x24] = (byte)((LAC >> 16) & 0xFF);

	        pd[0x25] = (byte)((LAC >> 8) & 0xFF);

	        pd[0x26] = (byte)((LAC >> 0) & 0xFF);

	        return pd;

	    }

	    static public String[] GetLatLng(int MCC, int MNC, int LAC, int CID)
	    {

	    	String[] result = new String[2];

	        try
	        {

	            String url = "http://www.google.com/glm/mmap";

	            URL urll = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection) urll.openConnection();
	            conn.setUseCaches(false);
	    		conn.setDoInput(true);
	    		conn.setDoOutput(true);
	    		conn.setRequestMethod("POST");
	    		 byte[] pd = PostData(MCC, MNC, LAC, CID);
	    		// System.out.println(Arrays.toString(pd));
	    		// System.out.println(pd.length);
	    		conn.setRequestProperty("Content-Length",  pd.length+"");
	    		conn.setRequestProperty("Content-Type",  "application/binary");
	    		OutputStream  wr = conn.getOutputStream();
				wr.write(pd,0,pd.length);
				wr.flush();
				wr.close();
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				//br.read()
				String output="";
				byte[] ps = null ;
				while ((output = br.readLine()) != null) {
					ps=output.getBytes();
				}
				

	           

	                //short opcode1 = (short)(ps[0] << 8 | ps[1]);

	               // byte opcode2 = ps[2];

	                int ret_code = (int)((ps[3] << 24) | (ps[4] << 16) |

	                               (ps[5] << 8) | (ps[6]));

	                if (ret_code == 0)
	                {

	                    double lat = ((double)((ps[7] << 24) | (ps[8] << 16)

	                                 | (ps[9] << 8) | (ps[10]))) / 1000000;

	                    double lon = ((double)((ps[11] << 24) | (ps[12] <<

	                                 16) | (ps[13] << 8) | (ps[14]))) /

	                                 1000000;

	                    result[0] = lat+"";
	                    result[1] = lon+"";
	                    return result;

	                }

	                else
	                {

	                    return result;
	                }




	        }
	        catch (Exception ex)
	        {
	           ex.printStackTrace();
	            return result;

	        }

	    }
}
