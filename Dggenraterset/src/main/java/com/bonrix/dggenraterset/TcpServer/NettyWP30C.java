package com.bonrix.dggenraterset.TcpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

@WebListener
public class NettyWP30C implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public NettyWP30C() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	
    	/*
    	  ServerBootstrap bootstrap = new ServerBootstrap(
    		      new NioServerSocketChannelFactory(
    		      Executors.newCachedThreadPool(), 
    		      Executors.newCachedThreadPool()));
    		    bootstrap.setPipelineFactory(new ChannelPipelineFactory()
    		    {
    		      public ChannelPipeline getPipeline()
    		        throws Exception
    		      {
    		        ChannelPipeline pipeline = Channels.pipeline();
    		        pipeline.addLast("decoder", new StringDecoder());
    		        pipeline.addLast("encoder", new StringEncoder());
    		        pipeline.addLast("handler", new WP30CServer.HandlerL100());
    		        return pipeline;
    		      }
    		    });
    		   
    		    bootstrap.bind(new InetSocketAddress(1399));*/
    		    
    }

}
