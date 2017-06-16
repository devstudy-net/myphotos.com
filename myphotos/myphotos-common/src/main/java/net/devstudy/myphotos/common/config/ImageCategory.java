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

package net.devstudy.myphotos.common.config;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public enum ImageCategory {
    
    PROFILE_AVATAR("media/avatar/", 128, 128, true, 0.8),
    
    SMALL_PHOTO("media/photo/", 400, 250, true, 0.7),
    
    LARGE_PHOTO("media/photo/", 1600, 900, false, 1.0);
    
    public static boolean isImageCategoryUrl(String url){
        if(url != null) {
            for(ImageCategory imageCategory : ImageCategory.values()) {
                if(url.contains(imageCategory.getRelativeRoot())) {
                    return true;
                }
            }
        }
        return false;
    }

    private final String relativeRoot;
    private final int width;
    private final int height;
    private final boolean crop;
    private final double quality;
    private final String outputFormat = "jpg";

    private ImageCategory(String relativeRoot, int width, int height, boolean crop, double quality) {
        this.relativeRoot = relativeRoot;
        this.width = width;
        this.height = height;
        this.crop = crop;
        this.quality = quality;
    }

    public String getRelativeRoot() {
        return relativeRoot;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isCrop() {
        return crop;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public double getQuality() {
        return quality;
    }
}
