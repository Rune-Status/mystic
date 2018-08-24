package org.mystic.game.model.networking.packet;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.ReceivedPacket;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.packet.impl.ChangeAppearancePacket;
import org.mystic.game.model.networking.packet.impl.ChangeRegionPacket;
import org.mystic.game.model.networking.packet.impl.ChatInterfacePacket;
import org.mystic.game.model.networking.packet.impl.ClickButtonPacket;
import org.mystic.game.model.networking.packet.impl.CloseInterfacePacket;
import org.mystic.game.model.networking.packet.impl.CommandPacket;
import org.mystic.game.model.networking.packet.impl.InterfaceAction;
import org.mystic.game.model.networking.packet.impl.ItemActionPacketListener;
import org.mystic.game.model.networking.packet.impl.MovementPacket;
import org.mystic.game.model.networking.packet.impl.NPCPacket;
import org.mystic.game.model.networking.packet.impl.ObjectInteractionPacket;
import org.mystic.game.model.networking.packet.impl.OperateItem;
import org.mystic.game.model.networking.packet.impl.PlayerOptionPacket;
import org.mystic.game.model.networking.packet.impl.PrivateMessagingPacket;
import org.mystic.game.model.networking.packet.impl.PublicChatPacket;
import org.mystic.game.model.networking.packet.impl.ReceiveString;
import org.mystic.game.model.networking.packet.impl.StringInputPacket;

public class PacketHandler {

	private final Client client;
	private final Player player;

	private static final IncomingPacket[] packets = new IncomingPacket[350];
	private final Map<IncomingPacket, Integer> packetMap = new HashMap<IncomingPacket, Integer>();

	private static final IncomingPacket buttonPacket = new ClickButtonPacket();
	private static final IncomingPacket publicChatPacket = new PublicChatPacket();
	private static final IncomingPacket commandPacket = new CommandPacket();
	private static final IncomingPacket movementPacket = new MovementPacket();
	private static final IncomingPacket itemPacket = new ItemActionPacketListener();
	private static final IncomingPacket appearancePacket = new ChangeAppearancePacket();
	private static final IncomingPacket chatInterfacePacket = new ChatInterfacePacket();
	private static final IncomingPacket closeInterfacePacket = new CloseInterfacePacket();
	private static final IncomingPacket npcPacket = new NPCPacket();
	private static final IncomingPacket objectPacket = new ObjectInteractionPacket();
	private static final IncomingPacket playerOptionPacket = new PlayerOptionPacket();
	private static final IncomingPacket p13 = new PrivateMessagingPacket();
	private static final IncomingPacket p14 = new ChangeRegionPacket();

	static {
		packets[232] = new OperateItem();
		packets['\u00B9'] = buttonPacket;
		packets[4] = publicChatPacket;
		packets[103] = commandPacket;
		packets['\u00F8'] = movementPacket;
		packets['\u00A4'] = movementPacket;
		packets[98] = movementPacket;
		packets['\u0091'] = itemPacket;
		packets[41] = itemPacket;
		packets[117] = itemPacket;
		packets[43] = itemPacket;
		packets['\u0081'] = itemPacket;
		packets[122] = itemPacket;
		packets['\u00D6'] = itemPacket;
		packets[87] = itemPacket;
		packets['\u00EC'] = itemPacket;
		packets[16] = itemPacket;
		packets[75] = itemPacket;
		packets[53] = itemPacket;
		packets[25] = itemPacket;
		packets['\u00ED'] = itemPacket;
		packets['\u00B5'] = itemPacket;
		packets['\u00FD'] = itemPacket;

		packets[121] = p14;
		packets[101] = appearancePacket;
		packets[40] = chatInterfacePacket;
		packets['\u0087'] = chatInterfacePacket;
		packets['\u00D0'] = chatInterfacePacket;
		packets['\u0082'] = closeInterfacePacket;

		packets['\u009B'] = npcPacket;
		packets[17] = npcPacket;
		packets[21] = npcPacket;
		packets[18] = npcPacket;
		packets[72] = npcPacket;
		packets['\u0083'] = npcPacket;
		packets[57] = npcPacket;

		packets['\u00C0'] = objectPacket;
		packets['\u0084'] = objectPacket;
		packets['\u00FC'] = objectPacket;
		packets[70] = objectPacket;
		packets['\u00EA'] = objectPacket;
		packets[35] = objectPacket;

		packets['\u0080'] = playerOptionPacket;
		packets[73] = playerOptionPacket;
		packets['\u0080'] = playerOptionPacket;
		packets['\u008B'] = playerOptionPacket;
		packets['\u0088'] = playerOptionPacket;
		packets['\u0099'] = playerOptionPacket;
		packets['\u00F9'] = playerOptionPacket;
		packets[14] = playerOptionPacket;
		packets[39] = playerOptionPacket;

		packets['\u00BC'] = p13;
		packets['\u00D7'] = p13;
		packets['\u0085'] = p13;
		packets[74] = p13;
		packets[126] = p13;
		packets[76] = p13;
		packets[60] = new StringInputPacket();
		packets[127] = new ReceiveString();
		packets[213] = new InterfaceAction();
	}

	public PacketHandler(Client client) {
		this.client = client;
		this.player = client.getPlayer();
	}

	public void handlePacket(ReceivedPacket packet) {
		client.resetLastPacketReceived();
		int opcode = packet.getOpcode();
		int length = packet.getSize();
		StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(packet.getPayload());
		try {
			if (packets[opcode] != null) {
				if (packetMap.get(packets[opcode]) == null) {
					packetMap.put(packets[opcode], Integer.valueOf(0));
				}
				player.setLastAction(System.currentTimeMillis());
				int count = packetMap.get(packets[opcode]) == null ? 0 : packetMap.get(packets[opcode]).intValue();
				if (count < packets[opcode].getMaxDuplicates()) {
					packets[opcode].handle(player, in, opcode, length);
					if (count != 0) {
						packetMap.remove(packets[opcode]);
					}
					packetMap.put(packets[opcode], Integer.valueOf(++count));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void reset() {
		if (packetMap.size() > 0) {
			packetMap.clear();
		}
	}

}