package de.fhws.applab.gemara.welling.application.app.res.anim;

public class FadeOut extends AnimGenerator {

	public FadeOut(String directoryName) {
		super("fade_out", directoryName);
	}

	@Override
	public Alpha getAlpha() {
		return new Alpha(600, 0.0, 1.0);
	}
}