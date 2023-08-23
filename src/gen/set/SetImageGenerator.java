package gen.set;

import java.nio.file.Path;

import gen.set.definitions.Card;
import gen.set.definitions.Face;
import gen.set.definitions.Set;
import gen.util.Stuff;

public class SetImageGenerator {
	
	private Set set;
	private Path imageOutputFolder;

	public SetImageGenerator(Set set, Path imageOutputFolder)  {
		this.set = set;
		this.imageOutputFolder = imageOutputFolder;
	}

	public void generateImages() throws ImageDownloaderException  {
		int i = 0;
		for (Card card : set.getCards()) {
			for (Face face : card.getFaces()) {
				if(imageAlreadyExists(face.getName()))
					continue;
				//System.out.println("GEN: " + face.getName());
				try {
					FaceImageGenerator generator = new FaceImageGenerator(face);
					generator.generateImage();
//					downloader.writeToFile(set.getName(), i);
				} catch (Exception e)
				{
					throw new ImageDownloaderException("Failed to dl image for face: " + face.getName(), e);
				}
			}
			i++;
		}
	}
	
	private boolean imageAlreadyExists(String name)
	{
		var imageFileOpt = Stuff.lookForImage(imageOutputFolder, name);
		return imageFileOpt.isPresent();
	}
	
	public static class ImageDownloaderException extends Exception {
		private static final long serialVersionUID = -2912760178429897748L;

		private ImageDownloaderException(String message, Throwable cause) {
			super(message, cause);
		}

	}
	
	private class FaceImageGenerator
	{
		private Face face;
		
		public FaceImageGenerator(Face face)
		{
			this.face = face;
		}
		
		public void generateImage()
		{
			
		}
		
		private void callApi()
		{
			
		}
		
		private void parseResponse()
		{
			
		}
		
		private void downloadImage()
		{
			
		}
	}
}
