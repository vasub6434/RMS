package com.bonrix.dggenraterset.Service;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.TcpServer.AtlantaWP30CServer;
import com.bonrix.dggenraterset.TcpServer.CV2EnergyMeterDevice;
import com.bonrix.dggenraterset.TcpServer.CV2EnergyMeterDeviceError;
import com.bonrix.dggenraterset.TcpServer.CV2NewDataTest;
import com.bonrix.dggenraterset.TcpServer.ECONTABEnergyMeterServer;
import com.bonrix.dggenraterset.TcpServer.EmiotsServer;
import com.bonrix.dggenraterset.TcpServer.EnergyMeterServer;
import com.bonrix.dggenraterset.TcpServer.EnergyMeterServer1252;
import com.bonrix.dggenraterset.TcpServer.GT06Server;
import com.bonrix.dggenraterset.TcpServer.GTLEnergyMeterDevice;
import com.bonrix.dggenraterset.TcpServer.GTLPowerAndDG;
import com.bonrix.dggenraterset.TcpServer.LiBattry1255;
import com.bonrix.dggenraterset.TcpServer.MODBUSRTUEnergyMeter;
import com.bonrix.dggenraterset.TcpServer.ModeBusEnergyMeterServer;
import com.bonrix.dggenraterset.TcpServer.Radient;
import com.bonrix.dggenraterset.TcpServer.TK103ServerNew;
import com.bonrix.dggenraterset.TcpServer.Testserver;
import com.bonrix.dggenraterset.TcpServer.WP30C1256;
import com.bonrix.dggenraterset.TcpServer.WP30CRS485;

@Service("ListenerServices")
public class ListenerServicesImp implements ListenerServices {
  private Logger log = Logger.getLogger(ListenerServicesImp.class);
  
/*  @Async
  public void startGT06(String ipaddress, int port) {
    this.log.info("Starting GT06");
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder(CharsetUtil.ISO_8859_1));
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder(CharsetUtil.ISO_8859_1));
          pipeline.addLast("handler", (ChannelHandler)new GT06Server());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
  public void startTk103(String ipaddress, int port) {
    this.log.info("Bnrix Starting Tk103");
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new TK103ServerNew.HandlerTk103New());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
  public void test(String ipaddress, int port) {
    this.log.info("Starting Test");
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new Testserver.HandlerTest());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
  public void startEnergyMeterServer(String ipaddress, int port) {
    this.log.info("Starting EnergyMeterServer On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new EnergyMeterServer.HandlerEnergyMeter());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
  public void radient(String ipaddress, int port) {
    this.log.info("RADIENT Starting Radient On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new Radient.HandlerRadient());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
  public void EmiotsServer(String ipaddress, int port) {
    this.log.info("EmiotsServer Starting Radient On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new EmiotsServer.HandlerEmiotsServer());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void AtlantaWP30CServer(String ipaddress, int port) {
    this.log.info("AtlantaWP30C Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new AtlantaWP30CServer.HandleAtlantaWP30C());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
  public void StartMODBUS(String ipaddress, int port) {
    this.log.info("MODBUSRTU Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new MODBUSRTUEnergyMeter.HandleMODBUSRTUEnergyMeter());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartWP30CRS485(String ipaddress, int port) {
    this.log.info("WP30CRS485 Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new WP30CRS485.HandlerWP30CRS485());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartICERDG(String ipaddress, int port) {
    this.log.info("ICERDG Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new ECONTABEnergyMeterServer.HandlerECONTAB());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartCV2EnergyMeterDevice(String ipaddress, int port) {
    this.log.info("CV2EnergyMeterDevice Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new CV2EnergyMeterDevice.HandlerCV2EnergyMeterDevice());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartWP30C1256(String ipaddress, int port) {
    this.log.info("WP30C1256 Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new WP30C1256.HandlerWP30C1256());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartEnergyMeterServer1252(String ipaddress, int port) {
    this.log.info("StartEnergyMeterServer1252 Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new EnergyMeterServer1252.HandlerEnergyMeterServer1252());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartCV2NewDataTest1233(String ipaddress, int port) {
    this.log.info("StartCV2NewDataTest1233 Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new CV2NewDataTest.HandlerCV2NewDataTest());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartGTLEnergyMeterDevice5113(String ipaddress, int port) {
    this.log.info("StartGTLEnergyMeterDevice5113 Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new GTLEnergyMeterDevice.HandlerGTLEnergyMeterDevice());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  public void StartGTLGTLPowerAndDGEnergyMeterDevice5114(String ipaddress, int port) {
    this.log.info("StartGTLGTLPowerAndDGEnergyMeterDevice5114 Starting  On IP " + ipaddress + " Port " + port);
    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    bootstrap.setPipelineFactory(() -> {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
          pipeline.addLast("handler", (ChannelHandler)new GTLPowerAndDG.HandlerGTLPowerAndDG());
          return pipeline;
        });
    bootstrap.bind(new InetSocketAddress(ipaddress, port));
  }
  
  @Async
public void StartLiBattry1255(String ipaddress, int port) {
	    this.log.info("StartLiBattry1255 Starting  On IP " + ipaddress + " Port " + port);
	    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
	          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	    bootstrap.setPipelineFactory(() -> {
	          ChannelPipeline pipeline = Channels.pipeline();
	          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
	          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());
	          pipeline.addLast("handler", (ChannelHandler)new LiBattry1255.HandlerLiBattry1255());
	          return pipeline;
	        });
	    bootstrap.bind(new InetSocketAddress(ipaddress, port));
	  }

  @Async
  public void StartCV2EnergyMeterDeviceError(String ipaddress, int port) {
	  this.log.info("StartCV2EnergyMeterDeviceError Starting  On IP " + ipaddress + " Port " + port);
	    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
	          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	    bootstrap.setPipelineFactory(() -> {
	          ChannelPipeline pipeline = Channels.pipeline();
	          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
	          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());  
	          pipeline.addLast("handler", (ChannelHandler)new CV2EnergyMeterDeviceError.HandlerCV2EnergyMeterDeviceError());
	          return pipeline;
	        });
	    bootstrap.bind(new InetSocketAddress(ipaddress, port));
	
}

  @Async
public void StartModeBusBEnergyMeterServer(String ipaddress, int port) {
	  this.log.info("StartModeBusBEnergyMeterServer Starting  On IP " + ipaddress + " Port " + port);
	    ServerBootstrap bootstrap = new ServerBootstrap((ChannelFactory)new NioServerSocketChannelFactory(
	          Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	    bootstrap.setPipelineFactory(() -> {
	          ChannelPipeline pipeline = Channels.pipeline();
	          pipeline.addLast("decoder", (ChannelHandler)new StringDecoder());
	          pipeline.addLast("encoder", (ChannelHandler)new StringEncoder());  
	          pipeline.addLast("handler", (ChannelHandler)new ModeBusEnergyMeterServer.HandleModeBusBEnergyMeterServer());
	          return pipeline;
	        });
	    bootstrap.bind(new InetSocketAddress(ipaddress, port));
	
}*/
}
