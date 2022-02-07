package fr.davidson.junit_upgrader.utils;

import spoon.reflect.declaration.CtType;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class Utils {

    public static final String ORG_JUNIT_JUPITER_API = "org.junit.jupiter.api";

    public static final String ASSERTIONS = "Assertions";

    public static final String JUNIT_FRAMEWORK_TEST_CASE = "junit.framework.TestCase";

    public static boolean isJUnit3WithLookUp(CtType<?> candidate) {
        return isJUnit3(candidate) || (candidate.getSuperclass() != null && isJUnit3WithLookUp(candidate.getSuperclass().getTypeDeclaration()));
    }

    public static boolean isJUnit3(CtType<?> candidate) {
        return (candidate.getSuperclass() != null &&
                JUNIT_FRAMEWORK_TEST_CASE.equals(candidate.getSuperclass().getQualifiedName()));
    }


}
