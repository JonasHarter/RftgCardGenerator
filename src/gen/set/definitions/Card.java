package gen.set.definitions;

import java.util.ArrayList;
import java.util.List;

import gen.set.definitions.CardType.CardTypeXmlAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "card")
@XmlAccessorType(XmlAccessType.NONE)
public class Card {

	@XmlAttribute(name = "cardType")
	@XmlJavaTypeAdapter(CardTypeXmlAdapter.class)
	private CardType cardType;

	@XmlElement(name = "face")
	private List<Face> faces = new ArrayList<>();

	public CardType getCardType() {
		return cardType;
	}

	public List<Face> getFaces() {
		return faces;
	}
}
