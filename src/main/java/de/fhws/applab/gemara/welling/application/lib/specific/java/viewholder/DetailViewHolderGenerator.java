package de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DetailViewHolderGenerator extends AbstractModelClass {

	private final DetailView detailView;
	private final String resourceName;

	private final ClassName resourceDetailCardViewClassName;
	private final ClassName rClassName;
	private final ClassName specificResourceClassName;

	private final FieldSpec resourceDetailCardView;

	public DetailViewHolderGenerator(String packageName, DetailView detailView) {
		super(packageName + ".specific.viewholder", detailView.getResourceName() + "DetailViewHolder");
		this.detailView = detailView;
		this.resourceName = detailView.getResourceName();

		this.resourceDetailCardViewClassName = ClassName.get(packageName + ".specific.customView", resourceName + "DetailCardView");
		this.rClassName = ClassName.get(packageName, "R");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", resourceName);

		this.resourceDetailCardView = FieldSpec.builder(resourceDetailCardViewClassName, "cardView", Modifier.PRIVATE, Modifier.FINAL).build();
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getViewHolderClassName())
				.addField(resourceDetailCardView)
				.addMethod(constructor())
				.addMethod(getAssignData())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec constructor() {
		//todo add onclicklistener for items
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getViewClassName(), "itemView")
				.addParameter(getViewOnClickListenerClassName(), "listener")
				.addStatement("super($N)", "itemView")
				.addStatement("this.$N = ($T) $N.findViewById($T.id.$N)", resourceDetailCardView, resourceDetailCardViewClassName, "itemView", rClassName, resourceName.toLowerCase() + "_detail_card")
				.build();
	}

	private MethodSpec getAssignData() {
		return MethodSpec.methodBuilder("assignData")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(specificResourceClassName, resourceName.toLowerCase())
				.addStatement("this.$N.setUpView($N)", resourceDetailCardView, resourceName.toLowerCase())
				.build();
	}
}
