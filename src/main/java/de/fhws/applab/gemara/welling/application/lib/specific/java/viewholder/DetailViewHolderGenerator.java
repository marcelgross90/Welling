package de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DetailViewHolderGenerator extends AbstractModelClass {

	private final AppResource appResource;

	private final ClassName resourceDetailCardViewClassName;
	private final ClassName rClassName;
	private final ClassName specificResourceClassName;

	private final FieldSpec resourceDetailCardView;

	public DetailViewHolderGenerator(String packageName, AppResource appResource) {
		super(packageName + ".specific.viewholder", appResource.getResourceName() + "DetailViewHolder");
		this.appResource = appResource;

		this.resourceDetailCardViewClassName = ClassName.get(packageName + ".specific.customView", appResource.getResourceName() + "DetailCardView");
		this.rClassName = ClassName.get(packageName, "R");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", appResource.getResourceName());

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
				.addStatement("this.$N = ($T) $N.findViewById($T.id.$N)", resourceDetailCardView, resourceDetailCardViewClassName, "itemView", rClassName, appResource.getResourceName().toLowerCase() + "_detail_card")
				.build();
	}

	private MethodSpec getAssignData() {
		return MethodSpec.methodBuilder("assignData")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(specificResourceClassName, appResource.getResourceName().toLowerCase())
				.addStatement("this.$N.setUpView($N)", resourceDetailCardView, appResource.getResourceName().toLowerCase())
				.build();
	}
}
