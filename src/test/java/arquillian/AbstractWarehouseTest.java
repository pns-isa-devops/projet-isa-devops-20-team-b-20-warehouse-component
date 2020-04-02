package arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import fr.polytech.warehouse.components.DeliveryModifier;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.utils.CarrierAPI;

/**
 * AbstractTCFTest
 */
public class AbstractWarehouseTest {

    @Deployment
    public static WebArchive createDeployment() {
        // @formatter:off
        return ShrinkWrap.create(WebArchive.class)
                // Utils
                .addPackage(CarrierAPI.class.getPackage())
                // Components and Interfaces
                .addPackage(DeliveryModifier.class.getPackage())
                // Exceptions
                .addPackage(UnknownDeliveryException.class.getPackage())
                // Libraries
                .addAsLibraries(Maven.resolver()
                            .loadPomFromFile("pom.xml")
                            .importRuntimeDependencies()
                            .resolve()
                            .withTransitivity()
                            .asFile());
        // @formatter:on
    }
}
