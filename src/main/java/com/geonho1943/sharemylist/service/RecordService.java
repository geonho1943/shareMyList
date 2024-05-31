package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.model.EventLog;
import com.geonho1943.sharemylist.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private EventLogRepository eventLogRepository;

    private void recodeEvent(int userIdx, String logType){
        EventLog loginLog = new EventLog(userIdx,logType);
        eventLogRepository.save(loginLog);
    }

    public void recordLogin(int userIdx) {
        recodeEvent(userIdx,"user/login");
    }

    public void recordJoin(int userIdx){
        recodeEvent(userIdx,"user/join");
    }

    public void recordResign(int userIdx){
        recodeEvent(userIdx,"user/resign");
    }

    public void recordCreatePlaylist(int userIdx){
        recodeEvent(userIdx,"PL/create");
    }

    public void recordDeletePlaylist(int userIdx){
        recodeEvent(userIdx, "PL/delete");
    }

    public void recordCheckPlaylist(int userIdx){
        //playlist 조회 로그
        recodeEvent(userIdx, "PL/check");
    }

    public void recordCreateCard(int userIdx){
        recodeEvent(userIdx,"card/create");
    }

    public void recordDeleteCard(int userIdx){
        recodeEvent(userIdx,"card/delete");
    }

    public void recordCheckCard(int userIdx){
        recodeEvent(userIdx,"card/check");
    }

}
