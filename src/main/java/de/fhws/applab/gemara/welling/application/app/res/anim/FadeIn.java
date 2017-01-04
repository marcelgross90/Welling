package de.fhws.applab.gemara.welling.application.app.res.anim;

public class FadeIn extends AnimGenerator {

	public FadeIn(String directoryName) {
		super("fade_in", directoryName);
	}

	@Override
	public Alpha getAlpha() {
		return new Alpha(600, 1.0, 0.0);
	}
}