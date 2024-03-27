package com.geonho1943.sharemylist.model;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.sql.Timestamp;

@Entity
@Table(name = "event_log")
public class EventLog {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_idx")
    private int logIdx;

    @Column(name = "user_idx")
    private int userIdx;

    @Column(name = "active_type")
    private String activeType;

    @Column(name = "active_reg")
    private Timestamp activeReg;

    public EventLog() {
    }

    public EventLog(int userIdx, String activeType) {
        //유저 로그를 위한 생성자
        this.userIdx = userIdx;
        this.activeType = activeType;
    }
}
