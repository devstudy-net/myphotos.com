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
package net.devstudy.myphotos.ejb.service.bean.scalable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.BEAN;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import static net.devstudy.myphotos.common.config.JMSEnvironmentSettings.JMS_CONNECTION_FACTORY_JNDI_NAME;
import static net.devstudy.myphotos.common.config.JMSEnvironmentSettings.UPLOAD_REQUEST_QUEUE_JNDI_NAME;
import net.devstudy.myphotos.common.config.JMSImageResourceType;
import static net.devstudy.myphotos.common.config.JMSImageResourceType.IMAGE_RESOURCE_PHOTO;
import static net.devstudy.myphotos.common.config.JMSImageResourceType.IMAGE_RESOURCE_PROFILE_AVATAR;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.IMAGE_RESOURCE_TEMP_PATH;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.IMAGE_RESOURCE_TYPE;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.PROFILE_ID;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Singleton
@LocalBean
@ConcurrencyManagement(BEAN)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AsyncUploadImageManager {

    @Inject
    private Logger logger;

    @Inject
    @JMSConnectionFactory(JMS_CONNECTION_FACTORY_JNDI_NAME)
    private JMSContext context;

    @Resource(lookup = UPLOAD_REQUEST_QUEUE_JNDI_NAME)
    private Queue uploadRequestQueue;

    private final Map<String, AsyncOperationItem> asyncOperationMap = new ConcurrentHashMap<>();

    public void uploadNewAvatar(Profile currentProfile, ImageResource imageResource, AsyncOperation<Profile> asyncOperation) {
        upload(currentProfile, imageResource, asyncOperation, IMAGE_RESOURCE_PROFILE_AVATAR);
    }

    public void uploadNewPhoto(Profile currentProfile, ImageResource imageResource, AsyncOperation<Photo> asyncOperation) {
        upload(currentProfile, imageResource, asyncOperation, IMAGE_RESOURCE_PHOTO);
    }

    public void completeUploadNewAvatarSuccess(String path, Profile profile) {
        completeAsynOperationSuccess(IMAGE_RESOURCE_PROFILE_AVATAR, path, profile);
    }

    public void completeUploadNewPhotoSuccess(String path, Photo photo) {
        completeAsynOperationSuccess(IMAGE_RESOURCE_PHOTO, path, photo);
    }

    public void completeUploadNewAvatarFailed(String path, Throwable throwable) {
        completeAsynOperationFailed(IMAGE_RESOURCE_PROFILE_AVATAR, path, throwable);
    }

    public void completeUploadNewPhotoFailed(String path, Throwable throwable) {
        completeAsynOperationFailed(IMAGE_RESOURCE_PHOTO, path, throwable);
    }

    private void upload(Profile currentProfile, ImageResource imageResource, AsyncOperation<?> asyncOperation, JMSImageResourceType imageResourceType) {
        Path uploadPath = imageResource.getTempPath();
        String path = uploadPath.toString();
        asyncOperationMap.put(path, new AsyncOperationItem(asyncOperation));
        logger.log(Level.INFO, "Save asyncOperation for path: {0}[{1}]", new Object[]{imageResourceType, path});

        sendUploadRequest(path, currentProfile, imageResourceType, asyncOperation.getTimeOutInMillis());
    }

    private void sendUploadRequest(String path, Profile currentProfile, JMSImageResourceType imageResourceType, long ttl) {
        Map<String, Object> message = new HashMap<>();
        message.put(IMAGE_RESOURCE_TEMP_PATH.name(), path);
        message.put(IMAGE_RESOURCE_TYPE.name(), imageResourceType.name());
        message.put(PROFILE_ID.name(), currentProfile.getId());
        context.createProducer()
                .setTimeToLive(ttl)
                .setDeliveryMode(DeliveryMode.NON_PERSISTENT)
                .send(uploadRequestQueue, message);
    }

    private <T> void completeAsynOperationSuccess(JMSImageResourceType imageResourceType, String path, T entity) {
        AsyncOperationItem asyncOperationItem = asyncOperationMap.remove(path);
        if (asyncOperationItem != null) {
            logger.log(Level.INFO, "Async operation successful for path: {0}[{1}]", new Object[]{imageResourceType, path});
            ((AsyncOperation<T>) asyncOperationItem.getAsyncOperation()).onSuccess(entity);
        } else {
            logger.log(Level.SEVERE, "Async operation not found for path: {0}[{1}]", new Object[]{imageResourceType, path});
        }
    }

    private void completeAsynOperationFailed(JMSImageResourceType imageResourceType, String path, Throwable throwable) {
        AsyncOperationItem asyncOperationItem = asyncOperationMap.remove(path);
        if (asyncOperationItem != null) {
            logger.log(Level.SEVERE, "Async operation failed for path: {0}[{1}]", new Object[]{imageResourceType, path});
            asyncOperationItem.getAsyncOperation().onFailed(throwable);
        } else {
            logger.log(Level.SEVERE, "Async operation not found for path: {0}[{1}]", new Object[]{imageResourceType, path});
        }
    }
    
    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void removeTimedOutAsyncOperations() {
        for (Map.Entry<String, AsyncOperationItem> entry : asyncOperationMap.entrySet()) {
            if (entry.getValue().isExpired()) {
                asyncOperationMap.remove(entry.getKey());
                logger.log(Level.WARNING, "AsyncOperation removed by timeout for path: {0}", entry.getKey());
            }
        }
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class AsyncOperationItem {

        private final AsyncOperation<?> asyncOperation;
        private final long created;

        public AsyncOperationItem(AsyncOperation<?> asyncOperation) {
            this.asyncOperation = asyncOperation;
            this.created = System.currentTimeMillis();
        }

        public AsyncOperation<?> getAsyncOperation() {
            return asyncOperation;
        }

        public boolean isExpired() {
            return created + asyncOperation.getTimeOutInMillis() < System.currentTimeMillis();
        }
    }
}
