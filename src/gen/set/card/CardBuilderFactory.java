package gen.set.card;

import gen.set.FileGeneratorData;
import gen.set.definitions.Card;
import gen.set.definitions.CardType;

public class CardBuilderFactory {

	public static CardBuilder buildCardBuilder(Card card, FileGeneratorData fileGeneratorData) {
		if (CardType.STARTER_DOUBLE.equals(card.getCardType())) {
			return new DoubleSizeCardBuilder(card, fileGeneratorData);
		}
		return new CardBuilder(card, fileGeneratorData);
	}
}
