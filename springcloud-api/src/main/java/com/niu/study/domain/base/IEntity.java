/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.niu.study.domain.base;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public class IEntity implements Serializable {

    @Id
    @Column(name = "id",length = 100)
    @NotNull
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String id;

    @NotNull
    private boolean enabled=true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IEntity iEntity = (IEntity) o;
        return enabled == iEntity.enabled && Objects.equals(id, iEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enabled);
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }


}
