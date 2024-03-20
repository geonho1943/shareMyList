package com.geonho1943.sharemylist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.xml.crypto.Data;

@Entity
@Table(name = "usage_count")
public class UsageCount {

    @Id
    @Column(name = "count_idx")
    private int countIdx;

    @Column(name = "funtion_name")
    private String funtionName;

    @Column(name = "count_reg")
    private Data countReg;

    //TODO: 카운트 컬럼 추가


    public UsageCount(int countIdx, String funtionName, Data countReg) {
        this.countIdx = countIdx;
        this.funtionName = funtionName;
        this.countReg = countReg;
    }
}
