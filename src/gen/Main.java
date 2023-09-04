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
		new SetBuilder(SetLoader.loadSet("base"), currentPath.resolve("build"), currentPath.resolve("img")).buildFaces();
		new SetBuilder(SetLoader.loadSet("Ambition"), currentPath.resolve("build"), currentPath.resolve("img")).buildFaces();
		new SetBuilder(SetLoader.loadSet("Rivalry"), currentPath.resolve("build"), currentPath.resolve("img")).buildFaces();
		new SetBuilder(SetLoader.loadSet("Promo"), currentPath.resolve("build"), currentPath.resolve("img")).buildFaces();
		
//		Path imgPath = Paths.get("D:\\Projects\\Roll for the Galaxy 2\\imgArchive");
//		ImageGenerator gen = new ImageGenerator();
//		gen.generateImage("the landscape of a prosperous planet covered in fruit orchards, colorful, light blue, sci-fi, digital art", imgPath);
	}
}
