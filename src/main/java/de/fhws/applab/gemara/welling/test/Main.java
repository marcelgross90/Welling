package de.fhws.applab.gemara.welling.test;

import com.squareup.javapoet.JavaFile;
import de.fhws.applab.gemara.dalston.generator.GeneratedFile;
import de.fhws.applab.gemara.welling.AbstractModelClass;
import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.AbstractMainActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceCardView;
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
import de.fhws.applab.gemara.welling.application.lib.specific.adapter.ListAdapterGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.model.ResourceGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.viewholder.ListViewHolderGenerator;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.metaModel.InputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcelgross on 02.12.16.
 */
public class Main {
	private static AndroidMetaModel model;

	public static void main(String[] args) throws InputException{
		model = MetaModelGenerator.generateMetaModel();

		writeFiles();
		//test();
	}

	private static void test() {
		/*AppCompatActivityClass classs = new AppCompatActivityClass();
		classs.setClassName("MainActivity");
		classs.setPackageName(packageName);*/

		//OKHttpSingleton classs = new OKHttpSingleton(packageName, "OKHttpSingleton");
		//NetworkCallback classs = new NetworkCallback(packageName, "NetworkCallback");
		//	NetworkResponse classs = new NetworkResponse(packageName, "NetworkResponse");
		//NetworkRequest classs = new NetworkRequest(packageName, "NetworkRequest");
		//NetworkClient classs = new NetworkClient(packageName, "NetworkClient");
		//		HeaderParser classs = new HeaderParser(packageName, "HeaderParser");
		//		FragmentHandler classs = new FragmentHandler(packageName, "FragmentHandler");
		//GensonBuilder classs = new GensonBuilder(packageName, "GensonBuilder");
		//		ScrollListener classs = new ScrollListener(packageName, "ScrollListener");
//		Link classs = new Link(model.getPackageName(), "Link");
//		Resource classs = new Resource(model.getPackageName(), "Resource");
		//ResourceViewHolder classs = new ResourceViewHolder(model.getPackageName(), "ResourceViewHolder");
	//	ResourceListAdapter classs = new ResourceListAdapter(model.getPackageName(), "ResourceListAdapter");
	//	DateTimePickerFragment classs = new DateTimePickerFragment(model.getPackageName(), "DateTimePickerFragment");
	//	DeleteDialogFragment classs = new DeleteDialogFragment(model.getPackageName(), "DeleteDialogFragment");
		//AttributeInput classs = new AttributeInput(model.getPackageName(), "AttributeInput");
		//AttributeView classs = new AttributeView(model.getPackageName(), "AttributeView");
		//DateTimeView classs = new DateTimeView(model.getPackageName(), "DateTimeView");
//		ProfileImageView classs = new ProfileImageView(model.getPackageName(), "ProfileImageView");
//		ResourceDetailView classs = new ResourceDetailView(model.getPackageName(), "ResourceDetailView");
//		ResourceInputView classs = new ResourceInputView(model.getPackageName(), "ResourceInputView");
	//	ResourceActivity classs = new ResourceActivity(model.getPackageName(), "ResourceActivity");
		AbstractMainActivity classs = new AbstractMainActivity(model.getPackageName(), "AbstractMainActivity");
//		ResourceDetailActivity classs = new ResourceDetailActivity(model.getPackageName(), "ResourceDetailActivity");

		JavaFile javaFile = classs.javaFile();

		try {
			javaFile.writeTo(System.out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private static List<AbstractModelClass> getJavaClasses() {

		List<AbstractModelClass> list = new ArrayList<>();
		/*AppCompatActivityClass appCompatActivityClass = new AppCompatActivityClass();
		appCompatActivityClass.setClassName("MainActivity");
		appCompatActivityClass.setPackageName(model.getPackageName());
		list.add(appCompatActivityClass);
		list.add(new OKHttpSingleton(model.getPackageName(), "OKHttpSingleton"));
		list.add(new NetworkCallback(model.getPackageName(), "NetworkCallback"));
		list.add(new NetworkResponse(model.getPackageName(), "NetworkResponse"));
		list.add(new NetworkRequest(model.getPackageName(), "NetworkRequest"));
		list.add(new NetworkClient(model.getPackageName(), "NetworkClient"));
		list.add(new HeaderParser(model.getPackageName(), "HeaderParser"));
		list.add(new FragmentHandler(model.getPackageName(), "FragmentHandler"));
		list.add(new GensonBuilder(model.getPackageName(), "GensonBuilder"));
		list.add(new ScrollListener(model.getPackageName(), "ScrollListener"));
		list.add(new Link(model.getPackageName(), "Link"));
		list.add(new Resource(model.getPackageName(), "Resource"));
		list.add(new ResourceViewHolder(model.getPackageName(), "ResourceViewHolder"));
		list.add(new DateTimePickerFragment(model.getPackageName(), "DateTimePickerFragment"));
		list.add(new DeleteDialogFragment(model.getPackageName(), "DeleteDialogFragment"));
		list.add(new AttributeInput(model.getPackageName(), "AttributeInput"));
		list.add(new AttributeView(model.getPackageName(), "AttributeView"));
		list.add(new DateTimeView(model.getPackageName(), "DateTimeView"));
		list.add(new ProfileImageView(model.getPackageName(), "ProfileImageView"));
		list.add(new ResourceDetailView(model.getPackageName(), "ResourceDetailView"));
		list.add(new ResourceInputView(model.getPackageName(), "ResourceInputView"));
		list.add(new ResourceActivity(model.getPackageName(), "ResourceActivity"));
		list.add(new ResourceListAdapter(model.getPackageName(), "ResourceListAdapter"));
		list.add(new ResourceGenerator(model.getPackageName(), model.getAppResources().get(0)));
		list.add(new ResourceCardView(model.getPackageName(), "ResourceCardView"));
		list.add(new ListAdapterGenerator(model.getPackageName(), model.getAppResources().get(0).getResourceName()));*/
		list.add(new ListViewHolderGenerator(model.getPackageName(), model.getAppResources().get(0)));
		return list;
	}

	private static List<GeneratedFile> getXMLClasses() {
		String baseDir = "generated/" + model.getApplicationName().toLowerCase() + "_lib/src/main/";
		String resDir = baseDir + "res/";
		List<GeneratedFile> list = new ArrayList<>();
		list.add(new DetailMenu(resDir));
		list.add(new ListMenu(resDir));
		list.add(new SaveMenu(resDir));

		list.add(new Attr(resDir, model.getAppDeclareStyleable()));
		list.add(new Colors(resDir, model.getAppColor()));
		list.add(new Dimens(resDir));
		list.add(new RestApi(resDir, model.getAppRestAPI()));
		list.add(new Styles(resDir, model.getLibStyles()));
		list.add(new Strings(resDir, model.getLibStrings()));
		list.add(new ManifestGenerator(baseDir, model.getLibManifest()));

		list.add(new ActivityMain(resDir));
		list.add(new FragmentResourceList(resDir));
		list.add(new TextinputAttribute(resDir));
		list.add(new Toolbar(resDir));

		return list;
	}

	private static void writeFiles() {
		writeJavaFiles(getJavaClasses());
		writeXMLFiles(getXMLClasses());
	}

	private static void writeJavaFiles(List<AbstractModelClass> classes) {
		String baseDir = "generated/" + model.getApplicationName().toLowerCase() + "_lib/src/main/java/";
		for (AbstractModelClass aClass : classes) {
			File file = new File(baseDir);
			try {
				aClass.javaFile().writeTo(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	private static void writeXMLFiles(List<GeneratedFile> files) {
		for (GeneratedFile file : files) {
			file.generateAndWrite();
		}
	}

}
