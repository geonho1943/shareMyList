package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.model.EventLog;
import com.geonho1943.sharemylist.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private EventLogRepository eventLogRepository;

    public void recordLogin(int userIdx) {
        //로그인시 로그
        EventLog loginLog = new EventLog(userIdx,"user/login");
        eventLogRepository.save(loginLog);
    }

    public void recordJoin(int userIdx){
        //회원가입시 로그
        EventLog joinLog = new EventLog(userIdx,"user/join");
        eventLogRepository.save(joinLog);
    }

    public void recordResign(int userIdx){
        //회원탈퇴시 로그
        EventLog resignLog = new EventLog(userIdx,"user/resign");
        eventLogRepository.save(resignLog);
    }

    public void recordCreatePlaylist(int userIdx){
        //playlist 생성 로그
        EventLog createPlaylistLog = new EventLog(userIdx,"PL/create");
        eventLogRepository.save(createPlaylistLog);
    }

    public void recordDeletePlaylist(int userIdx){
        //playlist 삭제 로그
        EventLog deletePlaylistLog = new EventLog(userIdx, "PL/delete");
        eventLogRepository.save(deletePlaylistLog);
    }

    public void recordCheckPlaylist(int userIdx){
        //playlist 조회 로그
        EventLog checkPlaylistLog = new EventLog(userIdx, "PL/check");
        eventLogRepository.save(checkPlaylistLog);
    }

    public void recordCreateCard(int userIdx){
        //card 생성 로그
        EventLog createPlaylistLog = new EventLog(userIdx,"card/create");
        eventLogRepository.save(createPlaylistLog);
    }

    public void recordDeleteCard(int userIdx){
        //card 삭제 로그
        EventLog createPlaylistLog = new EventLog(userIdx,"card/delete");
        eventLogRepository.save(createPlaylistLog);
    }

    public void recordCheckCard(int userIdx){
        //card 조회 로그
        EventLog createPlaylistLog = new EventLog(userIdx,"card/check");
        eventLogRepository.save(createPlaylistLog);
    }

}
