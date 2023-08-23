package gen;

import java.nio.file.Path;
import java.nio.file.Paths;

import gen.set.SetBuilder;
import gen.set.SetImageGenerator;
import gen.set.SetLoader;
import gen.set.definitions.Set;

public class Main {
	public static void main(String[] args) throws Exception {
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		Set base = SetLoader.loadSet("base");
		SetImageGenerator generator = new SetImageGenerator(base, currentPath.resolve("img"));
		generator.generateImages();
		SetBuilder builder = new SetBuilder(base, currentPath.resolve("build"), currentPath.resolve("img"));
		builder.buildFaces();
	}
}
