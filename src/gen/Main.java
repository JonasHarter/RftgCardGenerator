package gen;

import java.nio.file.Paths;

import gen.set.SetBuilder;
import gen.set.SetLoader;
import gen.set.definitions.Set;

public class Main {
	public static void main(String[] args) throws Exception {
		Set base = SetLoader.loadSet("base");
		SetBuilder x = new SetBuilder(base, Paths.get("D:\\Projects\\Roll for the Galaxy 2\\RftgCardGenerator\\build"));
		x.buildFaces();
	}
}
