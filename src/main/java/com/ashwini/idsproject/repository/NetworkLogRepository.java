package com.ashwini.idsproject.repository;


import com.ashwini.idsproject.entity.NetworkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NetworkLogRepository
        extends JpaRepository<NetworkLog, Long> {

    List<NetworkLog> findBySourceIpContaining(String ip);

    List<NetworkLog> findByStatus(String status);

    List<NetworkLog> findBySeverity(String severity);
}