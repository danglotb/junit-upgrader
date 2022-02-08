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

    private CtPackage jupiterApiPackage;

    private CtPackageReference jupiterApiPackageReference;

    @Override
    public void process(CtClass<?> ctClass) {
        final Factory factory = ctClass.getFactory();
        initJupiterApiPackage(factory);
        if (Utils.isJUnit3WithLookUp(ctClass)) {
            new SubJUnit3UpgraderProcessor(jupiterApiPackage, jupiterApiPackageReference).process(ctClass);
            Utils.replaceAssertionsClass(ctClass, jupiterApiPackageReference, Utils.FULL_QUALIFIED_NAME_TESTCASE_NAME);
        } else {
            new SubJUnit4UpgraderProcessor(jupiterApiPackage, jupiterApiPackageReference).process(ctClass);
            Utils.replaceAssertionsClass(ctClass, jupiterApiPackageReference, Utils.FULL_QUALIFIED_NAME_ASSERT_NAME);
        }
    }

    private void initJupiterApiPackage(Factory factory) {
        if (this.jupiterApiPackage == null) {
            this.jupiterApiPackage = factory.createPackage();
            jupiterApiPackage.setSimpleName(Utils.ORG_JUNIT_JUPITER_API_PACKAGE_NAME);
            this.jupiterApiPackageReference = jupiterApiPackage.getReference();
        }
    }

}
