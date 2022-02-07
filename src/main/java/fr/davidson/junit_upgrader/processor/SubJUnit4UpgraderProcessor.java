package fr.davidson.junit_upgrader.processor;

import fr.davidson.junit_upgrader.utils.Utils;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtTypeReference;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class SubJUnit4UpgraderProcessor extends SubJUnitUpgraderProcessor {


    public SubJUnit4UpgraderProcessor(CtPackage jupiterApiPackage, CtPackageReference jupiterApiPackageReference) {
        super(jupiterApiPackage, jupiterApiPackageReference);
    }

    @Override
    public void process(CtClass<?> classToProcess) {
        final Factory factory = classToProcess.getFactory();
        final CtPackage orgJUnitPackage = factory.createPackage();
        orgJUnitPackage.setSimpleName(Utils.ORG_JUNIT_PACKAGE_NAME);

        findAndReplaceAnnotation(classToProcess, orgJUnitPackage, Utils.BEFORE_ANNOTATION_NAME, Utils.BEFORE_EACH_ANNOTATION_NAME);
        findAndReplaceAnnotation(classToProcess, orgJUnitPackage, Utils.AFTER_ANNOTATION_NAME, Utils.AFTER_EACH_ANNOTATION_NAME);
        findAndReplaceAnnotation(classToProcess, orgJUnitPackage, Utils.BEFORE_CLASS_ANNOTATION_NAME, Utils.BEFORE_ANNOTATION_NAME);
        findAndReplaceAnnotation(classToProcess, orgJUnitPackage, Utils.AFTER_CLASS_ANNOTATION_NAME, Utils.AFTER_ANNOTATION_NAME);

        findAndReplaceAnnotation(classToProcess, orgJUnitPackage, Utils.TEST_ANNOTATION_NAME, Utils.TEST_ANNOTATION_NAME);
    }

    private void findAndReplaceAnnotation(
            final CtClass<?> classToProcess,
            final CtPackage orgJUnitPackage,
            final String annotationNameToReplace,
            final String fullQualifiedNameAnnotationReplacement
    ) {
        final Factory factory = classToProcess.getFactory();
        final CtTypeReference annotationType = factory.createAnnotationType(orgJUnitPackage, annotationNameToReplace).getReference();
        classToProcess.getMethods().stream()
                .filter(ctMethod -> ctMethod.getAnnotations()
                        .stream()
                        .anyMatch(ctAnnotation ->
                                ctAnnotation.getAnnotationType().getQualifiedName().equals(annotationType.getQualifiedName())
                        )
                ).forEach(ctMethod ->
                        Utils.replaceAnnotation(
                                ctMethod, jupiterApiPackage, fullQualifiedNameAnnotationReplacement, String.join(".", Utils.ORG_JUNIT_PACKAGE_NAME, annotationNameToReplace
                                )
                        )
                );
    }
}
