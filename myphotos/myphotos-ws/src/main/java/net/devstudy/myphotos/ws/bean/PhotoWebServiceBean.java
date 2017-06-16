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

package net.devstudy.myphotos.ws.bean;

import static javax.ejb.ConcurrencyManagementType.BEAN;
import static net.devstudy.myphotos.model.SortMode.POPULAR_AUTHOR;
import static net.devstudy.myphotos.model.SortMode.POPULAR_PHOTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

import net.devstudy.myphotos.common.converter.ModelConverter;
import net.devstudy.myphotos.common.converter.UrlConveter;
import net.devstudy.myphotos.model.OriginalImage;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.SortMode;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.ws.PhotoWebService;
import net.devstudy.myphotos.ws.error.ExceptionMapperInterceptor;
import net.devstudy.myphotos.ws.model.ImageLinkSOAP;
import net.devstudy.myphotos.ws.model.PhotoSOAP;
import net.devstudy.myphotos.ws.model.PhotosSOAP;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@MTOM
@Singleton
@ConcurrencyManagement(BEAN)
@WebService(portName = "PhotoServicePort",
        serviceName = "PhotoService",
        targetNamespace = "http://soap.myphotos.com/ws/PhotoService?wsdl",
        endpointInterface = "net.devstudy.myphotos.ws.PhotoWebService")
@Interceptors(ExceptionMapperInterceptor.class)
public class PhotoWebServiceBean implements PhotoWebService {

    @EJB
    private PhotoService photoService;

    @Inject
    private ModelConverter modelConverter;

    @Inject
    private UrlConveter urlConveter;

    @Override
    public PhotosSOAP findAllOrderByPhotoPopularity(int page, int limit, boolean withTotal) {
        return findAll(POPULAR_PHOTO, page, limit, withTotal);
    }

    @Override
    public PhotosSOAP findAllOrderByAuthorPopularity(int page, int limit, boolean withTotal) {
        return findAll(POPULAR_AUTHOR, page, limit, withTotal);
    }

    private PhotosSOAP findAll(SortMode sortMode, int page, int limit, boolean withTotal) {
        List<Photo> photos = photoService.findPopularPhotos(sortMode, new Pageable(page, limit));
        PhotosSOAP result = new PhotosSOAP();
        result.setPhotos(modelConverter.convertList(photos, PhotoSOAP.class));
        if (withTotal) {
            result.setTotal(photoService.countAllPhotos());
        }
        return result;
    }

    @Override
    public ImageLinkSOAP viewLargePhoto(Long photoId) {
        String relativeUrl = photoService.viewLargePhoto(photoId);
        String absoluteUrl = urlConveter.convert(relativeUrl);
        return new ImageLinkSOAP(absoluteUrl);
    }

    @Override
    public DataHandler downloadOriginalImage(Long photoId) {
        OriginalImage originalImage = photoService.downloadOriginalImage(photoId);
        return new DataHandler(new OriginalImageDataSource(originalImage));
    }

    /**
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class OriginalImageDataSource implements DataSource {

        private final OriginalImage originalImage;

        public OriginalImageDataSource(OriginalImage originalImage) {
            this.originalImage = originalImage;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return originalImage.getIn();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException("OutputStream is not supported");
        }

        @Override
        public String getContentType() {
            return "image/jpeg";
        }

        @Override
        public String getName() {
            return originalImage.getName();
        }
    }
}
