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

package net.devstudy.myphotos.rest.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import net.devstudy.myphotos.common.model.ListMap;
import net.devstudy.myphotos.rest.model.ValidationItemREST;
import net.devstudy.myphotos.rest.model.ValidationResultREST;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ConstraintViolationConverter {

    public <T> ValidationResultREST convert(Set<ConstraintViolation<T>> violations) {
        ListMap<String, String> listMap = new ListMap<>();
        for(ConstraintViolation<T> violation : violations) {
            for(Path.Node node : violation.getPropertyPath()) {
                listMap.add(node.getName(), violation.getMessage());
            }
        }
        return createValidationResult(listMap);
    }

    private ValidationResultREST createValidationResult(ListMap<String, String> listMap) {
        List<ValidationItemREST> list = new ArrayList<>();
        for(Map.Entry<String, List<String>> entry : listMap.toMap().entrySet()) {
            list.add(new ValidationItemREST(entry.getKey(), entry.getValue()));
        }
        return new ValidationResultREST(list);
    }
}
