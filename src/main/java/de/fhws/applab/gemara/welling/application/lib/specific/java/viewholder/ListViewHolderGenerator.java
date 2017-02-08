package de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.visitors.ContainsImageVisitor;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class ListViewHolderGenerator extends AbstractModelClass {

	private final CardView cardView;
	private final String resourceName;

	private final ClassName rClassName;
	private final ClassName resourceViewHolderClassName;
	private final ClassName resourceCardView;
	private final ClassName onResourceClickListenerClassName;
	private final ClassName profileImgClassName;
	private final ClassName resourceClassName;
	private final ClassName resourceTypeClassName;

	private final FieldSpec cardViewField;
	private final FieldSpec onResourceClickListener;
	private final FieldSpec profileImg;

	public ListViewHolderGenerator(String packageName, CardView cardView) {
		super(packageName + ".specific.viewholder", cardView.getResourceName() + "ListViewHolder");
		this.cardView = cardView;
		this.resourceName = cardView.getResourceName();

		this.rClassName = ClassName.get(packageName, "R");
		this.resourceViewHolderClassName = ClassName.get(packageName + ".generic.viewholder", "ResourceViewHolder");
		this.resourceCardView = ClassName.get(packageName + ".specific.customView", resourceName + "CardView");
		this.onResourceClickListenerClassName = ClassName.get(packageName + ".generic.adapter.ResourceListAdapter", "OnResourceClickListener");
		this.profileImgClassName = ClassName.get(packageName + ".generic.customView", "ProfileImageView");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.resourceTypeClassName = ClassName.get(packageName + ".specific.model", resourceName);
		this.cardViewField = FieldSpec.builder(resourceCardView, "cardViewField", Modifier.PRIVATE, Modifier.FINAL).build();
		this.onResourceClickListener = FieldSpec.builder(onResourceClickListenerClassName, "onResourceClickListener", Modifier.PRIVATE, Modifier.FINAL).build();

		this.profileImg = FieldSpec.builder(profileImgClassName, "profileImg", Modifier.PRIVATE, Modifier.FINAL).build();
	}


	@Override
	public JavaFile javaFile() {
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addModifiers(Modifier.PUBLIC);
		type.superclass(resourceViewHolderClassName);
		type.addField(cardViewField);
		type.addField(onResourceClickListener);
		ContainsImageVisitor visitor = new ContainsImageVisitor();
		boolean containsImage = false;
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			containsImage = visitor.isContainsImage();
			if (containsImage) {
				break;
			}
		}
		if (containsImage) {
			type.addField(profileImg);
		}
		type.addMethod(getConstructor());
		type.addMethod(getAssignData());

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private MethodSpec getConstructor() {
		ParameterSpec itemViewParam = ParameterSpec.builder(getViewClassName(), "itemView").build();
		ParameterSpec clickListenerParam = ParameterSpec.builder(onResourceClickListenerClassName, "onLecturerClickListener").build();
		MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
		constructor.addModifiers(Modifier.PUBLIC);
		constructor.addParameter(itemViewParam);
		constructor.addParameter(clickListenerParam);
		constructor.addStatement("super($N)", itemViewParam);
		constructor.addStatement("this.$N = $N", onResourceClickListener, clickListenerParam);
		constructor.addStatement("$N = ($T) $N.findViewById($T.id.$N)", cardViewField, resourceCardView, itemViewParam, rClassName, this.resourceName.toLowerCase() + "_card");

		ContainsImageVisitor visitor = new ContainsImageVisitor();
		boolean containsImage = false;
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			containsImage = visitor.isContainsImage();
			if (containsImage) {
				break;
			}
		}
		if (containsImage) {
			constructor.addStatement("$N = ($T) $N.findViewById($T.id.profileImg)", profileImg, profileImgClassName, itemViewParam, rClassName);
		}

		return constructor.build();
	}

	private MethodSpec getAssignData() {
		ParameterSpec resource = ParameterSpec.builder(resourceClassName, "resource", Modifier.FINAL).build();
		return MethodSpec.methodBuilder("assignData")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(resource)
				.addStatement("final $T $N = ($T) $N", resourceTypeClassName, resourceName.toLowerCase(), resourceTypeClassName, resource)
				.addStatement("$N.setOnClickListener($L);", cardViewField, getOnClick())
				.addStatement("$N.setUpView($N)", cardViewField, resourceName.toLowerCase())
				.build();
	}

	private TypeSpec getOnClick() {
		MethodSpec onClick = MethodSpec.methodBuilder("onClick")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(getViewClassName(), "view")
				.beginControlFlow("if ($N != null)", onResourceClickListener)
				.addStatement("$N.onResourceClickWithView($N, $N)", onResourceClickListener, resourceName.toLowerCase(), profileImg)
				.addStatement("$N.onResourceClick($N)", onResourceClickListener, resourceName.toLowerCase())
				.endControlFlow()
				.build();

		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(getViewOnClickListenerClassName())
				.addMethod(onClick)
				.build();
	}
}
