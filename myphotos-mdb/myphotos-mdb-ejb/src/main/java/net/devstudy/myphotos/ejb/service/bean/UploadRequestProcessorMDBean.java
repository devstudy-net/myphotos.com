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
package net.devstudy.myphotos.ejb.service.bean;

import static net.devstudy.myphotos.common.config.JMSEnvironmentSettings.JMS_CONNECTION_FACTORY_JNDI_NAME;
import static net.devstudy.myphotos.common.config.JMSEnvironmentSettings.UPLOAD_REQUEST_QUEUE_JNDI_NAME;
import static net.devstudy.myphotos.common.config.JMSEnvironmentSettings.UPLOAD_RESPONSE_QUEUE_JNDI_NAME;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.IMAGE_RESOURCE_TEMP_PATH;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.IMAGE_RESOURCE_TYPE;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.PROFILE_ID;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.REQUEST_SUCCESS;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSSessionMode;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;

import net.devstudy.myphotos.common.config.JMSImageResourceType;
import net.devstudy.myphotos.common.model.TempFileFactory;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = UPLOAD_REQUEST_QUEUE_JNDI_NAME)
})
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UploadRequestProcessorMDBean implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    @JMSConnectionFactory(JMS_CONNECTION_FACTORY_JNDI_NAME)
    @JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
    private JMSContext context;

    @Resource(lookup = UPLOAD_RESPONSE_QUEUE_JNDI_NAME)
    private Queue uploadResponseQueue;

    @EJB
    private ImageProcessorBean imageProcessorBean;

    @Override
    public void onMessage(Message jmsMessage) {
        try {
            MapMessage mapMessage = (MapMessage) jmsMessage;
            String path = mapMessage.getString(IMAGE_RESOURCE_TEMP_PATH.name());
            Long profileId = mapMessage.getLong(PROFILE_ID.name());
            JMSImageResourceType imageResourceType = JMSImageResourceType.valueOf(mapMessage.getString(IMAGE_RESOURCE_TYPE.name()));

            processMessage(imageResourceType, profileId, path);
        } catch (JMSException | RuntimeException ex) {
            logger.log(Level.SEVERE, getClass().getName() + ".onMessage failed: " + ex.getMessage(), ex);
        }
    }

    private void processMessage(JMSImageResourceType imageResourceType, Long profileId, String path) {
        try {
            Serializable entity;
            if (imageResourceType == JMSImageResourceType.IMAGE_RESOURCE_PHOTO) {
                entity = uploadNewPhoto(path);
            } else {
                entity = uploadNewAvatar(path, profileId);
            }
            sendSuccessUploadResponse(profileId, imageResourceType, path, entity);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Failed uploaded photo: " + e.getMessage(), e);
            sendFailedUploadResponse(profileId, imageResourceType, path, e);
        }
    }

    private Photo uploadNewPhoto(String path) {
        Photo photo = imageProcessorBean.processPhoto(new TempFileImageResource(Paths.get(path)));
        logger.log(Level.INFO, "Successful uploaded photo: smallUrl={0}, largeUrl={1}, originalUrl={2}",
                new Object[]{photo.getSmallUrl(), photo.getLargeUrl(), photo.getOriginalUrl()});
        return photo;
    }

    private Profile uploadNewAvatar(String path, Long profileId) {
        Profile profile = new Profile();
        profile.setAvatarUrl(imageProcessorBean.processProfileAvatar(new TempFileImageResource(Paths.get(path))));
        logger.log(Level.INFO, "Successful uploaded avatar {0} for profile {1}", new Object[]{profile.getAvatarUrl(), profileId});
        return profile;
    }

    private void sendSuccessUploadResponse(Long profileId, JMSImageResourceType imageResourceType, String path, Serializable entity) {
        context.createProducer()
                .setDeliveryMode(DeliveryMode.NON_PERSISTENT)
                .setProperty(REQUEST_SUCCESS.name(), true)
                .setProperty(PROFILE_ID.name(), profileId)
                .setProperty(IMAGE_RESOURCE_TYPE.name(), imageResourceType.name())
                .setProperty(IMAGE_RESOURCE_TEMP_PATH.name(), path)
                .send(uploadResponseQueue, entity);
    }

    private void sendFailedUploadResponse(Long profileId, JMSImageResourceType imageResourceType, String path, Throwable throwable) {
        context.createProducer()
                .setDeliveryMode(DeliveryMode.NON_PERSISTENT)
                .setProperty(REQUEST_SUCCESS.name(), false)
                .setProperty(PROFILE_ID.name(), profileId)
                .setProperty(IMAGE_RESOURCE_TYPE.name(), imageResourceType)
                .setProperty(IMAGE_RESOURCE_TEMP_PATH.name(), path)
                .send(uploadResponseQueue, throwable);
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class TempFileImageResource implements ImageResource {

        private final Path tempPath;

        public TempFileImageResource(Path tempPath) {
            this.tempPath = tempPath;
        }

        @Override
        public Path getTempPath() {
            return tempPath;
        }

        @Override
        public void close() {
            TempFileFactory.deleteTempFile(tempPath);
        }
    }
}
