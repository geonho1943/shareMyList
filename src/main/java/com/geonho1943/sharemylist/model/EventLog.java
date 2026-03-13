package com.geonho1943.sharemylist.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "event_log")
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_idx")
    private Long eventIdx;

    @Column(name = "user_idx")
    private int userIdx;

    @Column(name = "active_type")
    private String activeType;

    @Column(name = "active_reg")
    private Timestamp activeReg;

    public EventLog() {}

    public EventLog(int userIdx, String activeType) {
        this.userIdx = userIdx;
        this.activeType = activeType;
        this.activeReg = Timestamp.from(Instant.now());
    }
}
