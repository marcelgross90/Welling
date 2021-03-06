package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.application.app.java.DetailActivityGenerator;
import de.fhws.applab.gemara.welling.application.app.java.fragment.DetailFragmentGenerator;
import de.fhws.applab.gemara.welling.application.app.res.layout.DetailViewLayoutGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.ResourceDetailActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceDetailView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DeleteDialogFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DetailResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.CustomCardViewLayoutGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.DetailMenu;
import de.fhws.applab.gemara.welling.application.lib.specific.java.adapter.DetailAdapterGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailCardViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailViewWithPictureGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailViewWithoutPictureGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder.DetailViewHolderGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.DetailCardLayoutGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.ViewResourceDetailActivityGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.ArrayList;
import java.util.List;

public class DetailViewGenerator extends ResourceViewGenerator<DetailView> {

	public DetailViewGenerator(DetailView detailView, AppDescription appDescription, StateHolder stateHolder) {
		super(detailView, appDescription, stateHolder);
	}

	@Override
	protected List<AbstractModelClass> getLibJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		if (containsImage()) {
			classes.add(new ResourceDetailActivity(stateHolder, libPackageName));
			classes.add(new DetailAdapterGenerator(libPackageName, resourceView));
			classes.add(new DetailViewHolderGenerator(libPackageName, resourceView));
			classes.add(new DetailViewWithPictureGenerator(appDescription, resourceView));
			classes.add(new DetailCardViewGenerator(appDescription, resourceView));

		} else {
			classes.add(new DetailResourceFragment(libPackageName, stateHolder));
			classes.add(new DetailViewWithoutPictureGenerator(appDescription, resourceView));
		}
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			classes.add(new DeleteDialogFragment(appDescription));
		}

		classes.add(new ResourceDetailView(libPackageName));

		return classes;
	}

	@Override
	protected List<GeneratedFile> getLibXMLClasses() {
		String resourceName = resourceView.getResourceName();
		List<GeneratedFile> classes = new ArrayList<>();

		if (containsImage()) {
			classes.add(new DetailViewLayoutGenerator(appDescription, resourceName, "activity_" + resourceName.toLowerCase() + "_detail"));
			classes.add(new DetailCardLayoutGenerator(appDescription, "view_" + resourceName.toLowerCase() + "_detail_card", resourceView));
			classes.add(new ViewResourceDetailActivityGenerator(appDescription, resourceView));
			classes.add(new CustomCardViewLayoutGenerator(libResDirectory, libPackageName, "card_" + resourceName.toLowerCase() + "_detail",
					resourceName + "DetailCardView", resourceName.toLowerCase() + "_detail_card"));
		} else {
			classes.add(new DetailCardLayoutGenerator(appDescription, "view_" + resourceName.toLowerCase() + "_detail", resourceView));
			classes.add(new DetailViewLayoutGenerator(appDescription, resourceName, "fragment_" + resourceName.toLowerCase() + "_detail"));
		}
		if (stateHolder.contains(StateHolder.StateType.DELETE) || stateHolder.contains(StateHolder.StateType.PUT)) {
			classes.add(new DetailMenu(appDescription));
		}

		return classes;
	}

	@Override
	protected List<AbstractModelClass> getAppJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		if (containsImage()) {
			classes.add(new DetailActivityGenerator(appDescription, resourceView, stateHolder));
		} else {
			classes.add(new DetailFragmentGenerator(appDescription, resourceView, stateHolder));
		}

		return classes;
	}

	@Override
	protected void addStrings() {
		String resourceName = resourceView.getResourceName();

		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			appDescription.setLibStrings(replaceIllegalCharacters(resourceName.toLowerCase()) + "_delete_error",
					"Could not delete " + resourceName.toLowerCase());
		}
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}

	private boolean containsImage() {
		return resourceView.getImage() != null;
	}
}