package fr.davidson.junit_upgrader.processor;

import fr.davidson.junit_upgrader.utils.Utils;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtSuperAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class SubJUnit3UpgraderProcessor extends SubJUnitUpgraderProcessor {

    public static final String SET_UP_METHOD_NAME = "setUp";

    public static final String TEAR_DOWN_METHOD_NAME = "tearDown";

    public SubJUnit3UpgraderProcessor(CtPackage jupiterApiPackage, CtPackageReference jupiterApiPackageReference) {
        super(jupiterApiPackage, jupiterApiPackageReference);
    }

    @Override
    public void process(CtClass<?> classToProcess) {
        // JUnit 3 Transformation
        // Remove extends TestCase
        if (Utils.isJUnit3(classToProcess)) {
            classToProcess.getElements(new TypeFilter<>(CtInvocation.class) {
                @Override
                public boolean matches(CtInvocation candidate) {
                    return candidate.getTarget() instanceof CtSuperAccess;
                }
            }).forEach(superInvocation ->
                    superInvocation.getParent(CtBlock.class).removeStatement(superInvocation)
            );
            classToProcess.setSuperclass(null);
        }
        // Replace @Override for setUp and tearDown by
        final List<CtMethod<?>> setUps = classToProcess.getMethodsByName(SET_UP_METHOD_NAME);
        if (!setUps.isEmpty()) {
            final CtMethod<?> setUp = setUps.get(0);
            Utils.replaceAnnotation(setUp, jupiterApiPackage, Utils.BEFORE_EACH_ANNOTATION_NAME, Utils.OVERRIDE_ANNOTATION_FULL_QUALIFIED_NAME);
        }
        final List<CtMethod<?>> tearDowns = classToProcess.getMethodsByName(TEAR_DOWN_METHOD_NAME);
        if (!tearDowns.isEmpty()) {
            final CtMethod<?> tearDown = tearDowns.get(0);
            Utils.replaceAnnotation(tearDown, jupiterApiPackage, Utils.AFTER_EACH_ANNOTATION_NAME, Utils.OVERRIDE_ANNOTATION_FULL_QUALIFIED_NAME);
        }

        // add @Test to all test methods
        final Factory factory = classToProcess.getFactory();
        final CtTypeReference annotationType = factory.createAnnotationType(jupiterApiPackage, Utils.TEST_ANNOTATION_NAME).getReference();
        final CtAnnotation<?> annotation = factory.createAnnotation(annotationType);
        classToProcess.getElements(new TypeFilter<>(CtMethod.class) {
            @Override
            public boolean matches(CtMethod candidate) {
                return candidate.getSimpleName().startsWith(Utils.PREFIX_TEST_JUNIT3);
            }
        }).forEach(testMethod -> testMethod.addAnnotation(annotation));
    }
}
