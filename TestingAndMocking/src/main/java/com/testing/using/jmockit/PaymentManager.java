package com.testing.using.jmockit;

public class PaymentManager {

    private ThirdPartyPaymentService thirdPartyPaymentService;

    public boolean pay(int amount) throws PaymentFailedException {

        return thirdPartyPaymentService.pay(amount);
    }

}
