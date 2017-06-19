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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import static net.devstudy.myphotos.common.config.JMSEnvironmentSettings.UPLOAD_RESPONSE_QUEUE_JNDI_NAME;
import net.devstudy.myphotos.common.config.JMSImageResourceType;
import static net.devstudy.myphotos.common.config.JMSImageResourceType.IMAGE_RESOURCE_PHOTO;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.IMAGE_RESOURCE_TEMP_PATH;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.IMAGE_RESOURCE_TYPE;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.PROFILE_ID;
import static net.devstudy.myphotos.common.config.JMSMessageProperty.REQUEST_SUCCESS;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.ejb.service.bean.PhotoServiceBean;
import net.devstudy.myphotos.ejb.service.bean.ProfileServiceBean;
import net.devstudy.myphotos.model.domain.Profile;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = UPLOAD_RESPONSE_QUEUE_JNDI_NAME)
})
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UploadResponseProcessorMDBean implements MessageListener {

    @Inject
    private Logger logger;

    @EJB
    private PhotoServiceBean photoServiceBean;

    @EJB
    private ProfileServiceBean profileServiceBean;

    @EJB
    private AsyncUploadImageManager asyncUploadImageManager;
    
    @Resource
    private MessageDrivenContext messageDrivenContext;

    @Override
    public void onMessage(Message jmsMessage) {
        try {
            if (jmsMessage.getJMSRedelivered()) {
                logger.log(Level.WARNING, "Ignoring redelivered message for path {0}", jmsMessage.getStringProperty(IMAGE_RESOURCE_TEMP_PATH.name()));
                return;
            }
            ObjectMessage message = (ObjectMessage) jmsMessage;
            String path = message.getStringProperty(IMAGE_RESOURCE_TEMP_PATH.name());
            JMSImageResourceType imageResourceType = JMSImageResourceType.valueOf(message.getStringProperty(IMAGE_RESOURCE_TYPE.name()));
            processMessage(message, path, imageResourceType);
        } catch (JMSException ex) {
            messageDrivenContext.setRollbackOnly();
            logger.log(Level.SEVERE, UploadResponseProcessorMDBean.class.getName() + ".onMessage failed: " + ex.getMessage(), ex);
        }
    }

    private void processMessage(ObjectMessage message, String path, JMSImageResourceType imageResourceType) throws JMSException {
        Long profileId = message.getLongProperty(PROFILE_ID.name());
        if (message.getBooleanProperty(REQUEST_SUCCESS.name())) {
            processSuccessMessage(profileId, message, imageResourceType, path);
        } else {
            processFailedMessage(profileId, message, imageResourceType, path);
        }
    }

    private void processSuccessMessage(Long profileId, ObjectMessage message, JMSImageResourceType imageResourceType, String path) throws JMSException {
        if (imageResourceType == IMAGE_RESOURCE_PHOTO) {
            Photo photo = (Photo) message.getObject();
            photoServiceBean.createNewPhoto(profileId, photo);
            asyncUploadImageManager.completeUploadNewPhotoSuccess(path, photo);
        } else {
            Profile profile = (Profile) message.getObject();
            profileServiceBean.saveNewAvatar(profileId, profile.getAvatarUrl());
            asyncUploadImageManager.completeUploadNewAvatarSuccess(path, profile);
        }
    }

    private void processFailedMessage(Long profileId, ObjectMessage message, JMSImageResourceType imageResourceType, String path) throws JMSException {
        Throwable throwable = (Throwable) message.getObject();
        if (imageResourceType == IMAGE_RESOURCE_PHOTO) {
            asyncUploadImageManager.completeUploadNewPhotoFailed(path, throwable);
        } else {
            profileServiceBean.setAvatarPlaceHolder(profileId);
            asyncUploadImageManager.completeUploadNewAvatarFailed(path, throwable);
        }
    }
}