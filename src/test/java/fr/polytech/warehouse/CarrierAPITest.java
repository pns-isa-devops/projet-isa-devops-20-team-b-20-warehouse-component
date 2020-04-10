package fr.polytech.warehouse;

import javax.inject.Inject;
import org.jboss.arquillian.junit.Arquillian;

import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractWarehouseTest;
import fr.polytech.warehouse.components.WarehouseBean;

@RunWith(Arquillian.class)
public class CarrierAPITest extends AbstractWarehouseTest {

	@Inject
	private WarehouseBean bean;

	@Test
	public void test() {
		System.out.println(bean);
		bean.findDeliveries().forEach(e -> System.out.println(e.toString()));
	}
}