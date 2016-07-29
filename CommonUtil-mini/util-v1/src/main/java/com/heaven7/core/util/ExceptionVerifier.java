package com.heaven7.core.util;

/**
 * the class can help us to verify if VerifyException occurs or not.
 *
 * @param <Param>  the param used to verify
 * @param <Result> the verify result, if verify success.
 */
public abstract class ExceptionVerifier<Param, Result> {

    /**
     * verify the exception . and return true if a VerifyException not occurs in run.
     *
     * @param params the params to run
     * @return true if verify success.
     */
    public boolean verify(Param... params) {
        boolean success = false;
        Result result = null;
        try {
            result = run(params);
            success = true;
        } catch (VerifyException e) {
            onFailed(e);
        } finally {
            if (success) {
                onSuccess(result);
            }
        }
        return success;
    }

    /**
     * called when verify success.
     * see also: {@link #verify(Object[])}
     *
     * @param result the result of run/verify
     */
    protected void onSuccess(Result result) {

    }

    /**
     * the main method to execute verification.
     *
     * @param params the params to run
     * @return the run result
     * @throws VerifyException if verify failed with VerifyException occurs.
     */
    protected  abstract Result run(Param[] params) throws VerifyException ;

    /**
     * called when verify failed. that means exception occured.
     * see also: {@link #verify(Object[])}
     *
     * @param e the Verify exception when run/verify throws.
     */
    protected void onFailed(VerifyException e) {

    }

    /**
     * this class indicate verification failed .
     */
    public static class VerifyException extends Exception{
        public VerifyException() {
        }
        public VerifyException(String detailMessage) {
            super(detailMessage);
        }
        public VerifyException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
        public VerifyException(Throwable throwable) {
            super(throwable);
        }
    }

}
