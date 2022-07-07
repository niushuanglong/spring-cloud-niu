package com.niu.study.domain;


import com.niu.study.domain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="AccessToken")
@Entity
public class AccessToken {
    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;
    //身份令牌
    @Column(name = "access_token")
    private String accessToken;
    //过期时间
    @CreationTimestamp
    @Column(name = "expire_time", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date expireTime;

    public AccessToken(String accessToken, Date expireTime) {
        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }
}
