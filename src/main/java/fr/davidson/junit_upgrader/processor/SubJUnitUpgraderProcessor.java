package fr.davidson.junit_upgrader.processor;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.reference.CtPackageReference;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public abstract class SubJUnitUpgraderProcessor {

    protected final CtPackage jupiterApiPackage;
    protected final CtPackageReference jupiterApiPackageReference;

    public SubJUnitUpgraderProcessor(CtPackage jupiterApiPackage, CtPackageReference jupiterApiPackageReference) {
        this.jupiterApiPackage = jupiterApiPackage;
        this.jupiterApiPackageReference = jupiterApiPackageReference;
    }

    public abstract void process(CtClass<?> classToProcess);

}
