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

import com.niu.study.utils.IPUtils;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public class BaseEntity extends IEntity implements Serializable {
    @Column(name = "ip",length = 100)
    private String ip;

    @Column(name = "create_by", updatable = false)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @LastModifiedBy
    @Column(name = "update_by")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;

    public String getCreateBy() {
        return createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public BaseEntity() {
    }

    public BaseEntity(String ip, String createBy, String updateBy, Date createTime, Date updateTime) {
        this.ip = ip;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(createBy, that.createBy) && Objects.equals(updateBy, that.updateBy) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createBy, updateBy, createTime, updateTime);
    }


    /******************************************** 领域方法 ****************************************/
    public void setCreateInfo(String createBy, String updateBy, Date createTime, Date updateTime){
        this.ip= IPUtils.getIpAddr();
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    
}
