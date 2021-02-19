package com.testing.using.jmockit;

public interface ThirdPartyPaymentService {

    boolean pay(int amount) throws PaymentFailedException;

}
