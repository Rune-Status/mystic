package org.mystic.mysql;

// package core.game;
//
// import com.rspserver.motivote.MotivoteHandler;
// import com.rspserver.motivote.Reward;
//
// import core.game.content.QuestTab;
// import core.game.entity.World;
// import core.game.entity.player.Player;
// import core.game.entity.player.net.out.impl.SendMessage;
//
// public class VoteRewarder extends MotivoteHandler<Reward> {
//
// @Override
// public void onCompletion(Reward reward) {
// Player player = World.getPlayerByName(reward.username());
// if (player != null && player.isActive() && reward != null) {
// synchronized (player) {
// player.setVotePoints(player.getVotePoints() + reward.amount());
// player.send(new SendMessage("You have successfuly voted and have been
// rewarded!"));
// player.send(new SendMessage(
// "You now have a total of @dre@" + player.getVotePoints() + "@bla@ Vote
// Points."));
// player.send(
// new SendMessage("These points may be spent with the vote rewarder npc located
// at Neitiznot."));
// World.sendGlobalMessage("@blu@[Vote] -> Player: " + "'" +
// player.getUsername() + "' "
// + "@blu@has voted and has been rewarded.", false);
// QuestTab.update(player);
// reward.complete();
// }
// }
// }
// }