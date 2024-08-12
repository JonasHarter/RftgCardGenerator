package gen;

import java.nio.file.Path;
import java.nio.file.Paths;

import gen.set.SetBuilder;
import gen.set.SetLoader;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println("Generating card images");
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		Path imagePath = currentPath.resolve("img");
		Path targetPath = currentPath.resolve("build");
		new SetBuilder(SetLoader.loadSet("base"), targetPath, imagePath).build();
		new SetBuilder(SetLoader.loadSet("Ambition"), targetPath, imagePath).build();
		new SetBuilder(SetLoader.loadSet("Rivalry"), targetPath, imagePath).build();
		new SetBuilder(SetLoader.loadSet("Promo"), targetPath, imagePath).build();
//		new SetBuilder(SetLoader.loadSet("Custom"), targetPath, imagePath).build();
		
//		System.out.println();
//		System.out.println("Generating print file");
//		PrintGenerator print = new PrintGenerator(targetPath);
//		print.buildPrintFile();
		
		System.out.println();
		System.out.println("Done");
	}
}
