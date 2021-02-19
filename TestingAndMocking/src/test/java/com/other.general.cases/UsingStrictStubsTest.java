package com.other.general.cases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UsingStrictStubsTest {

    // activate the strict subs rule
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void withStrictStubsTest() throws Exception {
        DeepThought deepThought = mock(DeepThought.class);

        when(deepThought.getAnswerFor("Ultimate Question of Life, The Universe, and Everything")).thenReturn(42);
        // this fails now with an UnnecessaryStubbingException since it is never called in the test
        when(deepThought.otherMethod("some mundane thing")).thenReturn(null);

        // this will now throw a PotentialStubbingProblem Exception since we usually don't want to call methods on mocks without configured behavior
        deepThought.someMethod();

        assertEquals(42, deepThought.getAnswerFor("Ultimate Question of Life, The Universe, and Everything"));
        // verifyNoMoreInteractions now automatically verifies that all stubbed methods have been called as well
        verifyNoMoreInteractions(deepThought);
    }
}
