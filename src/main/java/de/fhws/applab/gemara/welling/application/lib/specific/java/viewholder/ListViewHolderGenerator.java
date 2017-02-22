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
import de.fhws.applab.gemara.welling.visitors.ClickActionCardViewVisitor;
import de.fhws.applab.gemara.welling.visitors.ClickableFieldVisitor;
import de.fhws.applab.gemara.welling.visitors.ContainsImageVisitor;
import de.fhws.applab.gemara.welling.visitors.InitializeClickableAttributesCardViewVisitor;
import de.fhws.applab.gemara.welling.visitors.SetOnClickListenerVisitor;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextClass;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewOnClickListenerClassName;

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
	private final ClassName attributeViewClassName;

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
		this.onResourceClickListenerClassName = ClassName
				.get(packageName + ".generic.adapter.ResourceListAdapter", "OnResourceClickListener");
		this.profileImgClassName = ClassName.get(packageName + ".generic.customView", "ProfileImageView");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.resourceTypeClassName = ClassName.get(packageName + ".specific.model", resourceName);
		this.attributeViewClassName = ClassName.get(packageName + ".generic.customView", "AttributeView");

		this.cardViewField = FieldSpec.builder(resourceCardView, "cardViewField", Modifier.PRIVATE, Modifier.FINAL).build();
		this.onResourceClickListener = FieldSpec
				.builder(onResourceClickListenerClassName, "onResourceClickListener", Modifier.PRIVATE, Modifier.FINAL).build();
		this.profileImg = FieldSpec.builder(profileImgClassName, "profileImg", Modifier.PRIVATE, Modifier.FINAL).build();
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addSuperinterface(getViewOnClickListenerClassName());
		type.addModifiers(Modifier.PUBLIC);
		type.superclass(resourceViewHolderClassName);
		type.addField(resourceTypeClassName, resourceName.toLowerCase(), Modifier.PRIVATE);
		type.addField(cardViewField);
		type.addField(onResourceClickListener);
		type.addField(getContextClass(), "context", Modifier.PRIVATE, Modifier.FINAL);

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
		type.addMethod(getOnClick());

		addFieldsClickableAttributes(type);

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private void addFieldsClickableAttributes(TypeSpec.Builder type) {
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(new ClickableFieldVisitor(type, attributeViewClassName));
		}
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
		constructor.addStatement("this.$N = $N.getContext()", "context", itemViewParam);
		constructor.addStatement("$N = ($T) $N.findViewById($T.id.$N)", cardViewField, resourceCardView, itemViewParam, rClassName,
				this.resourceName.toLowerCase() + "_card");

		ContainsImageVisitor visitor = new ContainsImageVisitor();
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			if (visitor.isContainsImage()) {
				constructor.addStatement("$N = ($T) $N.findViewById($T.id.profileImg)", profileImg, profileImgClassName, itemViewParam,
						rClassName);
			}
		}

		addInitializeClickableAttributes(constructor, "itemView");

		return constructor.build();
	}

	@SuppressWarnings("SameParameterValue")
	private void addInitializeClickableAttributes(MethodSpec.Builder method, String viewName) {
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(new InitializeClickableAttributesCardViewVisitor(method, viewName, attributeViewClassName, rClassName));
		}
	}

	private MethodSpec getAssignData() {
		ParameterSpec resource = ParameterSpec.builder(resourceClassName, "resource", Modifier.FINAL).build();

		MethodSpec.Builder method = MethodSpec.methodBuilder("assignData");
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addAnnotation(Override.class);
		method.addParameter(resource);
		method.addStatement("final $T $N = ($T) $N", resourceTypeClassName, resourceName.toLowerCase(), resourceTypeClassName, resource);
		method.addStatement("$N.setOnClickListener($L)", cardViewField, getInnerOnClick());
		method.addStatement("$N.setUpView($N)", cardViewField, resourceName.toLowerCase());
		method.addStatement("this.$N = $N", resourceName.toLowerCase(), resourceName.toLowerCase());

		addOnClickListener(method);

		return method.build();
	}

	private void addOnClickListener(MethodSpec.Builder method) {
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(new SetOnClickListenerVisitor(method, "this"));
		}
	}

	private TypeSpec getInnerOnClick() {
		MethodSpec.Builder onClick = MethodSpec.methodBuilder("onClick");
		onClick.addModifiers(Modifier.PUBLIC).returns(void.class);
		onClick.addAnnotation(Override.class);
		onClick.addParameter(getViewClassName(), "view");
		onClick.beginControlFlow("if ($N != null)", onResourceClickListener);

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
			onClick.addStatement("$N.onResourceClickWithView($N, $N)", onResourceClickListener, resourceName.toLowerCase(), profileImg);
		} else {
			onClick.addStatement("$N.onResourceClickWithView($N, $N)", onResourceClickListener, resourceName.toLowerCase(), "null");
		}
		onClick.addStatement("$N.onResourceClick($N)", onResourceClickListener, resourceName.toLowerCase());
		onClick.endControlFlow();

		// @formatter:off
		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(getViewOnClickListenerClassName())
				.addMethod(onClick.build())
				.build();
		// @formatter:on
	}

	private MethodSpec getOnClick() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("onClick");
		method.addAnnotation(Override.class);
		method.returns(void.class);
		method.addModifiers(Modifier.PUBLIC);
		method.addParameter(getViewClassName(), "view");
		method.addStatement("$T $N = $N.getId()", int.class, "i", "view");

		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute
					.accept(new ClickActionCardViewVisitor(method, "i", rClassName, cardView.getResourceName().toLowerCase(), true));
		}

		return method.build();
	}
}