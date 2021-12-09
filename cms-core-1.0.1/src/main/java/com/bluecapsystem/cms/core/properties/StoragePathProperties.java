package com.bluecapsystem.cms.core.properties;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StoragePathProperties {
	private static Map<String, String> directories;

	public void setDirectories(Map<String, String> fileRoot) {
		StoragePathProperties.directories = fileRoot;
	}

	public static File getDiretory(String key) {

		String path = directories.containsKey(key) ? directories.get(key) : null;

		if (path == null)
			return null;

		File file = new File(directories.get(key));

		if (file.exists() == false && file.mkdirs() == false)
			return null;
		if (file.isDirectory() == false)
			return null;

		return file;
	}

	public static Map<String, String> getLocalPath() {
		return new HashMap<String, String>(directories);
	}
}
