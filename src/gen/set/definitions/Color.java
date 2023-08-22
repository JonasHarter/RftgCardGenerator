package gen.set.definitions;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Dice/Cost colours
 */
public enum Color {

	WHITE("white", "000000"), BLACK("black", "FFFFFF"), GREY("grey", "C0C0C0"), YELLOW("yellow", "FFFF00"),
	BROWN("brown", "994C00"), RED("red", "FF0000"), GREEN("green", "00FF00"), LIGHT_BLUE("lightBlue", "00FFFF"),
	DARK_BLUE("darkBlue", "0000FF"), ORANGE("orange", "FF8000"), VIOLETT("violett", "DB7093");

	private String value;
	private String colorHex;

	private Color(String value, String colorHex) {
		this.value = value;
		this.colorHex = "#" + colorHex;
	}

	public String getValue() {
		return value;
	}

	public String getColorHex() {
		return colorHex;
	}

	public static class ColorAdapter extends XmlAdapter<String, Color> {

		@Override
		public Color unmarshal(String v) throws Exception {
			for (Color colour : Color.values()) {
				if (v.equals(colour.getValue()))
					return colour;
			}
			throw new RuntimeException("Failed to find Colour: " + v);
		}

		@Override
		public String marshal(Color v) throws Exception {
			return v.getValue();
		}

	}
}
