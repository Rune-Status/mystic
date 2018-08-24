package org.mystic.game.model.content.trivia;

public enum Trivia {

	DATA_4("The barrows brother Verac hits through which prayer?", "Protect from melee", "melee", "protect melee"),

	DATA_5("How many Owners are there of Mystic?", "3", "three"),

	DATA_8("I am a rock that turns into a crab what am i called?", "Rock crab", "Rock crabs"),

	DATA_9("What is the max level possible to be reached in any skill?", "99"),

	DATA_15("What is the name of the NPC that drops Wyvern bones?", "skeletal Wyvern", "skeletal", "wyverns"),

	DATA_16("How many colors of infinity are there?", "3", "Three"),

	DATA_19("Which of the barrows warriors is based on magic?", "Ahrim", "Ahrim"),

	DATA_20("What is the required fishing level for sharks?", "76", "76"),

	DATA_21("What is the smithing level required to create a DFS?", "90"),

	DATA_23("How many waves is fight caves in Mystic?", "24"),

	DATA_25("How much special attack does Magic Short Bow require?", "55%", "55"),

	DATA_26("Berserker ring is dropped by which NPC?", "Dagannoth King Rex", "Dagannoth Rex", "Rex"),

	DATA_30("Where are spiritual mages found?", "Godwars", "Godwars dungeon"),

	DATA_35("What is the required level for Smite?", "52"),

	DATA_39("How many bank booths are in edgeville bank?", "4", "four"),

	DATA_40("What slayer level is required to an Abyssal demon?", "85", "85"),

	DATA_41("What is the required attack level to wield a godsword?", "75"),

	DATA_42("How many barrows brothers are there?", "6"),

	DATA_43("Type this backwards; Mysticrocks", "skcorcitsym"),

	DATA_44("How much does Gold-membership cost?", "$10", "10 dollars", "10 dollar"),

	DATA_53("What is the maximum amount of friends allowed?", "200", "two hundred", "two-hundred"),

	DATA_54("What is the Woodcutting level required to wield a dragon hatchet?", "61", "sixty one", "sixty-one"),

	DATA_56("Where is home in Mystic", "edgeville", "edge"),

	DATA_58("What color party hat does the wise old man wear?", "blue"),

	DATA_60("What skill involves buring logs?", "firemaking"),

	DATA_61("What is the required defence level to wear dragon armour?", "60", "sixty"),

	DATA_62("What NPC allows you to reset combat stats in Mystic?", "combat master"),

	DATA_63("What minigame offers void armour as a reward?", "pest control"),

	DATA_64("What NPC can you talk to if you want to claim a donation?", "donation manager", "donations manager"),

	DATA_65("How many Thieving stalls are there at home?", "5", "five"),

	DATA_67("What is the best F2P armour?", "rune", "rune armour"),

	DATA_68("How much money does Gold-membership cost?", "$10", "10 dollars"),

	DATA_71("How many letters are in the word 'Mystic'?", "6", "six"),

	DATA_72("What level magic does the spell High Alchemy require?", "55", "fifty-five", "fifty five");

	private final String question;
	private final String[] answers;

	private Trivia(String question, String... answers) {
		this.question = question;
		this.answers = answers;
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

}