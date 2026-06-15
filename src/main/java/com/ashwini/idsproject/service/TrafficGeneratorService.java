package com.ashwini.idsproject.service;

import com.ashwini.idsproject.entity.NetworkLog;
import com.ashwini.idsproject.repository.NetworkLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TrafficGeneratorService {

    private final NetworkLogRepository repository;

    public TrafficGeneratorService(NetworkLogRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 5000)
    public void generateTraffic() {

        Random random = new Random();

        String[] protocols = {"TCP", "UDP", "ICMP"};
        String[] attackTypes = {
                "DDoS",
                "Port Scan",
                "SQL Injection",
                "Brute Force"
        };

        String ip =
                "192.168.1." +
                        (random.nextInt(200) + 1);

        String currentStatus =
                random.nextBoolean() ? "Normal" : "Attack";

        NetworkLog log = new NetworkLog();

        log.setSourceIp(ip);
        log.setProtocol(
                protocols[random.nextInt(protocols.length)]
        );
        log.setStatus(currentStatus);
        log.setTimestamp(java.time.LocalDateTime.now());


        if ("Attack".equals(currentStatus)) {

            String attack =
                    attackTypes[random.nextInt(attackTypes.length)];

            log.setAttackType(attack);

            if (attack.equals("DDoS")) {
                log.setSeverity("High");
            }
            else if (attack.equals("SQL Injection")) {
                log.setSeverity("High");
            }
            else if (attack.equals("Brute Force")) {
                log.setSeverity("Medium");
            }
            else {
                log.setSeverity("Low");
            }

        } else {

            log.setAttackType("None");
            log.setSeverity("Safe");
        }

        repository.save(log);

        System.out.println(
                "Traffic Generated : "
                        + log.getAttackType()
        );
    }
}