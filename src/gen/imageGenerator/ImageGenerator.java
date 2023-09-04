package gen.imageGenerator;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImageGenerator {

	//https://ptsv3.com/t/asdf/
	//https://httpbin.org/post 
//	private final static String API_STRING_HOST = "httpbin.org";
//	private final static String API_STRING_URI = "/post";
	private final static String API_STRING_HOST = "api.openai.com";
	private final static String API_STRING_URI = "/v1/images/generations";
	private final static String API_KEY = "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";
	private final static Integer COUNT = 4;
	private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(COUNT);

	public void generateImage(String prompt, Path targetFolderPath) throws ImageGeneratorException {
		try {
			callApi(prompt, targetFolderPath);
		} catch (Exception e) {
			throw new ImageGeneratorException("Failed image generation", e);
		}
	}

	private void callApi(String prompt, Path targetFolderPath) throws Exception {

		Integer n = 4;
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(n * 60)).build();

		try (CloseableHttpAsyncClient client = HttpAsyncClients.custom().setIOReactorConfig(ioReactorConfig).build()) {
			client.start();

			JSONObject content = new JSONObject().put("prompt", prompt).put("n", n);
			var requestBuilder = AsyncRequestBuilder.post();
			requestBuilder.setHttpHost(new HttpHost(API_STRING_HOST));
			requestBuilder.setScheme("https");
			requestBuilder.setPath(API_STRING_URI);
			requestBuilder.setEntity(content.toString());
			requestBuilder.addHeader("Content-Type", "application/json");
			requestBuilder.addHeader("Authorization", "Bearer " + API_KEY);
			System.out.println(requestBuilder.toString());

			AsyncRequestProducer requestProducer = requestBuilder.build();
			AsyncResponseConsumer<SimpleHttpResponse> requestConsumer = SimpleResponseConsumer.create();
			FutureCallback<SimpleHttpResponse> callback = new ImageGeneratorRequestCallback(targetFolderPath);
			var future = client.execute(requestProducer, requestConsumer, callback);
			future.get();
		}
	}

	private void parseResponse(SimpleHttpResponse result, Path targetFolderPath, String prefix) throws IOException {
		JSONObject json = new JSONObject(result.getBodyText());
		JSONArray data = json.getJSONArray("data");
		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < data.length(); i++) {
			JSONObject object = data.getJSONObject(i);
			String urlString = object.getString("url");
			URL url = new URL(urlString);
			Path imagePath = targetFolderPath.resolve(prefix + "_" + Integer.toString(i + 1) + ".png");
			futures.add(downloadImage(url, imagePath));
		}
		for(Future<?> future : futures)
		{
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private Future<?> downloadImage(URL url, Path targetPath) throws IOException {
		return EXECUTOR.submit(() -> {
			try {
				FileUtils.copyURLToFile(url, targetPath.toFile());
			} catch (IOException e) {
				System.err.println("Failed dl");
				e.printStackTrace();
			}
		});
		
	}

	private class ImageGeneratorRequestCallback implements FutureCallback<SimpleHttpResponse> {
		private String prefix = Long.toString(Instant.now().getEpochSecond());
		private Path targetFolderPath;

		ImageGeneratorRequestCallback(Path targetFolderPath) {
			this.targetFolderPath = targetFolderPath;
		}

		@Override
		public void completed(SimpleHttpResponse result) {
			try {
				parseResponse(result, targetFolderPath, prefix);
			} catch (Exception e) {
				System.err.println("Failed parse");
				e.printStackTrace();
			}
		}

		@Override
		public void failed(Exception ex) {
			System.err.println("Failed request");
			ex.printStackTrace();

		}

		@Override
		public void cancelled() {
			System.err.println("Failed cancelled");
		}

	}

	public static class ImageGeneratorException extends Exception {
		private static final long serialVersionUID = -369288247714369519L;

		private ImageGeneratorException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
