package de.fhws.applab.gemara.welling.generator.depricated;

import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.InputViewsGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.InputLayoutGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailCardViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.DetailViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder.DetailViewHolderGenerator;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.AbstractMainActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.ResourceActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.ResourceDetailActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.adapter.ResourceListAdapter;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.AttributeInput;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.AttributeView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.DateTimeView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ProfileImageView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceCardView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceDetailView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceInputView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DateTimePickerFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DeleteDialogFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DetailResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.EditResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.NewResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.ResourceListFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.viewholder.ResourceViewHolder;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.ActivityMain;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.FragmentResourceList;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.TextinputAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.Toolbar;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.DetailMenu;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.ListMenu;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.SaveMenu;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Attr;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Colors;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Dimens;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.RestApi;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Strings;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Styles;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.ResourceCardViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder.ListViewHolderGenerator;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.test.modelGenerator.CardViewModelGenerator;
import de.fhws.applab.gemara.welling.test.modelGenerator.DetailViewModelGenerator;
import de.fhws.applab.gemara.welling.test.modelGenerator.InputViewModelGenerator;

import java.util.ArrayList;
import java.util.List;

public class LibGenerator {

	private final AndroidMetaModel model;
	private final String baseDir;
	private final String resDir;

	public LibGenerator(AndroidMetaModel model) {
		this.model = model;
		this.baseDir = "generated/" + model.getApplicationName().toLowerCase() + "_lib/";
		String xmlBaseDir = baseDir + "src/main/";
		this.resDir = xmlBaseDir + "res/";

	}

	public List<AbstractModelClass> getJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getGenericAndroidClasses());
		classes.addAll(getSpecificAndroidClasses());

		return classes;
	}

	public List<GeneratedFile> getXMLClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.addAll(getResClasses());

		return classes;
	}


	public List<GeneratedFile> getResClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.addAll(getLayoutClasses());
		classes.addAll(getValuesClasses());
		classes.addAll(getMenuClasses());

		return classes;
	}

	private List<GeneratedFile> getLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.addAll(getSpecificLayoutClasses());
		classes.addAll(getGenericLayoutClasses());
		classes.addAll(getMenuClasses());

		return classes;
	}

	private List<GeneratedFile> getGenericLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.add(new ActivityMain(resDir));
		classes.add(new FragmentResourceList(resDir));
		classes.add(new TextinputAttribute(resDir));
		classes.add(new Toolbar(resDir));

		return classes;
	}

	private List<GeneratedFile> getSpecificLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.addAll(getInputViewClasses());
		classes.addAll(getSpecificCardViewClasses());
		classes.addAll(getCustomCardViewLayoutClasses());
		classes.addAll(getDetailViewClasses());
		return classes;
	}

	private List<GeneratedFile> getInputViewClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new InputLayoutGenerator(resDir, model.getPackageNameLib(), InputViewModelGenerator.lecturer()));

		return classes;
	}

	private List<GeneratedFile> getDetailViewClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		/*for (AppResource appResource : model.getAppResources()) {
			classes.add(new ViewResourceDetailActivityGenerator(resDir, appResource.getResourceName(), model.getPackageNameLib()));
		}*/

		return classes;
	}

	private List<GeneratedFile> getSpecificCardViewClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		/*for (AppResource appResource : model.getAppResources()) {
			String resourceName = appResource.getResourceName();
			//todo replace cardView
			classes.add(new CardLayoutGenerator("view_" + resourceName.toLowerCase() + "_card", resDir,
					CardViewModelGenerator.lecturer(), model.getPackageNameLib()));
			//todo replace detailView
			classes.add(new DetailCardLayoutGenerator("view_" + resourceName.toLowerCase() + "_detail_card", resDir, model.getPackageNameLib(), DetailViewModelGenerator
					.lecturer()));

		}*/

		return classes;
	}

	private List<GeneratedFile> getCustomCardViewLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

	/*	for (AppResource appResource : model.getAppResources()) {
			String resourceName = appResource.getResourceName();
			classes.add(new CustomCardViewLayoutGenerator(resDir, model.getPackageNameLib(), "card_" + resourceName.toLowerCase(), resourceName + "CardView", resourceName.toLowerCase() + "_card"));
			classes.add(new CustomCardViewLayoutGenerator(resDir, model.getPackageNameLib(), "card_" + resourceName.toLowerCase() + "_detail", resourceName + "DetailCardView", resourceName.toLowerCase() + "_detail_card"));
		}*/

		return classes;
	}

	private List<GeneratedFile> getValuesClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.add(new Attr(resDir, model.getAppDeclareStyleable()));
		classes.add(new Colors(resDir, model.getAppColor()));
		classes.add(new Dimens(resDir));
		classes.add(new RestApi(resDir, model.getAppRestAPI()));
		classes.add(new Strings(resDir, model.getLibStrings()));
		classes.add(new Styles(resDir, model.getLibStyles()));

		return classes;
	}

	private List<GeneratedFile> getMenuClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		//classes.add(new DetailMenu(resDir));
