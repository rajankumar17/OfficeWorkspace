package org.junit.internal.runners.statements;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Math.atan;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestTimedOutException;


/**
 * @author Asaf Ary, Stefan Birkner
 */
@RunWith(Parameterized.class)
public class FailOnTimeoutTest {
    private static final long TIMEOUT = 100;
    private static final long DURATION_THAT_EXCEEDS_TIMEOUT = 60 * 60 * 1000; //1 hour

    private final TestStatement statement = new TestStatement();

    private final boolean lookingForStuckThread;
    private final FailOnTimeout failOnTimeout;

    @Parameterized.Parameters(name = "lookingForStuckThread = {0}")
    public static Iterable<Boolean> getParameters() {
        return Arrays.asList(Boolean.TRUE, Boolean.FALSE);
    }

    public FailOnTimeoutTest(Boolean lookingForStuckThread) {
        this.lookingForStuckThread = lookingForStuckThread;
        this.failOnTimeout = builder().withTimeout(TIMEOUT, MILLISECONDS).build(statement);
    }

    private FailOnTimeout.Builder builder() {
        return FailOnTimeout.builder().withLookingForStuckThread(lookingForStuckThread);
    }

    @Test
    public void throwsTestTimedOutException() {
        assertThrows(
                TestTimedOutException.class,
                evaluateWithWaitDuration(DURATION_THAT_EXCEEDS_TIMEOUT));
    }

    @Test
    public void throwExceptionWithNiceMessageOnTimeout() {
        TestTimedOutException e = assertThrows(
                TestTimedOutException.class,
                evaluateWithWaitDuration(DURATION_THAT_EXCEEDS_TIMEOUT));
        assertEquals("test timed out after 100 milliseconds", e.getMessage());
    }

    @Test
    public void sendUpExceptionThrownByStatement() {
        RuntimeException exception = new RuntimeException();
        RuntimeException e = assertThrows(
                RuntimeException.class,
                evaluateWithException(exception));
        assertSame(exception, e);
    }

    @Test
    public void throwExceptionIfTheSecondCallToEvaluateNeedsTooMuchTime()
            throws Throwable {
        evaluateWithWaitDuration(0).run();
        assertThrows(
                TestTimedOutException.class,
                evaluateWithWaitDuration(DURATION_THAT_EXCEEDS_TIMEOUT));
    }

    @Test
    public void throwTimeoutExceptionOnSecondCallAlthoughFirstCallThrowsException() {
        try {
            evaluateWithException(new RuntimeException()).run();
        } catch (Throwable expected) {
        }

        TestTimedOutException e = assertThrows(
                TestTimedOutException.class,
                evaluateWithWaitDuration(DURATION_THAT_EXCEEDS_TIMEOUT));
        assertEquals("test timed out after 100 milliseconds", e.getMessage());
    }

    @Test
    public void throwsExceptionWithTimeoutValueAndTimeUnitSet() {
        TestTimedOutException e = assertThrows(
                TestTimedOutException.class,
                evaluateWithWaitDuration(DURATION_THAT_EXCEEDS_TIMEOUT));
        assertEquals(TIMEOUT, e.getTimeout());
        assertEquals(TimeUnit.MILLISECONDS, e.getTimeUnit());
    }

    private ThrowingRunnable evaluateWithDelegate(final Statement delegate) {
        return new ThrowingRunnable() {
            public void run() throws Throwable {
                statement.nextStatement = delegate;
                statement.waitDuration = 0;
                failOnTimeout.evaluate();
            }
        };
    }

    private ThrowingRunnable evaluateWithException(Exception exception) {
        return evaluateWithDelegate(new Fail(exception));
    }

    private ThrowingRunnable evaluateWithWaitDuration(final long waitDuration) {
        return new ThrowingRunnable() {
            public void run() throws Throwable {
                statement.nextStatement = null;
                statement.waitDuration = waitDuration;
                failOnTimeout.evaluate();
            }
        };
    }

    private static final class TestStatement extends Statement {
        long waitDuration;

        Statement nextStatement;

        @Override
        public void evaluate() throws Throwable {
            sleep(waitDuration);
            if (nextStatement != null) {
                nextStatement.evaluate();
            }
        }
    }

