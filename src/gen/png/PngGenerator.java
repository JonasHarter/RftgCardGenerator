package gen.png;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PngGenerator implements AutoCloseable {

	private static Path INKSCAPE_PATH = Paths.get("C:\\Program Files\\Inkscape\\bin\\inkscape.exe");
	private static String COMMAND_PNG = "--export-type=\"png\"";
	private static String COMMAND_FILENAME = "--export-filename=";
	private static String COMMAND_DPI = "--export-dpi=1200";
	// Dont use too much threads
	private ExecutorService executor = Executors.newFixedThreadPool(4);

	public void convertFile(File file) throws PngGeneratorException {
		runProcess(file);
	}

	public void convertFileAsync(File file) {
		executor.submit(() -> {
			try {
				runProcess(file);
			} catch (Exception e) {
				System.out.println("Failed for file: " + file.getName());
				e.printStackTrace();
			}
		});
	}

	@Override
	public void close() throws Exception {
		try {
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void runProcess(File file) throws PngGeneratorException {
		String filePath = file.getAbsolutePath().toString();
		String newFilePath = COMMAND_FILENAME + filePath.replace("svg", "png");
		ProcessBuilder processBuilder = new ProcessBuilder(INKSCAPE_PATH.toString(), COMMAND_PNG, COMMAND_DPI,
				newFilePath, "\"" + file.getAbsolutePath().toString() + "\"");
		processBuilder.inheritIO();
		Process process;
		try {
			process = processBuilder.start();
			int exit = process.waitFor();
			if (exit != 0)
				throw new PngGeneratorException("Process exited with " + exit);
			System.out.println(file.getName());
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

		public PngGeneratorException(String message) {
			super(message);
		}

	}
}
