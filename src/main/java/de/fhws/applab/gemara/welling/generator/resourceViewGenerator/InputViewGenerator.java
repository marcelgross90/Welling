package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.application.app.java.fragment.NewSpecificResourceFragment;
import de.fhws.applab.gemara.welling.application.app.res.layout.NewResourceFragmentViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.AttributeInput;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceInputView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.NewResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.TextinputAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.SaveMenu;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.InputLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.test.modelGenerator.InputViewModelGenerator;

import java.util.ArrayList;
import java.util.List;

public class InputViewGenerator extends ResourceViewGenerator<InputView> {

	//todo find out if post or put

	private final String appPackageName;
	private final String appResDirectory;
	private final String appName;

	public InputViewGenerator(InputView inputView, AppDescription appDescription, StateHolder stateHolder) {
		super(inputView, appDescription, stateHolder);

		this.appPackageName = appDescription.getAppPackageName();
		this.appResDirectory = appDescription.getAppResDirectory();
		this.appName = appDescription.getAppName();
	}

	@Override
	protected List<AbstractModelClass> getLibJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new NewResourceFragment(libPackageName));
		classes.add(new ResourceInputView(libPackageName));
		classes.add(new de.fhws.applab.gemara.welling.application.lib.specific.java.customView.InputViewsGenerator(appDescription, resourceView));
		classes.add(new AttributeInput(appDescription));

		return classes;
	}

	@Override
	protected List<GeneratedFile> getLibXMLClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new SaveMenu(appDescription));
		classes.add(new NewResourceFragmentViewGenerator(appResDirectory, resourceView.getResourceName(), libPackageName));
		classes.add(new InputLayoutGenerator(appDescription, resourceView));
		classes.add(new TextinputAttribute(libResDirectory));

		return classes;
	}

	@Override
	protected List<AbstractModelClass> getAppJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new NewSpecificResourceFragment(appPackageName, resourceView, appName));

		return classes;
	}

	@Override
	protected void addStrings() {

	}

	//todo implement me
}
