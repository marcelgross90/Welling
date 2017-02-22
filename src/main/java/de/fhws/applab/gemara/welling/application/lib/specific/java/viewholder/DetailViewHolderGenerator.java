package de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.Category;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.visitors.ContainsSubResourceVisitor;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getButtonClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewHolderClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewOnClickListenerClassName;

public class DetailViewHolderGenerator extends AbstractModelClass {

	private final DetailView detailView;
	private final String resourceName;

	private final ClassName resourceDetailCardViewClassName;
	private final ClassName rClassName;
	private final ClassName specificResourceClassName;
	private final ClassName buttonClassName;

	private final FieldSpec resourceDetailCardView;

	public DetailViewHolderGenerator(String packageName, DetailView detailView) {
		super(packageName + ".specific.viewholder", detailView.getResourceName() + "DetailViewHolder");
		this.detailView = detailView;
		this.resourceName = detailView.getResourceName();

		this.resourceDetailCardViewClassName = ClassName.get(packageName + ".specific.customView", resourceName + "DetailCardView");
		this.rClassName = ClassName.get(packageName, "R");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", resourceName);
		this.buttonClassName = getButtonClassName();

		this.resourceDetailCardView = FieldSpec.builder(resourceDetailCardViewClassName, "cardView", Modifier.PRIVATE, Modifier.FINAL)
				.build();
	}

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getViewHolderClassName())
				.addField(resourceDetailCardView)
				.addMethod(constructor())
				.addMethod(getAssignData())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec constructor() {
		ContainsSubResourceVisitor visitor = new ContainsSubResourceVisitor();

		//todo add onclicklistener for items
		MethodSpec.Builder method = MethodSpec.constructorBuilder();
		method.addModifiers(Modifier.PUBLIC);
		method.addParameter(getViewClassName(), "itemView");
		method.addParameter(getViewOnClickListenerClassName(), "listener");
		method.addStatement("super($N)", "itemView");
		method.addStatement("this.$N = ($T) $N.findViewById($T.id.$N)", resourceDetailCardView, resourceDetailCardViewClassName, "itemView",
				rClassName, resourceName.toLowerCase() + "_detail_card");

		for (Category category : detailView.getCategories()) {
			for (ResourceViewAttribute resourceViewAttribute : category.getResourceViewAttributes()) {
				resourceViewAttribute.accept(visitor);
				if (visitor.isContainsSubResource()) {
					method.addStatement("$T $N = ($T) $N.findViewById($T.id.$N)", buttonClassName, visitor.getViewName() + "_btn",
							buttonClassName, "itemView", rClassName, "tv" + getInputWithCapitalStart(visitor.getViewName()) + "Value");
					method.addStatement("$N.setOnClickListener($N)", visitor.getViewName() + "_btn", "listener");
				}
			}
		}

		return method.build();
	}

	private MethodSpec getAssignData() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("assignData");
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addParameter(specificResourceClassName, resourceName.toLowerCase());
		method.addStatement("this.$N.setUpView($N)", resourceDetailCardView, resourceName.toLowerCase());

		return method.build();
	}

	private String getInputWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
}