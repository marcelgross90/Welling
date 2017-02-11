package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.application.app.java.DetailActivityGenerator;
import de.fhws.applab.gemara.welling.application.app.res.layout.ActivityDetailViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.ResourceDetailActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceDetailView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DeleteDialogFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.DetailMenu;
import de.fhws.applab.gemara.welling.application.lib.specific.java.adapter.DetailAdapterGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailCardViewGenerator;
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

	private final String appPackageName;
	private final String appResDirectory;
	private final String appName;

	public DetailViewGenerator(DetailView detailView, AppDescription appDescription, StateHolder stateHolder) {
		super(detailView, appDescription, stateHolder);

		this.appPackageName = appDescription.getAppPackageName();
		this.appResDirectory = appDescription.getAppResDirectory();
		this.appName = appDescription.getAppName();

		addActivityToManifest();
	}

	private void addActivityToManifest() {
		//todo
	}


	@Override
	protected List<AbstractModelClass> getLibJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();
		if (containsImage()) {
			classes.add(new ResourceDetailActivity(libPackageName));
			classes.add(new DetailAdapterGenerator(libPackageName, resourceView));
			classes.add(new DetailViewHolderGenerator(libPackageName, resourceView));

		}
		if (stateHolder.getNextStates().contains(StateHolder.StateType.DELETE)) {
			classes.add(new DeleteDialogFragment(libPackageName));
		}

		classes.add(new de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailViewGenerator(appDescription, resourceView));
		classes.add(new DetailCardViewGenerator(appDescription, resourceView));
		classes.add(new ResourceDetailView(libPackageName));

		return classes;
	}

	@Override
	protected List<GeneratedFile> getLibXMLClasses() {
		String resourceName = resourceView.getResourceName();
		List<GeneratedFile> classes = new ArrayList<>();
		if (containsImage()) {
			classes.add(new ActivityDetailViewGenerator(appResDirectory, resourceName, appPackageName));
			classes.add(new DetailCardLayoutGenerator(appDescription, "view_" + resourceName.toLowerCase() + "_detail_card", resourceView));
			classes.add(new ViewResourceDetailActivityGenerator(appDescription, resourceView));
		}
		if (stateHolder.getNextStates().contains(StateHolder.StateType.DELETE) ||
				stateHolder.getNextStates().contains(StateHolder.StateType.PUT)) {
			classes.add(new DetailMenu(appDescription));
		}

		return classes;
	}

	@Override
	protected List<AbstractModelClass> getAppJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();
		if (containsImage()) {
			classes.add(new DetailActivityGenerator(appDescription, resourceView));
		}

		//todo implement and add LecturerActivity

		return classes;
	}

	@Override
	protected void addStrings() {
		String resourceName = resourceView.getResourceName();
		if (stateHolder.getNextStates().contains(StateHolder.StateType.DELETE)) {
			appDescription.setLibStrings(resourceName.toLowerCase() + "_delete_error", "Could not delete " + resourceName.toLowerCase());
		}
	}


	private boolean containsImage() {
		return resourceView.getImage() != null;
	}

}
