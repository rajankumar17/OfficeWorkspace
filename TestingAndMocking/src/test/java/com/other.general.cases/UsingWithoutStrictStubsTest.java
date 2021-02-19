package com.other.general.cases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UsingWithoutStrictStubsTest {

    // activate the strict subs rule
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.LENIENT);


    @Test
    public void withoutStrictStubsTest() throws Exception {
        DeepThought deepThought = mock(DeepThought.class);

        when(deepThought.getAnswerFor("Ultimate Question of Life, The Universe, and Everything")).thenReturn(42);
        when(deepThought.otherMethod("some mundane thing")).thenReturn(null);

        //Inline Strictness.LENIENT
        //lenient().when(deepThought.getAnswerFor("Ultimate Question of Life, The Universe, and Everything")).thenReturn(42);
        //lenient().when(deepThought.otherMethod("some mundane thing")).thenReturn(null);

        System.out.println(deepThought.getAnswerFor("Six by nine"));
        deepThought.someMethod();

        assertEquals(42, deepThought.getAnswerFor("Ultimate Question of Life, The Universe, and Everything"));
        verify(deepThought, times(1)).getAnswerFor("Ultimate Question of Life, The Universe, and Everything");
    }

}
