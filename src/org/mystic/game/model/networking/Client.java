package org.mystic.game.model.networking;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.mystic.game.World;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.out.OutgoingPacket;
import org.mystic.game.model.networking.packet.PacketHandler;
import org.mystic.utility.Misc;
import org.mystic.utility.Misc.Stopwatch;

/**
 * The class behind a Player that handles all networking-related things.
 * 
 * @author blakeman8192
 */
public class Client {

	public enum Stages {
		CONNECTED, LOGGING_IN, LOGGED_IN, LOGGED_OUT
	}

	private Channel channel;

	private final List<Npc> mobs = new LinkedList<Npc>();

	private Queue<ReceivedPacket> incomingPackets = new ConcurrentLinkedQueue<ReceivedPacket>();
	private Queue<OutgoingPacket> outgoingPackets = new ConcurrentLinkedQueue<OutgoingPacket>();

	private Stages stage = Stages.LOGGING_IN;
	private final Misc.Stopwatch timeoutStopwatch = new Misc.Stopwatch();

	private ISAACCipher encryptor;
	private ISAACCipher decryptor;

	private Player player;
	private PacketHandler packetHandler;
	private String host;

	private long hostId = 0;

	private boolean logPlayer = false;

	private String enteredPassword = null;

	private String lastPlayerOption = "";

	private long lastPacketTime = World.getCycles();

	public Client(Channel channel) {
		try {
			this.channel = channel;
			if (channel != null) {
				this.host = channel.getRemoteAddress().toString();
				this.host = host.substring(1, host.indexOf(":"));
				this.hostId = Misc.nameToLong(host);
			} else {
				this.host = "none";
				this.hostId = -1;
			}
			this.player = new Player(this);
			this.packetHandler = new PacketHandler(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		if (outgoingPackets != null) {
			outgoingPackets = null;
		}
	}

	public ISAACCipher getDecryptor() {
		return decryptor;
	}

	public ISAACCipher getEncryptor() {
		return encryptor;
	}

	public String getEnteredPassword() {
		return enteredPassword;
	}

	public String getHost() {
		return host;
	}

	public long getHostId() {
		return hostId;
	}

	public long getLastPacketTime() {
		return lastPacketTime;
	}

	public String getLastPlayerOption() {
		return lastPlayerOption;
	}

	public List<Npc> getNpcs() {
		return mobs;
	}

	public Queue<OutgoingPacket> getOutgoingPackets() {
		return outgoingPackets;
	}

	public Player getPlayer() {
		return player;
	}

	public Stages getStage() {
		return stage;
	}

	public Stopwatch getTimeoutStopwatch() {
		return timeoutStopwatch;
	}

	public boolean isLogPlayer() {
		return logPlayer;
	}

	/**
	 * Handles packets we have received
	 */
	public void processIncomingPackets() {
		ReceivedPacket p = null;
		try {
			if (outgoingPackets == null) {
				return;
			}
			if (outgoingPackets == null) {
				return;
			}
			while ((p = incomingPackets.poll()) != null) {
				packetHandler.handlePacket(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles packets we are sending
	 */
	public void processOutgoingPackets() {
		if (channel == null || outgoingPackets == null) {
			return;
		}
		try {
			if (channel == null) {
				return;
			}
			if (outgoingPackets == null) {
				return;
			}
			OutgoingPacket p = null;
			while ((p = outgoingPackets.poll()) != null) {
				p.execute(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a packet to the queue
	 * 
	 * @param packet
	 */
	public void queueIncomingPacket(ReceivedPacket packet) {
		resetLastPacketReceived();
		incomingPackets.offer(packet);
	}

	/**
	 * Adds a packet to the outgoing queue
	 * 
	 * @param o
	 *            the OutGoingPacket object
	 */
	public void queueOutgoingPacket(OutgoingPacket o) {
		if (outgoingPackets == null) {
			return;
		}
		if (outgoingPackets == null) {
			return;
		}
		outgoingPackets.offer(o);
	}

	/**
	 * Resets the packet handler
	 */
	public void reset() {
		packetHandler.reset();
	}

	public void resetLastPacketReceived() {
		lastPacketTime = World.getCycles();
	}

	/**
	 * Sends the buffer to the socket.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public void send(ChannelBuffer buffer) {
		try {
			if (channel == null || !channel.isConnected()) {
				return;
			}
			channel.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the decryptor.
	 * 
	 * @param decryptor
	 *            the decryptor.
	 */
	public void setDecryptor(ISAACCipher decryptor) {
		this.decryptor = decryptor;
	}

	/**
	 * Sets the encryptor.
	 * 
	 * @param encryptor
	 *            the encryptor
	 */
	public void setEncryptor(ISAACCipher encryptor) {
		this.encryptor = encryptor;
	}

	public void setEnteredPassword(String enteredPassword) {
		this.enteredPassword = enteredPassword;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setLastPlayerOption(String lastPlayerOption) {
		this.lastPlayerOption = lastPlayerOption;
	}

	public void setLogPlayer(boolean logPlayer) {
		this.logPlayer = logPlayer;
	}

	public void setStage(Stages stage) {
		this.stage = stage;
	}

}