//		classes.add(new ListMenu(resDir));
//		classes.add(new SaveMenu(resDir));

		return classes;
	}

	public List<AbstractModelClass> getGenericAndroidClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getGenericActivities());
		classes.addAll(getGenericAdapter());
		classes.addAll(getGenericCustomViews());
		classes.addAll(getGenericFragments());
		classes.addAll(getGenericViewholder());

		return classes;
	}

	private List<AbstractModelClass> getGenericActivities() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new AbstractMainActivity(model.getPackageNameLib()));
		classes.add(new ResourceActivity(model.getPackageNameLib()));
		//classes.add(new ResourceDetailActivity(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericAdapter() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new ResourceListAdapter(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericCustomViews() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new AttributeInput(model.getPackageNameLib()));
		classes.add(new AttributeView(model.getPackageNameLib()));
		classes.add(new DateTimeView(model.getPackageNameLib()));
		classes.add(new ProfileImageView(model.getPackageNameLib()));
		classes.add(new ResourceCardView(model.getPackageNameLib()));
		classes.add(new ResourceDetailView(model.getPackageNameLib()));
		classes.add(new ResourceInputView(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericFragments() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new DateTimePickerFragment(model.getPackageNameLib()));
		classes.add(new DeleteDialogFragment(model.getPackageNameLib()));
		classes.add(new DetailResourceFragment(model.getPackageNameLib()));
		classes.add(new EditResourceFragment(model.getPackageNameLib()));
		classes.add(new NewResourceFragment(model.getPackageNameLib()));
		classes.add(new ResourceListFragment(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericViewholder() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new ResourceViewHolder(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getSpecificAndroidClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getSpecificAdapter());
		classes.addAll(getSpecificCustomViews());
		classes.addAll(getSpecificViewholder());

		return classes;
	}

	private List<AbstractModelClass> getSpecificAdapter() {
		List<AbstractModelClass> classes = new ArrayList<>();

	/*	for (AppResource appResource : model.getAppResources()) {
			classes.add(new ListAdapterGenerator(model.getPackageNameLib(), appResource.getResourceName()));
			//todo detailview
			classes.add(new DetailAdapterGenerator(model.getPackageNameLib(), DetailViewModelGenerator.lecturer()));
		}*/

		return classes;
	}

	private List<AbstractModelClass> getSpecificCustomViews() {
		List<AbstractModelClass> classes = new ArrayList<>();
			//todo add right cardView
			classes.add(new ResourceCardViewGenerator(model.getPackageNameLib(), CardViewModelGenerator.lecturer()));
		//	classes.add(new DetailViewGenerator(model.getPackageNameLib(), DetailViewModelGenerator.lecturer()));
		//	classes.add(new DetailCardViewGenerator(model.getPackageNameLib(), DetailViewModelGenerator.lecturer()));
		//todo add inputview instead of appResource
				classes.add(new InputViewsGenerator(model.getPackageNameLib(), InputViewModelGenerator.lecturer()));

		return classes;
	}




	private List<AbstractModelClass> getSpecificViewholder() {
		List<AbstractModelClass> classes = new ArrayList<>();

			//todo replace cardview
			classes.add(new ListViewHolderGenerator(model.getPackageNameLib(), CardViewModelGenerator.lecturer()));
			classes.add(new DetailViewHolderGenerator(model.getPackageNameLib(), DetailViewModelGenerator.lecturer()));

		return classes;
	}


}
