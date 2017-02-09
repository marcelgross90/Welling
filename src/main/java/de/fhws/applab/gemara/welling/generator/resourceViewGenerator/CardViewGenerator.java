package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.application.app.java.fragment.ListFragmentGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.adapter.ResourceListAdapter;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceCardView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.ResourceListFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.viewholder.ResourceViewHolder;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.CustomCardViewLayoutGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.FragmentResourceList;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.ListMenu;
import de.fhws.applab.gemara.welling.application.lib.specific.java.adapter.ListAdapterGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.ResourceCardViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder.ListViewHolderGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.CardLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;

import java.util.ArrayList;
import java.util.List;

public class CardViewGenerator {

	private final AppDescription appDescription;
	private final CardView cardView;
	private final String appName;
	private final String resourceName;

	private final String appPackageName;
	private final String appJavaDirectory;

	private final String libPackageName;
	private final String libResDirectory;
	private final String libJavaDirectory;

	public CardViewGenerator(CardView cardView, AppDescription appDescription) {
		this.appDescription = appDescription;
		this.cardView = cardView;
		this.appName = appDescription.getAppName();
		this.resourceName = cardView.getResourceName();

		this.appPackageName = appDescription.getAppPackageName();
		this.appJavaDirectory = appDescription.getAppJavaDirectory();
		this.libPackageName = appDescription.getLibPackageName();
		this.libResDirectory = appDescription.getLibResDirectory();
		this.libJavaDirectory = appDescription.getLibJavaDirectory();
	}

	public void generate() {
		FileWriter.writeJavaFiles(getLibJavaClasses(), libJavaDirectory);
		FileWriter.writeGeneratedFiles(getLibXMLClasses());

		FileWriter.writeJavaFiles(getAppJavaClasses(), appJavaDirectory);

	}

	private List<AbstractModelClass> getLibJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getLibGenericJavaClasses());
		classes.addAll(getLibSpecificJavaClasses());

		return classes;
	}

	private List<GeneratedFile> getLibXMLClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.addAll(getLibLayoutClasses());
		classes.addAll(getLibMenuClasses());

		return classes;
	}

	private List<AbstractModelClass> getLibGenericJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(getResourceListAdapter());
		classes.add(getResourceCardView());
		classes.add(getResourceListFragment());
		classes.add(getResourceViewHolder());

		return classes;
	}

	private AbstractModelClass getResourceListAdapter() {
		return new ResourceListAdapter(libPackageName);
	}

	private AbstractModelClass getResourceCardView() {
		return new ResourceCardView(libPackageName);
	}

	private AbstractModelClass getResourceListFragment() {
		return new ResourceListFragment(libPackageName);
	}

	private AbstractModelClass getResourceViewHolder() {
		return new ResourceViewHolder(libPackageName);
	}

	private List<AbstractModelClass> getLibSpecificJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(getLibSpecificListAdapter());
		classes.add(getLibSpecificCardView());
		classes.add(getLibSpecificViewHolder());

		return classes;
	}

	private AbstractModelClass getLibSpecificListAdapter() {
		return new ListAdapterGenerator(libPackageName, resourceName);
	}

	private AbstractModelClass getLibSpecificCardView() {
		ResourceCardViewGenerator resourceCardViewGenerator = new ResourceCardViewGenerator(libPackageName, cardView);

		appDescription.setDeclareStyleables(new AppDeclareStyleable.DeclareStyleable(resourceCardViewGenerator.getClassName()));

		return resourceCardViewGenerator;
	}

	private AbstractModelClass getLibSpecificViewHolder() {
		return new ListViewHolderGenerator(libPackageName, cardView);
	}

	private List<GeneratedFile> getLibLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new CustomCardViewLayoutGenerator(libResDirectory, libPackageName, "card_" + resourceName.toLowerCase(),
				resourceName + "CardView", resourceName.toLowerCase() + "_card"));
		classes.add(new FragmentResourceList(libResDirectory));
		classes.add(new CardLayoutGenerator(appDescription, "view_" + resourceName.toLowerCase() + "_card", cardView));

		return classes;
	}

	private List<GeneratedFile> getLibMenuClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new ListMenu(appDescription));

		return classes;
	}

	private List<AbstractModelClass> getAppJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new ListFragmentGenerator(appPackageName, cardView, appName));

		return classes;
	}
}
