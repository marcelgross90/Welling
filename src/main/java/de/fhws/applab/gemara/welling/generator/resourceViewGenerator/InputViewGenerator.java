package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.attributes.Attribute;
import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.welling.application.app.java.NoPictureActivityGenerator;
import de.fhws.applab.gemara.welling.application.app.java.PictureActivityGenerator;
import de.fhws.applab.gemara.welling.application.app.java.fragment.EditSpecificResourceFragment;
import de.fhws.applab.gemara.welling.application.app.java.fragment.NewSpecificResourceWithPictureFragment;
import de.fhws.applab.gemara.welling.application.app.java.fragment.NewSpecificResourceWithoutPictureFragment;
import de.fhws.applab.gemara.welling.application.app.res.layout.EditResourceFragmentViewGenerator;
import de.fhws.applab.gemara.welling.application.app.res.layout.NewResourceFragmentViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.ResourceActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.AttributeInput;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.DateTimeView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ResourceInputView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DateTimePickerFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.EditResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.NewResourceFragment;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.TextinputAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.menu.SaveMenu;
import de.fhws.applab.gemara.welling.application.lib.specific.res.layout.InputLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.visitors.IAttributeVisitorImpl;
import de.fhws.applab.gemara.welling.visitors.ResourceViewVisitorImpl;

import java.util.ArrayList;
import java.util.List;

public class InputViewGenerator extends ResourceViewGenerator<InputView> {

	private final String appResDirectory;
	private final ResourceViewVisitorImpl.InputType inputType;

	public InputViewGenerator(InputView inputView, AppDescription appDescription, StateHolder stateHolder, ResourceViewVisitorImpl.InputType inputType) {
		super(inputView, appDescription, stateHolder);

		this.appResDirectory = appDescription.getAppResDirectory();
		this.inputType = inputType;
	}

	@Override
	protected List<AbstractModelClass> getLibJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new ResourceInputView(libPackageName));
		classes.add(new de.fhws.applab.gemara.welling.application.lib.specific.java.customView.InputViewsGenerator(appDescription, resourceView));
		classes.add(new AttributeInput(appDescription));

		if (inputType == ResourceViewVisitorImpl.InputType.POST) {
			classes.add(new NewResourceFragment(libPackageName));
		}

		if (inputType == ResourceViewVisitorImpl.InputType.PUT) {
			classes.add(new ResourceActivity(libPackageName));
			classes.add(new EditResourceFragment(libPackageName));
		}

		for (InputViewAttribute inputViewAttribute : resourceView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				classes.add(new DateTimePickerFragment(libPackageName));
				classes.add(new DateTimeView(appDescription));
				break;
			}
		}


		return classes;
	}

	@Override
	protected List<GeneratedFile> getLibXMLClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new SaveMenu(appDescription));
		classes.add(new InputLayoutGenerator(appDescription, resourceView));
		classes.add(new TextinputAttribute(libResDirectory));

		if (inputType == ResourceViewVisitorImpl.InputType.POST) {
			classes.add(new NewResourceFragmentViewGenerator(appResDirectory, resourceView.getResourceName(), libPackageName));
		}

		if (inputType == ResourceViewVisitorImpl.InputType.PUT) {
			classes.add(new EditResourceFragmentViewGenerator(appResDirectory, resourceView.getResourceName(), libPackageName));
		}


		return classes;
	}

	@Override
	protected List<AbstractModelClass> getAppJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		if (inputType == ResourceViewVisitorImpl.InputType.POST) {
			if (containsImage()) {
				classes.add(new NewSpecificResourceWithPictureFragment(appDescription, resourceView));
			} else {
				classes.add(new NewSpecificResourceWithoutPictureFragment(appDescription, resourceView));
			}
		}
		if (inputType == ResourceViewVisitorImpl.InputType.PUT) {
			if (containsImage()) {
				classes.add(new PictureActivityGenerator(appDescription, resourceView.getResourceName()));
			} else {
				classes.add(new NoPictureActivityGenerator(appDescription, resourceView.getResourceName()));
			}

			classes.add(new EditSpecificResourceFragment(appDescription, resourceView));
		}



		return classes;
	}

	@Override
	protected void addStrings() {

	}

	private boolean containsImage() {
		IAttributeVisitorImpl visitor = new IAttributeVisitorImpl();
		SingleResource singleResource = appDescription.getResource(resourceView.getResourceName()).getResource();
		for (Attribute attribute : singleResource.getAllAttributes()) {
			attribute.generate(visitor);
			if (visitor.isContainsImage()) {
				return true;
			}
		}
		return false;
	}
}
