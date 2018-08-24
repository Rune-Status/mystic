package org.mystic.game;

import org.mystic.cache.map.MapLoading;
import org.mystic.cache.map.ObjectDef;
import org.mystic.cache.map.Region;
import org.mystic.game.model.content.Achievements;
import org.mystic.game.model.content.trivia.TriviaBot;
import org.mystic.game.model.entity.item.impl.GlobalItemHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.drops.NPCDrops;
import org.mystic.game.model.entity.object.ObjectConstants;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.utility.GameDefinitionLoader;

public class GameDataLoader {

	/**
	 * Loads all of the game data
	 */
	public static void load() {

		try {
			new Thread() {

				@Override
				public void run() {
					try {
						ObjectDef.loadConfig();
						ObjectConstants.declare();
						MapLoading.load();
						Region.sort();
						ObjectManager.declare();
						GameDefinitionLoader.loadNpcSpawns();
						Npc.spawnBosses();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();

			GlobalItemHandler.spawn();
			TriviaBot.start();
			Achievements.declare();

			GameDefinitionLoader.loadItemValues();
			GameDefinitionLoader.loadNpcDefinitions();
			GameDefinitionLoader.loadItemDefinitions();
			GameDefinitionLoader.loadEquipmentDefinitions();
			GameDefinitionLoader.loadShopDefinitions();
			GameDefinitionLoader.setRequirements();
			GameDefinitionLoader.loadWeaponDefinitions();
			GameDefinitionLoader.loadSpecialAttackDefinitions();
			GameDefinitionLoader.loadRangedStrengthDefinitions();
			GameDefinitionLoader.loadSpecialAttackDefinitions();
			GameDefinitionLoader.loadCombatSpellDefinitions();
			GameDefinitionLoader.loadRangedWeaponDefinitions();
			GameDefinitionLoader.loadNpcCombatDefinitions();
			GameDefinitionLoader.loadItemBonusDefinitions();

			NPCDrops.parseDrops().load();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}