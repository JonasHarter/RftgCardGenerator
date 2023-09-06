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
		Path imagePath = currentPath.resolve("img");
		Path targetPath = currentPath.resolve("build");
		Integer runningInteger = new SetBuilder(SetLoader.loadSet("base"), null, targetPath, imagePath).buildFaces();
		runningInteger = new SetBuilder(SetLoader.loadSet("Ambition"), runningInteger, targetPath, imagePath).buildFaces();
		runningInteger = new SetBuilder(SetLoader.loadSet("Rivalry"), runningInteger, targetPath, imagePath).buildFaces();
		runningInteger = new SetBuilder(SetLoader.loadSet("Promo"), runningInteger, targetPath, imagePath).buildFaces();
		
//		Path imgPath = Paths.get("D:\\Projects\\Roll for the Galaxy 2\\imgArchive");
//		ImageGenerator gen = new ImageGenerator();
//		gen.generateImage("the landscape of a prosperous planet covered in fruit orchards, colorful, light blue, sci-fi, digital art", imgPath);
	}
}
