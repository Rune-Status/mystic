package org.mystic.game.model.networking.packet;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;

public abstract class IncomingPacket {

	public abstract int getMaxDuplicates();

	public abstract void handle(Player paramPlayer, StreamBuffer.InBuffer paramInBuffer, int paramInt1, int paramInt2);
}
