package gen.set.definitions;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public enum CardType {

	STARTER_DOUBLE("double");

	private String value;

	private CardType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static class CardTypeXmlAdapter extends XmlAdapter<String, CardType> {

		@Override
		public CardType unmarshal(String v) throws Exception {
			for (CardType cardType : CardType.values()) {
				if (v.equals(cardType.getValue()))
					return cardType;
			}
			throw new RuntimeException("Failed to find CardType: " + v);
		}

		@Override
		public String marshal(CardType v) throws Exception {
			return v.getValue();
		}

	}

}
