package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.model.EventLog;
import com.geonho1943.sharemylist.repository.EventLogRepository;
//import com.geonho1943.sharemylist.repository.UsageCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    @Autowired
    private EventLogRepository eventLogRepository;


    public void loginlog(int userIdx) {
        //로그인시 로그
        EventLog joinLog = new EventLog(userIdx,"login");
        eventLogRepository.save(joinLog);
    }

    public void joinlog(int userIdx){
        //회원가입시 로그
        EventLog joinLog = new EventLog(userIdx,"join");
        eventLogRepository.save(joinLog);
    }

    public void resignlog(int userIdx){
        //회원탈퇴시 로그
        EventLog joinLog = new EventLog(userIdx,"resign");
        eventLogRepository.save(joinLog);
    }


}
