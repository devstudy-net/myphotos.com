/*
 * Copyright 2017 </>DevStudy.net.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.devstudy.myphotos.ejb.service.interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import net.devstudy.myphotos.model.AsyncOperation;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Interceptor
public class AsyncOperationInterceptor {

    @AroundInvoke
    public Object aroundProcessImageResource(InvocationContext ic) throws Exception {
        replaceAsynOperationByProxy(ic);
        return ic.proceed();
    }

    private void replaceAsynOperationByProxy(InvocationContext ic) {
        Object[] params = ic.getParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof AsyncOperation) {
                params[i] = new AsyncOperationProxy((AsyncOperation) params[i]);
            }
        }
        ic.setParameters(params);
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class AsyncOperationProxy implements AsyncOperation {

        private final AsyncOperation originalAsyncOperation;

        public AsyncOperationProxy(AsyncOperation originalAsyncOperation) {
            this.originalAsyncOperation = originalAsyncOperation;
        }

        @Override
        public void onSuccess(Object result) {
            originalAsyncOperation.onSuccess(result);
        }

        @Override
        public void onFailed(Throwable throwable) {
            try {
                originalAsyncOperation.onFailed(throwable);
            } catch (RuntimeException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "AsyncOperation.onFailed throws exception: " + e.getMessage(), e);
            }
        }

        @Override
        public long getTimeOutInMillis() {
            return originalAsyncOperation.getTimeOutInMillis();
        }
    }
}
