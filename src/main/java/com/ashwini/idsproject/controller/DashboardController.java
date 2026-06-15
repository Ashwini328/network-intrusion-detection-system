package com.ashwini.idsproject.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.ashwini.idsproject.entity.NetworkLog;
import com.ashwini.idsproject.repository.NetworkLogRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final NetworkLogRepository repository;

    public DashboardController(NetworkLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String dashboard(
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) String severity,
            Model model) {

        List<NetworkLog> logs;

        if (severity != null && !severity.isEmpty()) {

            logs = repository.findBySeverity(severity);

        }
        else if (ip != null && !ip.isEmpty()) {

            logs = repository.findBySourceIpContaining(ip);

        }
        else {

            logs = repository.findAll();

        }

        long totalPackets = repository.count();

        long normalTraffic = repository.findAll()
                .stream()
                .filter(log -> "Normal".equals(log.getStatus()))
                .count();

        long attackTraffic = repository.findAll()
                .stream()
                .filter(log -> "Attack".equals(log.getStatus()))
                .count();

        double attackPercentage = 0;

        if (totalPackets > 0) {
            attackPercentage =
                    ((double) attackTraffic / totalPackets) * 100;
        }

        model.addAttribute("logs", logs);
        model.addAttribute("totalPackets", totalPackets);
        model.addAttribute("normalTraffic", normalTraffic);
        model.addAttribute("attackTraffic", attackTraffic);
        model.addAttribute("attackPercentage",
                String.format("%.2f", attackPercentage));

        model.addAttribute(
                "attackLogs",
                repository.findByStatus("Attack")
        );

        long highSeverity =
                repository.findAll()
                        .stream()
                        .filter(log -> "High".equals(log.getSeverity()))
                        .count();

        model.addAttribute("highSeverity", highSeverity);

        long ddos =
                repository.findAll()
                        .stream()
                        .filter(log -> "DDoS".equals(log.getAttackType()))
                        .count();

        long sql =
                repository.findAll()
                        .stream()
                        .filter(log -> "SQL Injection".equals(log.getAttackType()))
                        .count();

        long brute =
                repository.findAll()
                        .stream()
                        .filter(log -> "Brute Force".equals(log.getAttackType()))
                        .count();

        long portScan =
                repository.findAll()
                        .stream()
                        .filter(log -> "Port Scan".equals(log.getAttackType()))
                        .count();

        model.addAttribute("ddos", ddos);
        model.addAttribute("sql", sql);
        model.addAttribute("brute", brute);
        model.addAttribute("portScan", portScan);


        return "dashboard";
    }

    @GetMapping("/generate")
    public String generateTraffic() {

        Random random = new Random();

        String[] ips = {
                "192.168.1.10",
                "192.168.1.20",
                "192.168.1.77",
                "192.168.1.121",
                "192.168.1.173"
        };

        String[] protocols = {
                "TCP",
                "UDP",
                "ICMP"
        };

        String[] attackTypes = {
                "DDoS",
                "Port Scan",
                "SQL Injection",
                "Brute Force"
        };




        NetworkLog log = new NetworkLog();

        String status =
                random.nextBoolean() ? "Normal" : "Attack";

        log.setSourceIp(
                ips[random.nextInt(ips.length)]
        );

        log.setProtocol(
                protocols[random.nextInt(protocols.length)]
        );

        log.setStatus(status);

        if ("Attack".equals(status)) {

            String attack =
                    attackTypes[random.nextInt(attackTypes.length)];

            log.setAttackType(attack);

        } else {

            log.setAttackType("None");
        }

        log.setTimestamp(LocalDateTime.now());


        // Debug Output
        System.out.println("================================");
        System.out.println("Status      : " + log.getStatus());
        System.out.println("Attack Type : " + log.getAttackType());
        System.out.println("Timestamp   : " + log.getTimestamp());
        System.out.println("================================");

        repository.save(log);

        return "redirect:/";
    }
    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=attack_logs.csv"
        );

        PrintWriter writer = response.getWriter();

        writer.println("ID,IP,Protocol,Attack Type,Severity,Time");

        List<NetworkLog> attacks =
                repository.findByStatus("Attack");

        for (NetworkLog log : attacks) {

            writer.println(
                    log.getId() + "," +
                            log.getSourceIp() + "," +
                            log.getProtocol() + "," +
                            log.getAttackType() + "," +
                            log.getSeverity() + "," +
                            log.getTimestamp()
            );
        }

        writer.flush();
        writer.close();
    }


}