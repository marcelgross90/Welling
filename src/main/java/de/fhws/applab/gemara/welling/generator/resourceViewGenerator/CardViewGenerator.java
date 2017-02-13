package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.application.app.java.NoPictureActivityGenerator;
import de.fhws.applab.gemara.welling.application.app.java.PictureActivityGenerator;
import de.fhws.applab.gemara.welling.generator.StateHolder;
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
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;
import de.fhws.applab.gemara.welling.visitors.ContainsImageVisitor;
import de.fhws.applab.gemara.welling.visitors.LinkDescriptionVisitor;

import java.util.ArrayList;
import java.util.List;

public class CardViewGenerator extends ResourceViewGenerator<CardView> {

	private final String resourceName;


	public CardViewGenerator(CardView cardView, AppDescription appDescription, StateHolder stateHolder) {
		super(cardView, appDescription, stateHolder);

		this.resourceName = cardView.getResourceName();
	}

	@Override
	protected List<AbstractModelClass> getLibJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getLibGenericJavaClasses());
		classes.addAll(getLibSpecificJavaClasses());

		return classes;
	}

	@Override
	protected List<GeneratedFile> getLibXMLClasses() {
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
		ResourceCardViewGenerator resourceCardViewGenerator = new ResourceCardViewGenerator(libPackageName, resourceView);

		appDescription.setDeclareStyleables(resourceCardViewGenerator.getClassName(), new AppDeclareStyleable.DeclareStyleable(resourceCardViewGenerator.getClassName()));

		return resourceCardViewGenerator;
	}

	private AbstractModelClass getLibSpecificViewHolder() {
		return new ListViewHolderGenerator(libPackageName, resourceView);
	}

	private List<GeneratedFile> getLibLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new CustomCardViewLayoutGenerator(libResDirectory, libPackageName, "card_" + resourceName.toLowerCase(),
				resourceName + "CardView", resourceName.toLowerCase() + "_card"));
		classes.add(new FragmentResourceList(libResDirectory));
		classes.add(new CardLayoutGenerator(appDescription, "view_" + resourceName.toLowerCase() + "_card", resourceView));

		return classes;
	}

	private List<GeneratedFile> getLibMenuClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new ListMenu(appDescription));

		return classes;
	}

	@Override
	protected List<AbstractModelClass> getAppJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new ListFragmentGenerator(stateHolder, appDescription, resourceView));

		ContainsImageVisitor containsImageVisitor = new ContainsImageVisitor();

		boolean containsImage = false;
		for (ResourceViewAttribute resourceViewAttribute : resourceView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(containsImageVisitor);
			if (containsImageVisitor.isContainsImage()) {
				containsImage = true;
				break;
			}
		}

		if (containsImage) {
			classes.add(new PictureActivityGenerator(appDescription, resourceName));
		} else {
			classes.add(new NoPictureActivityGenerator(appDescription, resourceName));
		}

		return classes;
	}

	@Override
	protected void addStrings() {
		LinkDescriptionVisitor visitor = new LinkDescriptionVisitor(appDescription);
		for (ResourceViewAttribute resourceViewAttribute : resourceView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
		}
	}
}
