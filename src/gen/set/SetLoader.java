package gen.set;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import gen.Main;
import gen.set.definitions.Set;
import gen.util.GenericMarshaller;
import gen.util.GenericMarshaller.GenericMarshallerException;

public class SetLoader {

	private SetLoader() {

	}

	public static Set loadSet(String setName) throws SetLoaderException {
		var url = Main.class.getResource("/gen/set/data/" + setName + ".xml");
		try {
			var path = Paths.get(url.toURI());
			return GenericMarshaller.unmarshall(Set.class, path);
		} catch (GenericMarshallerException | URISyntaxException e) {
			throw new SetLoaderException("Failed to load: " + setName, e);
		}
	}

	public static class SetLoaderException extends Exception {
		private static final long serialVersionUID = -2065853778373481542L;

		private SetLoaderException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
