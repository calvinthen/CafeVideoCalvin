package com.calvin.cafe.POJO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@Table(name = "auth_authority_member")
public class AuthAuthorityMember {

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "auth_id")
    private String auth_id;

}
