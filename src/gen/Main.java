package gen;

import java.nio.file.Path;
import java.nio.file.Paths;

import gen.print.PrintGenerator;
import gen.set.SetBuilder;
import gen.set.SetLoader;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println("Generating card images");
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		Path imagePath = currentPath.resolve("img");
		Path targetPath = currentPath.resolve("build");
		Integer runningInteger = new SetBuilder(SetLoader.loadSet("base"), null, targetPath, imagePath).buildFaces();
		runningInteger = new SetBuilder(SetLoader.loadSet("Ambition"), runningInteger, targetPath, imagePath)
				.buildFaces();
		runningInteger = new SetBuilder(SetLoader.loadSet("Rivalry"), runningInteger, targetPath, imagePath)
				.buildFaces();
		runningInteger = new SetBuilder(SetLoader.loadSet("Promo"), runningInteger, targetPath, imagePath).buildFaces();

		System.out.println();
		System.out.println("Generating print file");
		PrintGenerator print = new PrintGenerator(targetPath);
		print.buildPrintFile();

		System.out.println();
		System.out.println("Done");
	}
}
