package org.mystic.game.model.networking.impl;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class Encode extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext chc, Channel chan, Object in) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) in;
		return buffer;
	}
}
