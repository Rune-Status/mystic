package org.mystic.game.model.content;

// package org.exigence.model.content;
//
// import org.exigence.model.entity.player.Player;
//
// public class QuickCurses {
//
// public static final int MAX_CURSES = 20;
//
// private static final int[][] data = { { 67050, 630, 0 }, { 67051, 631, 1 }, {
// 67052, 632, 2 }, { 67053, 633, 3 },
// { 67054, 634, 4 }, { 67055, 635, 5 }, { 67056, 636, 6 }, { 67057, 637, 7 }, {
// 67058, 638, 8 },
// { 67059, 639, 9 }, { 67060, 640, 10 }, { 67061, 641, 11 }, { 67062, 642, 12
// }, { 67063, 643, 13 },
// { 67064, 644, 14 }, { 67065, 645, 15 }, { 67066, 646, 16 }, { 67067, 647, 17
// }, { 67068, 648, 18 },
// { 67069, 649, 19 } };
//
// private static int[] curseIds = { 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
// 39, 40, 41, 42, 43, 44, 45, 26, 27 };
//
// public static void clickCurse(Player player, int actionId) {
// canBeSelected(player, actionId);
// for (int j = 0; j < data.length; j++) {
// if (data[j][0] == actionId) {
// if (player.quickCurses[data[j][2]]) {
// player.quickCurses[data[j][2]] = false;
// player.getActionSender().sendFrame36(data[j][1], 0);
// } else {
// player.quickCurses[data[j][2]] = true;
// player.getActionSender().sendFrame36(data[j][1], 1);
// }
// }
// }
// }
//
// public static void loadCheckMarks(Player player) {
// for (int j = 0; j < data.length; j++)
// player.getActionSender().sendFrame36(data[j][1],
// player.quickCurses[data[j][2]] ? 1 : 0);
// }
//
// public static void canBeSelected(Player player, int actionId) {
// boolean[] curse = new boolean[MAX_CURSES];
// for (int j = 0; j < curse.length; j++) {
// curse[j] = true;
// }
// switch (actionId) {
//
// case 67051:
// curse[10] = false;
// curse[19] = false;
// break;
//
// case 67052:
// curse[11] = false;
// curse[19] = false;
// break;
//
// case 67053:
// curse[12] = false;
// curse[19] = false;
// break;
//
// case 67054:
// curse[16] = false;
// break;
//
// case 67057:
// curse[8] = false;
// curse[9] = false;
// curse[17] = false;
// curse[18] = false;
// break;
//
// case 67058:
// curse[7] = false;
// curse[9] = false;
// curse[17] = false;
// curse[18] = false;
// break;
//
// case 67059:
// curse[7] = false;
// curse[8] = false;
// curse[17] = false;
// curse[18] = false;
// break;
//
// case 67060:
// curse[1] = false;
// curse[19] = false;
// break;
//
// case 67061:
// curse[2] = false;
// curse[19] = false;
// break;
//
// case 67062:
// curse[3] = false;
// curse[19] = false;
// break;
//
// case 67063:
// case 67064:
// case 67065: // Leech run-energy
// curse[19] = false;
// break;
//
// case 67066: // Leech special
// curse[4] = false;
// curse[19] = false;
// break;
//
// case 67067:
// for (int i = 7; i < 10; i++)
// curse[i] = false;
// curse[18] = false;
// break;
//
// case 67068:
// for (int i = 7; i < 10; i++)
// curse[i] = false;
// curse[17] = false;
// break;
//
// case 67069:
// for (int i = 1; i < 5; i++) {
// for (int j = 10; j < 17; j++) {
// curse[i] = false;
// curse[j] = false;
// }
// }
// break;
// }
// for (int i = 0; i < MAX_CURSES; i++) {
// if (!curse[i]) {
// player.quickCurses[i] = false;
// for (int j = 0; j < data.length; j++) {
// if (i == data[j][2])
// player.getActionSender().sendFrame36(data[j][1], 0);
// }
// }
// }
// }
//
// public static void turnOnQuicks(Player player) {
// if (player.getDuel().getDuelRules()[7]) {
// for (int p = 0; p < Config.PRAYER.length; p++) {
// player.prayerActive[p] = false;
// player.getActionSender().sendFrame36(Config.PRAYER_GLOW[p], 0);
// }
// player.getActionSender().sendMessage("Prayer has been disabled in this
// duel!");
// return;
// }
// if (!player.cursesOn) {
// for (int i = 0; i < player.quickPrayers.length; i++) {
// if (player.quickPrayers[i] && !player.prayerActive[i]) {
// CombatAssistant.activatePrayer(player, i);
// }
// if (!player.quickPrayers[i] && player.prayerActive[i]) {
// CombatAssistant.deactivatePrayer(player, i);
// }
// }
// } else {
// for (int i = 0; i < player.quickCurses.length; i++) {
// if (player.quickCurses[i] && !player.prayerActive[curseIds[i]]) {
// CombatAssistant.activateCurse(player, curseIds[i]);
// }
// if (!player.quickCurses[i] && player.prayerActive[curseIds[i]]) {
// CombatAssistant.deactivatePrayer(player, curseIds[i]);
// }
// }
// }
// for (int i = 0; i < player.prayerActive.length; i++) {
// if (player.cursesOn && i >= 20) {
// if (player.headIcon >= 0) {
// for (int i2 = 0; i2 < Config.PRAYER_HEAD_ICONS.length; i2++) {
// if (player.headIcon == Config.PRAYER_HEAD_ICONS[i2]) {
// if (!player.prayerActive[i2]) {
// player.headIcon = -1;
// player.getPA().requestUpdates();
// }
// break;
// }
// }
// }
// return;
// }
// if (player.prayerActive[player.cursesOn ? curseIds[i] : i]) {
// if (player.cursesOn)
// player.quickCurse = player.quickPrayersOn = true;
// else
// player.quickPray = player.quickPrayersOn = true;
// player.getActionSender().sendString(":quicks:on", -1);
// if (player.headIcon >= 0) {
// for (int i2 = 0; i2 < Config.PRAYER_HEAD_ICONS.length; i2++) {
// if (player.headIcon == Config.PRAYER_HEAD_ICONS[i2]) {
// if (!player.prayerActive[i2]) {
// player.headIcon = -1;
// player.getPA().requestUpdates();
// }
// break;
// }
// }
// }
// return;
// }
// }
// CombatAssistant.deactivateAllPrayers(player);
// player.getActionSender().sendString(":quicks:off", -1);
// }
//
// public static void turnOffQuicks(Player player) {
// CombatAssistant.deactivateAllPrayers(player);
// player.getActionSender().sendString(":quicks:off", -1);
// player.quickPray = false;
// player.quickCurse = false;
// player.headIcon = -1;
// player.quickPrayersOn = false;
// }
//
// public static void selectQuickInterface(Player player) {
// if (player.cursesOn == false) {
// QuickPrayers.loadCheckMarks(player);
// } else {
// loadCheckMarks(player);
// }
// player.setSidebarInterface(6, player.cursesOn == false ? 17200 : 17234);
// player.getActionSender().sendFrame106(6);
// }
//
// public static void clickConfirm(Player player) {
// player.setSidebarInterface(6, player.cursesOn ? 21356 : 5608);
// }
// }
// }
