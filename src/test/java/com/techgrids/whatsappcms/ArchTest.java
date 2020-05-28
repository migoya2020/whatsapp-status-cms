package com.techgrids.whatsappcms;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.techgrids.whatsappcms");

        noClasses()
            .that()
            .resideInAnyPackage("com.techgrids.whatsappcms.service..")
            .or()
            .resideInAnyPackage("com.techgrids.whatsappcms.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.techgrids.whatsappcms.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
