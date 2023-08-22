package gen.set.definitions;

/**
 * Dice Name x Colour
 */
public enum Dice {

	HOME("Heimat", Color.WHITE), MILITARY("Militär", Color.RED), CONSUME("Konsum", Color.VIOLETT),
	RARE_EARTH("SelteneElemente", Color.BROWN), LUXURY("Luxusartikel", Color.LIGHT_BLUE), GEN("Gen", Color.GREEN),
	ALIEN("AlienTechnologie", Color.YELLOW), LEADER("Anführer", Color.BLACK), FOUNDER("Gründer", Color.ORANGE),
	PIONEER("Pionier", Color.DARK_BLUE);

	private String value;
	private Color color;

	private Dice(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public static Dice getByValue(String value) {
		for (Dice dice : values()) {
			if (value.equals(dice.getValue()))
				return dice;
		}
		throw new RuntimeException("Bad value given: " + value);
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

}
