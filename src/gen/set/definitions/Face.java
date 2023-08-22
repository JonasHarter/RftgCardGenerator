package gen.set.definitions;

import java.util.List;

import gen.set.definitions.Color.ColorAdapter;
import gen.set.definitions.Phase.PhaseAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlList;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "face")
@XmlAccessorType(XmlAccessType.NONE)
public class Face {

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "nameOverwrite")
	private String nameOverwrite;

	@XmlElement(name = "cost")
	private Integer cost;

	@XmlElement(name = "color")
	@XmlJavaTypeAdapter(ColorAdapter.class)
	private Color color;

	@XmlElement(name = "phase")
	@XmlList
	@XmlJavaTypeAdapter(PhaseAdapter.class)
	private List<Phase> phase;

	@XmlElement(name = "text")
	private String text;

	@XmlElement(name = "textSp")
	private String textSp;

	public String getName() {
		if(nameOverwrite != null)
			return nameOverwrite;
		return name;
	}

	public Integer getCost() {
		return cost;
	}

	public Color getColor() {
		return color;
	}

	public List<Phase> getPhase() {
		return phase;
	}

	public String getText() {
		return text;
	}

	public String getTextSp() {
		return textSp;
	}

}