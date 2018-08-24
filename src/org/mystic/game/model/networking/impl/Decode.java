package org.mystic.game.model.networking.impl;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.mystic.game.model.networking.ISAACCipher;
import org.mystic.game.model.networking.ReceivedPacket;
import org.mystic.utility.Misc;

/**
 * 
 * @author Graham Edgecombe
 * @author Stuart Murphy
 * 
 */
public class Decode extends FrameDecoder {

	private final ISAACCipher cipher;
	private int opcode = -1;
	private int size = -1;

	public Decode(ISAACCipher cipher) {
		this.cipher = cipher;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (opcode == -1) {
			if (buffer.readableBytes() >= 1) {
				opcode = buffer.readByte() & 0xFF;
				opcode = (opcode - cipher.getNextValue()) & 0xFF;
				size = Misc.packetLengths[opcode];
			} else {
				return null;
			}
		}
		if (size == -1) {
			if (buffer.readableBytes() >= 1) {
				size = buffer.readByte() & 0xFF;
			} else {
				return null;
			}
		}
		if (buffer.readableBytes() >= size) {
			byte[] data = new byte[size];
			buffer.readBytes(data);
			ChannelBuffer payload = ChannelBuffers.buffer(size);
			payload.writeBytes(data);
			try {
				return new ReceivedPacket(opcode, size, payload);
			} finally {
				opcode = -1;
				size = -1;
			}
		}
		return null;
	}
}