    @Test
    public void stopEndlessStatement() throws Throwable {
        InfiniteLoopStatement infiniteLoop = new InfiniteLoopStatement();
        FailOnTimeout infiniteLoopTimeout = builder().withTimeout(TIMEOUT, MILLISECONDS).build(infiniteLoop);
        try {
            infiniteLoopTimeout.evaluate();
        } catch (Exception timeoutException) {
            sleep(20); // time to interrupt the thread
            int firstCount = InfiniteLoopStatement.COUNT;
            sleep(20); // time to increment the count
            assertTrue("Thread has not been stopped.",
                    firstCount == InfiniteLoopStatement.COUNT);
        }
    }

    private static final class InfiniteLoopStatement extends Statement {
        private static int COUNT = 0;

        @Override
        public void evaluate() throws Throwable {
            while (true) {
                sleep(10); // sleep in order to enable interrupting thread
                ++COUNT;
            }
        }
    }

    @Test
    public void stackTraceContainsRealCauseOfTimeout() throws Throwable {
        StuckStatement stuck = new StuckStatement();
        FailOnTimeout stuckTimeout = builder().withTimeout(TIMEOUT, MILLISECONDS).build(stuck);
        try {
            stuckTimeout.evaluate();
            // We must not get here, we expect a timeout exception
            fail("Expected timeout exception");
        } catch (Exception timeoutException) {
            StackTraceElement[] stackTrace = timeoutException.getStackTrace();
            boolean stackTraceContainsTheRealCauseOfTheTimeout = false;
            boolean stackTraceContainsOtherThanTheRealCauseOfTheTimeout = false;
            for (StackTraceElement element : stackTrace) {
                String methodName = element.getMethodName();
                if ("theRealCauseOfTheTimeout".equals(methodName)) {
                    stackTraceContainsTheRealCauseOfTheTimeout = true;
                }
                if ("notTheRealCauseOfTheTimeout".equals(methodName)) {
                    stackTraceContainsOtherThanTheRealCauseOfTheTimeout = true;
                }
            }
            assertTrue(
                    "Stack trace does not contain the real cause of the timeout",
                    stackTraceContainsTheRealCauseOfTheTimeout);
            assertFalse(
                    "Stack trace contains other than the real cause of the timeout, which can be very misleading",
                    stackTraceContainsOtherThanTheRealCauseOfTheTimeout);
        }
    }

    private static final class StuckStatement extends Statement {

        @Override
        public void evaluate() throws Throwable {
            try {
                // Must show up in stack trace
                theRealCauseOfTheTimeout();
            } catch (InterruptedException e) {
            } finally {
                // Must _not_ show up in stack trace
                notTheRealCauseOfTheTimeout();
            }
        }

        private void theRealCauseOfTheTimeout() throws InterruptedException {
            sleep(MAX_VALUE);
        }

        private void notTheRealCauseOfTheTimeout() {
            for (long now = currentTimeMillis(), eta = now + 1000L; now < eta; now = currentTimeMillis()) {
                // Doesn't matter, just pretend to be busy
                atan(now);
            }
        }
    }

    @Test
    public void lookingForStuckThread_threadGroupNotLeaked() throws Throwable {
        assumeTrue(lookingForStuckThread);
        final AtomicReference<ThreadGroup> innerThreadGroup = new AtomicReference<ThreadGroup>();
        final AtomicReference<Thread> innerThread = new AtomicReference<Thread>();
        final ThreadGroup outerThreadGroup = currentThread().getThreadGroup();
        ThrowingRunnable runnable = evaluateWithDelegate(new Statement() {
            @Override
            public void evaluate() {
                innerThread.set(currentThread());
                ThreadGroup group = currentThread().getThreadGroup();
                assertNotSame("inner thread should use a different thread group", outerThreadGroup, group);
                innerThreadGroup.set(group);
                assertTrue("the 'FailOnTimeoutGroup' thread group should be a daemon thread group", group.isDaemon());
            }
        });

        runnable.run();

        assertTrue("the Statement was never run", innerThread.get() != null);
        innerThread.get().join();
        assertTrue("the 'FailOnTimeoutGroup' thread group should be destroyed after running the test", innerThreadGroup.get().isDestroyed());
    }

    @Test
    public void notLookingForStuckThread_usesSameThreadGroup() throws Throwable {
        assumeFalse(lookingForStuckThread);
        final ThreadGroup outerThreadGroup = currentThread().getThreadGroup();
        ThrowingRunnable runnable = evaluateWithDelegate(new Statement() {
            @Override
            public void evaluate() {
                ThreadGroup group = currentThread().getThreadGroup();
                assertSame("inner thread should use the same thread group", outerThreadGroup, group);
            }
        });

        runnable.run();
    }
}
