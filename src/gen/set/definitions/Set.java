package gen.set.definitions;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "set")
@XmlAccessorType(XmlAccessType.NONE)
public class Set {

	@XmlAttribute(name = "name")
	private String name;

	@XmlElement(name = "card")
	private List<Card> cards = new ArrayList<>();

	public String getName() {
		return name;
	}

	public List<Card> getCards() {
		return cards;
	}
}
