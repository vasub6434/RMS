package com.bonrix.dggenraterset.TcpServer;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelLocal;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.CharsetUtil;

import com.bonrix.dggenraterset.Oprations.GT06;
import com.bonrix.dggenraterset.Tools.Crc16;
public class GT06Server  extends SimpleChannelUpstreamHandler{
	private Logger log = Logger.getLogger(GT06Server.class);
	public static final ChannelLocal<String> IMEIdata = new ChannelLocal<String>();
	public static final ChannelLocal<String> STATUESdata = new ChannelLocal<String>();
	public static final ChannelLocal<String> ACSTATUESdata = new ChannelLocal<String>();
	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

	GT06 gt06=new GT06();
		
	
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws ParseException {
			String msg = (String) e.getMessage();
			String dataString = printHexBinary(msg.getBytes(CharsetUtil.ISO_8859_1)).toLowerCase();
			try {
				log.info("SAM:: HExa DataString:: " + dataString);
				// log.info("SAM:: Decimal DataString:: " + dataString);
				// ac string 78780c 69 1108010c3a1801005d09a30d0a
				// login packet 78780D 01 035341353215036200022D060D0A
				String protocol = dataString.substring(6, 8);
				if (protocol.equals("01") || protocol.equals("13") || protocol.equals("69")) {
					if (protocol.equals("01")) {
						log.info("SAM:: LoGin Request " + dataString);
						// log.info("SAM:: SET IMEI No ::: " + dataString.substring(8, 24));
						IMEIdata.set(e.getChannel(), dataString.substring(8, 24));
						log.info("SAM:: Login Response:: " + getBytsToString(getLoginResponse()));
						e.getChannel().write(ChannelBuffers.wrappedBuffer(getLoginResponse()));
					} else if (protocol.equals("13"))// Status information
					{
						log.info("SAM:: Status Request ::: " + dataString);
						STATUESdata.set(e.getChannel(), dataString);
						log.info("SAM:: Status Response:: " + getBytsToString(getStatuesResponse()));
						e.getChannel().write(ChannelBuffers.wrappedBuffer(getStatuesResponse()));
					} else if (protocol.equals("69")) {

						log.info("SAM:: AC Status Request ::: " + dataString);
						ACSTATUESdata.set(e.getChannel(), dataString);
						log.info("SAM:: AC Status Response:: " + getBytsToString(getStatuesResponse()));
						e.getChannel().write(ChannelBuffers.wrappedBuffer(getStatuesResponse()));
					}

				} else // location information
				{
					//log.info("SAM:: Location information ::: " + dataString);
					//log.info("statues " + dataString.substring(50, 52));
					String status = "78780a1345002e00010312f1d40d0a";
					String acstatus = "78780a1345002e00010312f1d40d0a";
					try {
						status = STATUESdata.get(e.getChannel()).toString();
					} catch (Exception e2) {
						status = "78780a1345002e00010312f1d40d0a";
					}
					try {
						acstatus = ACSTATUESdata.get(e.getChannel()).toString();
					} catch (Exception e2) {
						acstatus="78780a1345002e00010312f1d40d0a";
					}
					log.info("GT06 :: "+gt06);
					gt06.parseGT06(dataString, IMEIdata.get(e.getChannel()), status,acstatus);
					//
					
					log.info("SAM:       Response:: No Response");
				}

			} catch (Exception excption) {
				excption.printStackTrace();
			}
		}
		
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			// TCPEchoServer.logger.error("Unexpected exception from downstream",
			// e.getCause());
			// System.out.print(e.getCause());
			e.getChannel().close();
		}
		
		 byte[] getLoginResponse() {
			byte[] respBuffer = new byte[10];
			byte[] tmprespBuffer = new byte[4];

			respBuffer[0] = (byte) (0x78);
			respBuffer[1] = (byte) (0x78);

			// Length:
			respBuffer[2] = (byte) (0x05);
			tmprespBuffer[0] = (byte) (0x05);

			// Profotcol
			respBuffer[3] = (byte) (0x01);
			tmprespBuffer[1] = (byte) (0x01);

			// Serial No
			respBuffer[4] = (byte) (0x00);
			respBuffer[5] = (byte) (0x01);

			tmprespBuffer[2] = (byte) (0x00);
			tmprespBuffer[3] = (byte) (0x01);

			// CRC Check...
			String crc = new Crc16().getCRC(tmprespBuffer);

			respBuffer[6] = (byte) Integer.parseInt(crc.substring(0, 2), 16);
			respBuffer[7] = (byte) Integer.parseInt(crc.substring(2, 4).toString(), 16);

			respBuffer[8] = (byte) 0x0D;
			respBuffer[9] = (byte) 0x0A;

			return respBuffer;
		}

		byte[] getStatuesResponse() {
			byte[] respBuffer = new byte[10];
			byte[] tmprespBuffer = new byte[4];

			respBuffer[0] = (byte) (0x78);
			respBuffer[1] = (byte) (0x78);

			// Length:
			respBuffer[2] = (byte) (0x05);
			tmprespBuffer[0] = (byte) (0x05);

			// Profotcol
			respBuffer[3] = (byte) (0x13);
			tmprespBuffer[1] = (byte) (0x13);

			// Serial No
			respBuffer[4] = (byte) (0x00);
			respBuffer[5] = (byte) (0x01);

			tmprespBuffer[2] = (byte) (0x00);
			tmprespBuffer[3] = (byte) (0x01);

			// CRC Check...
			String crc = new Crc16().getCRC(tmprespBuffer);

			respBuffer[6] = (byte) Integer.parseInt(crc.substring(0, 2), 16);
			respBuffer[7] = (byte) Integer.parseInt(crc.substring(2, 4).toString(), 16);

			respBuffer[8] = (byte) 0x0D;
			respBuffer[9] = (byte) 0x0A;

			return respBuffer;
		}

	public  String printHexBinary(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(hexCode[(b >> 4) & 0xF]);
			r.append(hexCode[(b & 0xF)]);
		}
		return r.toString();
	}




	
	  String getBytsToString(byte[] respBuffer) {
		String response = "";

		for (int i = 0; i < respBuffer.length; i++) {

			response = response + Integer.toString((respBuffer[i] & 0xff) + 0x100, 16).substring(1);
		}

		return response;
	}
}