package com.testing.using.jmockit;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

public class TestPaymentManagerException {

    @Tested
    private PaymentManager paymentManager;

    @Injectable
    private ThirdPartyPaymentService thirdPartyPaymentService;

    //Here Exception is expected
    @Test(expected = PaymentFailedException.class)
    public void shouldNotMakePaymentAndThrowException() throws PaymentFailedException {

        //Create the expectation for exception
        new Expectations(){{
           thirdPartyPaymentService.pay(anyInt);
           result = new PaymentFailedException();
           times =1;

        }};

        paymentManager.pay(100);

    }

}
