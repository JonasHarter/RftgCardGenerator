package gen.png;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;

public class PngGenerator {

	private static Path INKSCAPE_PATH = Paths.get("C:\\Program Files\\Inkscape\\bin\\inkscape.exe");
	private static String COMMAND = "--export-type=\"png\"";
	private Path targetPath;

	public PngGenerator(Path targetPath) {
		this.targetPath = targetPath;
	}

	public void convertFiles() {
		// Del images
		for (File file : targetPath.toFile().listFiles()) {
			if (!FilenameUtils.getExtension(file.getName()).equals("png"))
				continue;
			file.delete();
		}
		// Get files
		List<File> vectorFiles = new ArrayList<>();
		for (File file : targetPath.toFile().listFiles()) {
			if (!FilenameUtils.getExtension(file.getName()).equals("svg"))
				continue;
			vectorFiles.add(file);
		}
		// Convert
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
		for (File file : vectorFiles) {
			executor.submit(() -> {
				try {
					runProcess(file);
				} catch (Exception e) {
					System.out.println("Failed for file: " + file.getName());
					e.printStackTrace();
				}
			});
		}
		try {
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		// Del old files
		for (File file : vectorFiles) {
			file.delete();
		}
	}

	private void runProcess(File file) throws PngGeneratorException {
		ProcessBuilder processBuilder = new ProcessBuilder(INKSCAPE_PATH.toString(), COMMAND, "\"" + file.getAbsolutePath().toString() + "\"");
		processBuilder.redirectOutput(Redirect.PIPE);
		//processBuilder.directory(INKSCAPE_PATH.to);
		Process process;
		try {
			process = processBuilder.start();
			process.waitFor();
		} catch (IOException e) {
			throw new PngGeneratorException("Failed to run process", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new PngGeneratorException("Failed to wait for process", e);
		}
	}

	public static class PngGeneratorException extends Exception {
		private static final long serialVersionUID = -1069667162924953080L;

		private PngGeneratorException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
