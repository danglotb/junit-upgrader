package fr.davidson.junit_upgrader.utils;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.regex.Pattern;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class Utils {

    public static final String ORG_JUNIT_JUPITER_API_PACKAGE_NAME = "org.junit.jupiter.api";

    public static final String ORG_JUNIT_PACKAGE_NAME = "org.junit";

    public static final String FULL_QUALIFIED_NAME_TESTCASE_NAME = "junit.framework.TestCase";

    public static final String BEFORE_EACH_ANNOTATION_NAME = "BeforeEach";

    public static final String BEFORE_ANNOTATION_NAME = "Before";

    public static final String BEFORE_ANNOTATION_JUNIT4_FULL_QUALIFIED_NAME = String.join(".", ORG_JUNIT_PACKAGE_NAME, BEFORE_ANNOTATION_NAME);

    public static final String BEFORE_CLASS_ANNOTATION_NAME = "BeforeClass";

    public static final String AFTER_EACH_ANNOTATION_NAME = "AfterEach";

    public static final String AFTER_ANNOTATION_NAME = "After";

    public static final String AFTER_ANNOTATION_JUNIT4_FULL_QUALIFIED_NAME = String.join(".", ORG_JUNIT_PACKAGE_NAME, AFTER_ANNOTATION_NAME);

    public static final String AFTER_CLASS_ANNOTATION_NAME = "AfterClass";

    public static final String ASSERTIONS_CLASS_NAME = "Assertions";

    public static final String FULL_QUALIFIED_NAME_ASSERT_NAME = "org.junit.Assert";

    public static final String OVERRIDE_ANNOTATION_FULL_QUALIFIED_NAME = "java.lang.Override";

    public static final String TEST_ANNOTATION_NAME = "Test";

    public static final String JUNIT4_TEST_ANNOTATION_FULL_QUALIFIED_NAME = String.join(".", ORG_JUNIT_PACKAGE_NAME, TEST_ANNOTATION_NAME);

    public static final String PREFIX_TEST_JUNIT3 = "test";

    public static boolean isJUnit3WithLookUp(CtType<?> candidate) {
        return isJUnit3(candidate) || (isCandidateToIsJUnit3(candidate) && isJUnit3WithLookUp(candidate.getSuperclass().getTypeDeclaration()));
    }

    public static boolean isJUnit3(CtType<?> candidate) {
        return (isCandidateToIsJUnit3(candidate) && FULL_QUALIFIED_NAME_TESTCASE_NAME.equals(candidate.getSuperclass().getQualifiedName()));
    }

    private static boolean isCandidateToIsJUnit3(CtType<?> candidate) {
        return candidate != null && candidate.getSuperclass() != null && !candidate.isAnonymous();
    }

    public static void replaceAnnotation(
            final CtMethod<?> method,
            final CtPackage jupiterApiPackage,
            final String annotationNameToAdd,
            final String annotationFullQualifiedNameToRemove) {
        final Factory factory = method.getFactory();
        final CtTypeReference annotationType = factory.createAnnotationType(jupiterApiPackage, annotationNameToAdd).getReference();
        final CtAnnotation<?> annotation = factory.createAnnotation(annotationType);
        method.getAnnotations()
                .stream()
                .filter(ctAnnotation -> annotationFullQualifiedNameToRemove.equals(ctAnnotation.getAnnotationType().getQualifiedName()))
                .findFirst()
                .ifPresentOrElse(
                        ctAnnotation -> ctAnnotation.replace(annotation),
                        () -> method.addAnnotation(annotation)
                );
    }

}
