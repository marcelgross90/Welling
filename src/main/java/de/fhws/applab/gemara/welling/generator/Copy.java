package de.fhws.applab.gemara.welling.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Copy {

	private final ClassLoader classLoader;

	public Copy() {
		this.classLoader = this.getClass().getClassLoader();
	}

	public void copyFile(String source, String destination) {
		copy(getSource(source), getDestination(destination));
	}
	public void copyFolder(String source, String destination) {
		copyFolder(getSource(source), getDestination(destination));
	}

	public void copySubFolderOnly(String source, String destination) {
		File src = getSource(source);
		File dest = getDestination(destination);

		String files[] = src.list();

		for (String file : files) {
			File srcFile = new File(src, file);
			File destFile = new File(dest, file);

			copyFolder(srcFile, destFile);
		}
	}

	private File getSource(String source) {
		URL sourceUrl = classLoader.getResource(source);
		if (sourceUrl == null) {
			throw new NullPointerException();
		}
		return new File(sourceUrl.getFile());
	}

	private File getDestination(String destination) {
		return new File(System.getProperty("user.dir") + "/gemara/android/src-gen/generated/" + destination);
	}

	private void copyFolder(File src, File dest) {

		if (src.isDirectory()) {

			if (!dest.exists()) {
				boolean dirsCreated = dest.mkdirs();
				if (!dirsCreated) {
					System.out.println("Could not create ( " + dest.getAbsolutePath() + " )");
				}
			}

			String files[] = src.list();

			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);

				copyFolder(srcFile, destFile);
			}

		} else {
			copy(src, dest);
		}
	}

	private void copy(File src, File dest) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (IOException ex) {
			System.out.println("Could not copyFile file");
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			System.out.println("Could not find ( " + src + " )");
		} finally {
			try {
				if (is != null && os != null) {
					is.close();
					os.close();
				}
			} catch (IOException ex) {
				System.out.println("Could not close Stream");
				ex.printStackTrace();
			}

		}
	}
}
