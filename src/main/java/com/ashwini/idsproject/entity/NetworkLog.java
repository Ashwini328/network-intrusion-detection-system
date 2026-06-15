package com.ashwini.idsproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;



@Entity
@Table(name = "network_logs")
public class NetworkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceIp;

    private String protocol;

    private String status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "attack_type")
    private String attackType;

    private String severity;

    public NetworkLog() {
    }

    public NetworkLog(String sourceIp,
                      String protocol,
                      String status) {
        this.sourceIp = sourceIp;
        this.protocol = protocol;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttackType(){
        return attackType;
    }

    public void setAttackType(String attackType){
        this.attackType=attackType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}