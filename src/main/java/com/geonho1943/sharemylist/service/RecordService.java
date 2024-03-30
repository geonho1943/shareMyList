package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.model.EventLog;
import com.geonho1943.sharemylist.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private EventLogRepository eventLogRepository;

    public void loginLog(int userIdx) {
        //로그인시 로그
        EventLog loginLog = new EventLog(userIdx,"user/login");
        eventLogRepository.save(loginLog);
    }

    public void joinLog(int userIdx){
        //회원가입시 로그
        EventLog joinLog = new EventLog(userIdx,"user/join");
        eventLogRepository.save(joinLog);
    }

    public void resignLog(int userIdx){
        //회원탈퇴시 로그
        EventLog resignLog = new EventLog(userIdx,"user/resign");
        eventLogRepository.save(resignLog);
    }

    public void createPlaylistLog(int userIdx){
        //playlist 생성 로그
        EventLog createPlaylistLog = new EventLog(userIdx,"PL/create");
        eventLogRepository.save(createPlaylistLog);
    }

    public void deletePlaylistLog(int userIdx){
        //playlist 삭제 로그
        EventLog deletePlaylistLog = new EventLog(userIdx, "PL/delete");
        eventLogRepository.save(deletePlaylistLog);
    }

    public void checkPlaylistLog(int userIdx){
        //playlist 조회 로그
        EventLog checkPlaylistLog = new EventLog(userIdx, "PL/check");
        eventLogRepository.save(checkPlaylistLog);
    }


}
