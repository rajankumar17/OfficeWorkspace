package com.testing.using.jmockit;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class TestProductService {

    @Tested
    private ProductService productService;

    @Injectable
    private SearchService searchService;

    @Injectable
    private PricingService pricingService;

    private List<Product> products;

    private Map<Long, Double> priceMap;

    @Before
    public void setup() {
        products = new ArrayList<Product>();
        Product p1 = new Product();
        p1.setSku(1L);
        Product p2 = new Product();
        p2.setSku(2L);
        products.add(p1);
        products.add(p2);

        priceMap = new HashMap<>();
        priceMap.put(1L, 111.90);
        priceMap.put(2L, 15.0);

    }

    @Test
    public void shouldFindProducts() {

        new Expectations() {{
            searchService.search("Java Threading");
            returns(products);
            times = 1;

            pricingService.price((List<Long>) any);
            returns(priceMap);
        }};
        List<Product> products = productService.find("Java Threading");
        assertEquals(2, products.size());

        Product product1 = products.get(0);
        assertEquals(1L, product1.getSku());
        assertEquals(111.90, product1.getPrice(), 0);

        Product product2 = products.get(1);
        assertEquals(2L, product2.getSku());
        assertEquals(15.00, product2.getPrice(), 0);
    }
}
