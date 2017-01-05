package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.AbstractModelClass;
import de.fhws.applab.gemara.welling.GeneratedFile;
import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
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
import de.fhws.applab.gemara.welling.application.lib.generic.java.model.Link;
import de.fhws.applab.gemara.welling.application.lib.generic.java.model.Resource;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.HeaderParser;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkCallback;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkClient;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkRequest;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkResponse;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.OKHttpSingleton;
import de.fhws.applab.gemara.welling.application.lib.generic.java.util.FragmentHandler;
import de.fhws.applab.gemara.welling.application.lib.generic.java.util.GensonBuilder;
import de.fhws.applab.gemara.welling.application.lib.generic.java.util.ScrollListener;
import de.fhws.applab.gemara.welling.application.lib.generic.java.viewholder.ResourceViewHolder;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.ActivityMain;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.CustomCardViewLayoutGenerator;
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
import de.fhws.applab.gemara.welling.application.lib.specific.java.adapter.ListAdapterGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.ResourceCardViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.ResourceGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder.ListViewHolderGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.CardLayoutGenerator;
import de.fhws.applab.gemara.welling.gradle.lib.BuildGradleLib;
import de.fhws.applab.gemara.welling.gradle.lib.LibProguardRules;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibGenerator {

	private final AndroidMetaModel model;
	private final String baseDir;
	private final String xmlBaseDir;
	private final String resDir;

	public LibGenerator(AndroidMetaModel model) {
		this.model = model;
		this.baseDir = "generated/" + model.getApplicationName().toLowerCase() + "_lib/";
		this.xmlBaseDir = baseDir + "src/main/";
		this.resDir = xmlBaseDir + "res/";

		copyDrawableFolders();
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

	public List<GeneratedFile> getGradleClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new BuildGradleLib(baseDir));
		classes.add(new LibProguardRules(baseDir));

		return classes;
	}

	public List<GeneratedFile> getResClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.addAll(getLayoutClasses());
		classes.add(getManifest());
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
		classes.addAll(getSpecificCardViewClasses());
		classes.addAll(getCustomCardViewLayoutClasses());
		return classes;
	}

	private List<GeneratedFile> getSpecificCardViewClasses() {
		return model.getAppResources().stream()
				.map(appResource -> new CardLayoutGenerator("view_" + appResource.getResourceName().toLowerCase() + "_card", resDir,
						appResource, model.getPackageNameLib())).collect(Collectors.toList());
	}

	private List<GeneratedFile> getCustomCardViewLayoutClasses() {
		return model.getAppResources().stream()
				.map(appResource -> new CustomCardViewLayoutGenerator(resDir, model.getPackageNameLib(), appResource)).collect(Collectors.toList());
	}

	private GeneratedFile getManifest() {
		return new ManifestGenerator(xmlBaseDir, model.getLibManifest());
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
		classes.add(new DetailMenu(resDir));
		classes.add(new ListMenu(resDir));
		classes.add(new SaveMenu(resDir));

		return classes;
	}

	public List<AbstractModelClass> getGenericAndroidClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getGenericActivities());
		classes.addAll(getGenericAdapter());
		classes.addAll(getGenericCustomViews());
		classes.addAll(getGenericFragments());
		classes.addAll(getGenericModels());
		classes.addAll(getGenericNetwork());
		classes.addAll(getGenericUtil());
		classes.addAll(getGenericViewholder());

		return classes;
	}

	private List<AbstractModelClass> getGenericActivities() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new AbstractMainActivity(model.getPackageNameLib()));
		classes.add(new ResourceActivity(model.getPackageNameLib()));
		classes.add(new ResourceDetailActivity(model.getPackageNameLib()));

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

	private List<AbstractModelClass> getGenericModels() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new Link(model.getPackageNameLib()));
		classes.add(new Resource(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericNetwork() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new HeaderParser(model.getPackageNameLib()));
		classes.add(new NetworkCallback(model.getPackageNameLib()));
		classes.add(new NetworkClient(model.getPackageNameLib()));
		classes.add(new NetworkRequest(model.getPackageNameLib()));
		classes.add(new NetworkResponse(model.getPackageNameLib()));
		classes.add(new OKHttpSingleton(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericUtil() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new FragmentHandler(model.getPackageNameLib()));
		classes.add(new GensonBuilder(model.getPackageNameLib()));
		classes.add(new ScrollListener(model.getPackageNameLib()));

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
		classes.addAll(getSpecificModels());
		classes.addAll(getSpecificViewholder());

		return classes;
	}

	private List<AbstractModelClass> getSpecificAdapter() {
		return model.getAppResources().stream()
				.map(appResource -> new ListAdapterGenerator(model.getPackageNameLib(), appResource.getResourceName())).collect(Collectors.toList());
	}

	private List<AbstractModelClass> getSpecificCustomViews() {
		return model.getAppResources().stream()
				.map(appResource -> new ResourceCardViewGenerator(model.getPackageNameLib(), appResource)).collect(Collectors.toList());
	}

	private List<AbstractModelClass> getSpecificModels() {
		return model.getAppResources().stream()
				.map(appResource -> new ResourceGenerator(model.getPackageNameLib(), appResource)).collect(Collectors.toList());
	}


	private List<AbstractModelClass> getSpecificViewholder() {
		return model.getAppResources().stream()
				.map(appResource -> new ListViewHolderGenerator(model.getPackageNameLib(), appResource)).collect(Collectors.toList());
	}

	private void copyDrawableFolders() {
		Copy copy = new Copy();
		copy.copySubFolderOnly("drawable_lib", model.getApplicationName().toLowerCase() + "_lib/src/main/res");
	}
}