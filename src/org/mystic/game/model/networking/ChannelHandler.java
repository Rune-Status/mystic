package org.mystic.game.model.networking;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.mystic.LoginThread;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class ChannelHandler extends SimpleChannelHandler {

	private Client client = null;

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (client != null) {
			client.disconnect();
			client = null;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		ctx.getChannel().close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			if (!e.getChannel().isConnected()) {
				return;
			} else if (e.getMessage() instanceof Client) {
				client = (Client) e.getMessage();
				if (!ClientMap.allow(client)) {
					StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
					resp.writeByte(Misc.LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED);
					resp.writeByte(0);
					resp.writeByte(0);
					client.send(resp.getBuffer());
					ctx.getChannel().close();
				} else {
					final Player p = client.getPlayer();
					LoginThread.queueLogin(p);
				}
			} else if (e.getMessage() instanceof ReceivedPacket) {
				client.queueIncomingPacket((ReceivedPacket) e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
