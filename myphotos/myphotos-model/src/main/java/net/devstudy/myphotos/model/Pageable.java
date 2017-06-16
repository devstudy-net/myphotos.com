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
package net.devstudy.myphotos.model;

import net.devstudy.myphotos.exception.ValidationException;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class Pageable {

    private final int page;
    private final int limit;
    
    public Pageable(int limit) {
        this(1, limit);
    }
    
    public Pageable(int page, int limit) {
        if(page < 1) {
            throw new ValidationException("Invalid page value. Should be >= 1");
        }
        if(limit < 1) {
            throw new ValidationException("Invalid limit value. Should be >= 1");
        }
        this.page = page;
        this.limit = limit;
    }
    
    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }
    
    public int getOffset(){
        return (page-1) * limit;
    }

    @Override
    public String toString() {
        return "Pageable{" + "page=" + page + ", limit=" + limit + '}';
    }
}
