package de.fhws.applab.gemara.welling;

import com.squareup.javapoet.JavaFile;
import de.fhws.applab.gemara.welling.lib.generic.adapter.ResourceListAdapter;
import de.fhws.applab.gemara.welling.lib.generic.model.Link;
import de.fhws.applab.gemara.welling.lib.generic.model.Resource;
import de.fhws.applab.gemara.welling.lib.generic.network.HeaderParser;
import de.fhws.applab.gemara.welling.lib.generic.network.NetworkCallback;
import de.fhws.applab.gemara.welling.lib.generic.network.NetworkClient;
import de.fhws.applab.gemara.welling.lib.generic.network.NetworkRequest;
import de.fhws.applab.gemara.welling.lib.generic.network.NetworkResponse;
import de.fhws.applab.gemara.welling.lib.generic.network.OKHttpSingleton;
import de.fhws.applab.gemara.welling.lib.generic.util.FragmentHandler;
import de.fhws.applab.gemara.welling.lib.generic.util.GensonBuilder;
import de.fhws.applab.gemara.welling.lib.generic.util.ScrollListener;
import de.fhws.applab.gemara.welling.lib.generic.viewholder.ResourceViewHolder;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcelgross on 02.12.16.
 */
public class Main {
	private static final AndroidMetaModel model = new AndroidMetaModel("de.fhws.applab.gemara", "Lecturer");

	public static void main(String[] args) {
		//writeFiles(getClasses());
		test();
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
		ResourceListAdapter classs = new ResourceListAdapter(model.getPackageName(), "ResourceListAdapter");

		JavaFile javaFile = classs.javaFile();

		try {
			javaFile.writeTo(System.out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private static List<AbstractModelClass> getClasses() {

		List<AbstractModelClass> list = new ArrayList<>();
		AppCompatActivityClass appCompatActivityClass = new AppCompatActivityClass();
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

		return list;
	}

	private static void writeFiles(List<AbstractModelClass> classes) {
		String baseDir = "generated/" + model.getApplicationName().toLowerCase() + "_lib/src/main/";
		for (AbstractModelClass aClass : classes) {
			File file = new File(baseDir);
			try {
				aClass.javaFile().writeTo(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

}