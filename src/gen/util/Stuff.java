package gen.util;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

public class Stuff {
	
	private Stuff()
	{}
	
	public static Optional<File> lookForImage(Path folder, String name) {
		for (File file : folder.toFile().listFiles()) {
			String fileName = FilenameUtils.removeExtension(file.getName());
			if (fileName.equals(name)) {
				return Optional.of(file);
			}
		}
		return Optional.empty();
	}
}
