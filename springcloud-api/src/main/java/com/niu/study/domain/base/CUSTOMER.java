package com.niu.study.domain.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

@Data
@ApiModel
@JsonIgnoreProperties(value="系统常量数据")
public class CUSTOMER {
    @ApiModelProperty("常量id")
    public String id;
    @ApiModelProperty("常量name")
    public String name;

    public CUSTOMER(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
