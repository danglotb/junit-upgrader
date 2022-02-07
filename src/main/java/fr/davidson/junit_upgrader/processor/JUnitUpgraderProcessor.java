package fr.davidson.junit_upgrader.processor;

import fr.davidson.junit_upgrader.utils.Utils;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtSuperAccess;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 05/02/2022
 */
public class JUnitUpgraderProcessor extends AbstractProcessor<CtClass<?>> {

    @Override
    public void process(CtClass<?> ctClass) {
        final Factory factory = ctClass.getFactory();
        final CtPackage jupiterApiPackage = factory.createPackage();
        jupiterApiPackage.setSimpleName(Utils.ORG_JUNIT_JUPITER_API);
        final CtPackageReference jupiterApiPackageReference = jupiterApiPackage.getReference();

        if (Utils.isJUnit3WithLookUp(ctClass)) {
            junit3Transformation(ctClass, jupiterApiPackage);
        }

        // Common JUnit 3 and JUnit 4 : replace Assertions
        this.replaceAssertionsClass(ctClass, jupiterApiPackageReference, Utils.JUNIT_FRAMEWORK_TEST_CASE);
    }

    private void junit3Transformation(CtClass<?> ctClass, CtPackage jupiterApiPackage) {
        // JUnit 3 Transformation
        // Remove extends TestCase
        if (Utils.isJUnit3(ctClass)) {
            ctClass.getElements(new TypeFilter<>(CtInvocation.class) {
                @Override
                public boolean matches(CtInvocation candidate) {
                    return candidate.getTarget() instanceof CtSuperAccess;
                }
            }).forEach(superInvocation ->
                    superInvocation.getParent(CtBlock.class).removeStatement(superInvocation)
            );
            ctClass.setSuperclass(null);
        }
        // Replace @Override for setUp and tearDown by
        final List<CtMethod<?>> setUps = ctClass.getMethodsByName("setUp");
        if (!setUps.isEmpty()) {
            final CtMethod<?> setUp = setUps.get(0);
            replaceAnnotation(setUp, jupiterApiPackage, "BeforeEach", "java.lang.Override");
        }
        final List<CtMethod<?>> tearDowns = ctClass.getMethodsByName("tearDown");
        if (!tearDowns.isEmpty()) {
            final CtMethod<?> tearDown = tearDowns.get(0);
            replaceAnnotation(tearDown, jupiterApiPackage, "AfterEach", "java.lang.Override");
        }

        // add @Test to all test methods
        final Factory factory = ctClass.getFactory();
        final CtTypeReference annotationType = factory.createAnnotationType(jupiterApiPackage, "Test").getReference();
        final CtAnnotation<?> annotation = factory.createAnnotation(annotationType);
        ctClass.getElements(new TypeFilter<>(CtMethod.class) {
            @Override
            public boolean matches(CtMethod candidate) {
                return candidate.getSimpleName().startsWith("test");
            }
        }).forEach(testMethod -> testMethod.addAnnotation(annotation));
    }

    private void replaceAnnotation(
            final CtMethod<?> method,
            final CtPackage jupiterApiPackage,
            final String annotationNameToAdd,
            final String annotationNameToRemove) {
        final Factory factory = method.getFactory();
        final CtTypeReference annotationType = factory.createAnnotationType(jupiterApiPackage, annotationNameToAdd).getReference();
        final CtAnnotation<?> annotation = factory.createAnnotation(annotationType);
        method.getAnnotations()
                .stream()
                .filter(ctAnnotation -> annotationNameToRemove.equals(ctAnnotation.getAnnotationType().getQualifiedName()))
                .findFirst()
                .ifPresentOrElse(
                        ctAnnotation -> ctAnnotation.replace(annotation),
                        () -> method.addAnnotation(annotation)
                );
    }

    private void replaceAssertionsClass(
            CtClass<?> ctClass,
            final CtPackageReference jupiterApiPackageReference,
            final String fullQualifiedNameToReplace
    ) {
        ctClass.getElements(new TypeFilter<>(CtTypeReference.class) {
            @Override
            public boolean matches(CtTypeReference element) {
                return fullQualifiedNameToReplace.equals(element.getQualifiedName());
            }
        }).forEach(ctTypeReference -> {
            ctTypeReference.setSimpleName(Utils.ASSERTIONS);
            ctTypeReference.setPackage(jupiterApiPackageReference);
        });
    }
}
