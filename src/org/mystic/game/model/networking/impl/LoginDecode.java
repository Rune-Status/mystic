package org.mystic.game.model.networking.impl;

import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.ISAACCipher;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.utility.Misc;

/**
 * 
 * @author Stuart Murphy
 * 
 */
public class LoginDecode extends FrameDecoder {

	public static final int CLIENT_VERSION = 46730;

	private static final int CONNECTED = 0;

	private static final int LOGGING_IN = 1;

	private static Client login(Channel channel, ISAACCipher inCipher, ISAACCipher outCipher, int version, String name,
			String pass) {
		if (!name.matches("[A-Za-z0-9 ]+") || name.length() > 12 || name.length() <= 0) {
			sendReturnCode(channel, Misc.LOGIN_RESPONSE_INVALID_CREDENTIALS);
			return null;
		}
		name = name.trim();
		channel.getPipeline().remove("decoder");
		channel.getPipeline().addFirst("decoder", new Decode(inCipher));
		Client client = new Client(channel);
		client.getPlayer().setUsername(name);
		client.getPlayer().setDisplay(name);
		client.getPlayer().setPassword(pass);
		client.setEnteredPassword(pass);
		client.setEncryptor(outCipher);
		return client;
	}

	public static void sendReturnCode(Channel channel, int code) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeByte(code);
		channel.write(out.getBuffer()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}

	private int state = CONNECTED;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer in) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}
		switch (state) {

		case CONNECTED:
			if (in.readableBytes() < 2) {
				return null;
			}
			int request = in.readUnsignedByte();
			if (request == 5) {
				sendReturnCode(channel, Misc.LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN);
				return null;
			}
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			in.readUnsignedByte();
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(17);
			out.writeLong(0);
			out.writeByte(0);
			out.writeLong((new SecureRandom().nextLong() / 2) + (new SecureRandom().nextLong() / 2));
			channel.write(out.getBuffer());
			state = LOGGING_IN;
			break;

		case LOGGING_IN:
			if (in.readableBytes() < 2) {
				return null;
			}
			int loginType = in.readUnsignedByte();
			if (loginType != 16 && loginType != 18) {
				System.out.println("Invalid login type: " + loginType);
				channel.close();
				return null;
			}
			int blockLength = in.readUnsignedByte();
			int loginEncryptSize = blockLength - (36 + 1 + 1 + 2);
			if (loginEncryptSize <= 0) {
				System.out.println("Encrypted packet size zero or negative: " + loginEncryptSize);
				channel.close();
				return null;
			}
			in.readUnsignedByte();
			int clientVersion = in.readUnsignedShort();
			int currentVersion = CLIENT_VERSION;
			if (clientVersion != currentVersion) {
				System.out
						.println("Invalid client version, Received: " + clientVersion + " Expected: " + currentVersion);
				StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
				resp.writeByte(Misc.LOGIN_RESPONSE_UPDATED);
				resp.writeByte(0);
				resp.writeByte(0);
				channel.write(resp.getBuffer());
				channel.close();
				return null;
			}
			in.readByte();
			for (int i = 0; i < 9; i++) {
				in.readInt();
			}
			in.readByte();
			int rsaOpcode = in.readByte();
			if (rsaOpcode != 100) {
				System.err.println("Unable to decode RSA block properly!");
				channel.close();
				return null;
			}
			long clientHalf = in.readLong();
			long serverHalf = in.readLong();
			int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32),
					(int) serverHalf };
			ISAACCipher inCipher = new ISAACCipher(isaacSeed);
			for (int i = 0; i < isaacSeed.length; i++) {
				isaacSeed[i] += 50;
			}
			ISAACCipher outCipher = new ISAACCipher(isaacSeed);
			int version = in.readInt();
			String username = Misc.getRS2String(in).trim();
			String password = Misc.getRS2String(in);
			return login(channel, inCipher, outCipher, version, username, password);
		}
		return null;
	}

}
