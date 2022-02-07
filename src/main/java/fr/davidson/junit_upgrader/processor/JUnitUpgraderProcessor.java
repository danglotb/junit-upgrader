package fr.davidson.junit_upgrader.processor;

import fr.davidson.junit_upgrader.utils.Utils;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtPackageReference;

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
        jupiterApiPackage.setSimpleName(Utils.ORG_JUNIT_JUPITER_API_PACKAGE_NAME);
        final CtPackageReference jupiterApiPackageReference = jupiterApiPackage.getReference();
        if (Utils.isJUnit3WithLookUp(ctClass)) {
            new SubJUnit3UpgraderProcessor(jupiterApiPackage, jupiterApiPackageReference).process(ctClass);
            Utils.replaceAssertionsClass(ctClass, jupiterApiPackageReference, Utils.FULL_QUALIFIED_NAME_TESTCASE_NAME);
        } else {
            new SubJUnit4UpgraderProcessor(jupiterApiPackage, jupiterApiPackageReference).process(ctClass);
            Utils.replaceAssertionsClass(ctClass, jupiterApiPackageReference, Utils.FULL_QUALIFIED_NAME_ASSERT_NAME);
        }
    }

}
