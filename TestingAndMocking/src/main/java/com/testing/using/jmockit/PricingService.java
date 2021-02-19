package com.testing.using.jmockit;

import java.util.List;
import java.util.Map;

public interface PricingService {
    Map<Long,Double> price(List<Long> skus);
}
