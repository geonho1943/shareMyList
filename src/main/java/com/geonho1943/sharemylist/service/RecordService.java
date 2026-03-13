package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.model.EventLog;
import com.geonho1943.sharemylist.repository.EventLogRepository;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    private final EventLogRepository eventLogRepository;

    public RecordService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    private void recordEvent(int userIdx, String logType){
        EventLog loginLog = new EventLog(userIdx,logType);
        eventLogRepository.save(loginLog);
    }

    public void recordLogin(int userIdx) {
        recordEvent(userIdx,"user/login");
    }

    public void recordJoin(int userIdx){
        recordEvent(userIdx,"user/register");
    }

    public void recordResign(int userIdx){
        recordEvent(userIdx,"user/unregister");
    }

    public void recordCreatePlaylist(int userIdx){
        recordEvent(userIdx,"PL/create");
    }

    public void recordDeletePlaylist(int userIdx){
        recordEvent(userIdx, "PL/delete");
    }

    public void recordCheckPlaylist(int userIdx){
        recordEvent(userIdx, "PL/check");
    }

    public void recordCreateCard(int userIdx){
        recordEvent(userIdx,"card/create");
    }

    public void recordDeleteCard(int userIdx){
        recordEvent(userIdx,"card/delete");
    }

    public void recordCheckCard(int userIdx){
        recordEvent(userIdx,"card/check");
    }

}
