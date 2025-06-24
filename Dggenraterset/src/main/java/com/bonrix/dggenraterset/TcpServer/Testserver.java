package com.bonrix.dggenraterset.TcpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;


public class Testserver {

	public static class HandlerTest extends SimpleChannelUpstreamHandler {
		private Logger log = Logger.getLogger(HandlerTest.class);
	

		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception{
			
			System.out.println((String) e.getMessage());
		}

		

		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			log.info("SAM::: Chanel Close" + e.getCause());
			e.getChannel().close();
		}
	}

	public static void main(String[] args) {

		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("handler", new Testserver.HandlerTest());
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(8080));
	}



}
