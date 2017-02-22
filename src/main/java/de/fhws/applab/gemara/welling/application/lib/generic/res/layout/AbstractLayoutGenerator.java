package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractLayoutGenerator extends GeneratedFile {

	protected abstract View generateLayout();

	private final String fileName;
	private final String directoryName;

	public AbstractLayoutGenerator(String fileName, String directoryName) {
		this.fileName = fileName + ".xml";
		this.directoryName = directoryName + "/layout";
	}

	@Override
	public void generate() {
		appendln("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		appendView(generateLayout());
	}

	private void appendView(View view) {
		appendln("<" + view.getViewType());
		appendViewAttributes(view.getViewAttributes());
		appendln(view.getSubViews().size() == 0 ? "/>" : ">");
		appendSubView(view.getSubViews());
		appendln(view.getSubViews().size() == 0 ? "" : "</" + view.getViewType() + ">");
	}

	private void appendViewAttributes(List<String> viewAttributes) {
		viewAttributes.forEach(this::appendln);
	}

	private void appendSubView(List<View> subviews) {
		subviews.forEach(this::appendView);
	}

	@Override
	protected String getFileName() {
		return this.fileName;
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}

	protected List<String> getLayoutAttributes(String width, String height) {
		List<String> attributes = new ArrayList<>();

		attributes.add("android:layout_width=\"" + width + "\"");
		attributes.add("android:layout_height=\"" + height + "\"");

		return attributes;
	}

	public static class View {
		private final String viewType;
		private final List<String> viewAttributes = new ArrayList<>();
		private final List<View> subViews = new ArrayList<>();

		public View(String viewType) {
			this.viewType = viewType;
		}

		public String getViewType() {
			return viewType;
		}

		public List<String> getViewAttributes() {
			return viewAttributes;
		}

		public void setViewAttributes(List<String> viewAttributes) {
			this.viewAttributes.addAll(viewAttributes);
		}

		public void addViewAttribute(String viewAttribute) {
			this.viewAttributes.add(viewAttribute);
		}

		public List<View> getSubViews() {
			return subViews;
		}

		public void setSubViews(List<View> subViews) {
			this.subViews.addAll(subViews);
		}

		public void addSubView(View view) {
			this.subViews.add(view);
		}
	}
}