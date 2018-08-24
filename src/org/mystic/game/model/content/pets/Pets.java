package org.mystic.game.model.content.pets;

import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;

public class Pets {

	public class Pet {

		private Npc mob = null;

		private byte stage = 0;

		private long start = System.currentTimeMillis();

		public Pet(Npc mob, byte stage, byte hunger) {
			this.mob = mob;
			this.stage = stage;
		}

		public Npc getMob() {
			return mob;
		}
	}

	public static final void chat(Npc mob) {
		String[] messages = PetData.getPetDataForMob(mob.getId()).messages;
		if (messages != null) {
			mob.getUpdateFlags().sendForceMessage(messages[org.mystic.utility.Misc.randomNumber(messages.length)]);
		}
	}

	public static String[] getPetChat(int id) {
		return PetData.getPetDataForMob(id).messages;
	}

	public static final boolean isItemPet(int id) {
		return PetData.getPetDataForItem(id) != null;
	}

	public static final boolean isMobPet(int id) {
		return PetData.getPetDataForMob(id) != null;
	}

	private Player player;

	private Pet pet = null;

	public Pets(Player player) {
		this.player = player;
	}

	public void checkForGrowth() {
		if (pet == null) {
			return;
		}
		if (System.currentTimeMillis() - pet.start >= 3600000L) {
			grow(false);
			pet.start = System.currentTimeMillis();
		}
	}

	public Pet getPet() {
		return pet;
	}

	public void grow(boolean overgrow) {
		PetData.PetStage stage = PetData.getPetDataForMob(pet.mob.getId());
		if ((stage.next == null) || ((!overgrow) && (stage.petType.overgrows) && (pet.stage == 1))) {
			return;
		}
		pet.mob.transform(stage.next.npcId);
		pet.start = System.currentTimeMillis();
		Pet tmp97_94 = pet;
		tmp97_94.stage = ((byte) (tmp97_94.stage + 1));
	}

	public boolean hasPet() {
		return pet != null;
	}

	public void init(int id) {
		PetData.PetStage data = PetData.getPetDataForItem(id);
		if (data == null) {
			return;
		}
		if ((pet != null) || (player.getSummoning().hasFamiliar())) {
			player.getClient().queueOutgoingPacket(new SendMessage("You already have a follower."));
			return;
		}
		player.getInventory().remove(new Item(id, 1));
		final Npc mob = new Npc(player, data.npcId, false, false, true, player.getLocation());
		pet = new Pet(mob, data.stage, (byte) 0);
		mob.getFollowing().setIgnoreDistance(true);
		mob.getFollowing().setFollow(player);
		chat(mob);
		if (mob.getDefinition().getName().toLowerCase().contains("cat")) {
			SoundPlayer.play(player, 1152, 0, 0);
		}
	}

	public boolean remove() {
		if (pet != null) {
			if (System.currentTimeMillis() - pet.start >= 3600000L) {
				grow(false);
				pet.start = System.currentTimeMillis();
			}
			final Item item = new Item(PetData.getPetDataForMob(pet.mob.getId()).itemId, 1);
			if (!player.getInventory().hasSpaceFor(item)) {
				if (player.getBank().hasSpaceFor(item)) {
					player.getBank().add(item);
					player.getClient().queueOutgoingPacket(new SendMessage("Your pet has been added to your bank."));
				} else {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You must free some inventory space to pick up your pet."));
					return false;
				}
			} else {
				player.face(pet.getMob());
				player.getInventory().add(item);
				player.send(new SendMessage("You pick up your pet."));
				player.getUpdateFlags().sendAnimation(new Animation(827));
				pet.mob.remove();
				pet = null;
			}
		}
		return true;
	}

	public void spawn(int id) {
		PetData.PetStage data = PetData.getPetDataForItem(id);
		if (data == null) {
			return;
		}
		final Npc mob = new Npc(player, data.npcId, false, false, true, player.getLocation());
		mob.getFollowing().setIgnoreDistance(true);
		mob.getFollowing().setFollow(player);
		chat(mob);
		pet = new Pet(mob, data.stage, (byte) 0);
		TaskQueue.queue(new Task(mob, 100) {
			@Override
			public void execute() {
				if (pet == null) {
					stop();
					return;
				}
				Pets.chat(mob);
			}

			@Override
			public void onStop() {
			}
		});
	}
}