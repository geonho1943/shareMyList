package com.geonho1943.sharemylist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.xml.crypto.Data;

@Entity
@Table(name = "event_log")
public class EventLog {

    @Id
    @Column(name = "log_idx")
    private int logIdx;

    @Column(name = "user_idx")
    private int userIdx;

    @Column(name = "action_type")
    private String ActionType;

    @Column(name = "action_reg")
    private Data ActionReg;

}
