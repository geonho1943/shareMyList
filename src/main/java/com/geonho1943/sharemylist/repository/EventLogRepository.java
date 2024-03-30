package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long>{

}
