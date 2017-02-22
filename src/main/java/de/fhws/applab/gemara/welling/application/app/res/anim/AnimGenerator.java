package de.fhws.applab.gemara.welling.application.app.res.anim;

import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

@SuppressWarnings("WeakerAccess")
public abstract class AnimGenerator extends GeneratedFile {

	public abstract Alpha getAlpha();

	private final String fileName;
	private final String directoryName;

	public AnimGenerator(String fileName, String directoryName) {
		this.fileName = fileName + ".xml";
		this.directoryName = directoryName + "/anim";
	}

	@Override
	public void generate() {
		appendln("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		appendln("<set xmlns:android=\"http://schemas.android.com/apk/res/android\" android:fillAfter=\"true\">");
		generateBody(getAlpha());
		appendln("</set>");
	}

	private void generateBody(Alpha alpha) {
		appendln("<alpha");
		appendln("android:duration=\"" + alpha.getDuration() + "\"");
		appendln("android:fromAlpha=\"" + alpha.getFromAlpha() + "\"");
		appendln("android:toAlpha=\"" + alpha.getToAlpha() + "\"/>");
	}

	@Override
	protected String getFileName() {
		return this.fileName;
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}

	public static class Alpha {

		private final int duration;
		private final double fromAlpha;
		private final double toAlpha;

		@SuppressWarnings("SameParameterValue")
		public Alpha(int duration, double fromAlpha, double toAlpha) {
			this.duration = duration;
			this.fromAlpha = fromAlpha;
			this.toAlpha = toAlpha;
		}

		public int getDuration() {
			return duration;
		}

		public double getFromAlpha() {
			return fromAlpha;
		}

		public double getToAlpha() {
			return toAlpha;
		}
	}
}