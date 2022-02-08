package fr.davidson.junit_upgrader.processor;

import fr.davidson.junit_upgrader.utils.Utils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.regex.Pattern;

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
        final CtTypeAccess<?> jupiterAssertionsTypeAccess = ctClass.getFactory().createTypeAccess(ctClass.getFactory().Class().get(Assertions.class).getReference());
        if (Utils.isJUnit3WithLookUp(ctClass)) {
            new SubJUnit3UpgraderProcessor(jupiterApiPackage, jupiterApiPackageReference).process(ctClass);
            replaceAssertionsClass(
                    ctClass,
                    ctClass.getFactory().Class().get(TestCase.class),
                    jupiterAssertionsTypeAccess
            );
        } else {
            new SubJUnit4UpgraderProcessor(jupiterApiPackage, jupiterApiPackageReference).process(ctClass);
            replaceAssertionsClass(
                    ctClass,
                    ctClass.getFactory().Class().get(Assert.class),
                    jupiterAssertionsTypeAccess
            );
        }
    }

    private void initJupiterApiPackage(Factory factory) {
        if (this.jupiterApiPackage == null) {
            this.jupiterApiPackage = factory.createPackage();
            jupiterApiPackage.setSimpleName(Utils.ORG_JUNIT_JUPITER_API_PACKAGE_NAME);
            this.jupiterApiPackageReference = jupiterApiPackage.getReference();
        }
    }



    public void replaceAssertionsClass(
            final CtClass<?> ctClass,
            final CtClass<?> originAssertionClass,
            final CtTypeAccess<?> jupiterAssertionsTypeAccess
    ) {
        ctClass.getElements(new TypeFilter<>(CtInvocation.class) {
            @Override
            public boolean matches(CtInvocation candidate) {
                return originAssertionClass.getMethods()
                        .stream()
                        .filter(method -> method.getSimpleName().startsWith("assert") || method.getSimpleName().startsWith("fail"))
                        .anyMatch(method ->
                                candidate.getParent() instanceof CtBlock &&
                                        method.getReference().getSignature().equals(candidate.getExecutable().getSignature())
                        );
            }
        }).forEach(invocation -> {
            invocation.setTarget(jupiterAssertionsTypeAccess);
            handleArguments(invocation);
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleArguments(CtInvocation<?> invocation) {
        if ("assertThrows".equals(invocation.getExecutable().getSimpleName())) {
            final Factory factory = invocation.getFactory();
            // we need here to transforme the org.junit.function.ThrowingRunnable into org.junit.jupiter.api.function.Executable
            final CtNewClass newThrowableToBeExecutable = ((CtNewClass)invocation.getArguments().get(1));
            newThrowableToBeExecutable.getExecutable().setType(factory.Type().get(Executable.class).getReference());
            final CtMethod runToBeExecute = (CtMethod) newThrowableToBeExecutable.getAnonymousClass().getMethodsByName("run").get(0);
            runToBeExecute.setSimpleName("execute");
        } else {
            permuteArgumentsIfNeeded(invocation);
        }
    }

    private void permuteArgumentsIfNeeded(CtInvocation<?> invocation) {
        if (Pattern.compile("assert(True|False)").matcher(invocation.getExecutable().getSimpleName()).matches()) {
            if (invocation.getArguments().size() > 1) {
                invocation.getArguments().add(invocation.getArguments().remove(0));
            }
        } else if ("assertEquals".equals(invocation.getExecutable().getSimpleName())) {
            if (invocation.getArguments().size() > 2) {
                invocation.getArguments().add(invocation.getArguments().remove(0));
            }
        }
    }

}
