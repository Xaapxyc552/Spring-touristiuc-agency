package ua.skidchenko.registrationform.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.skidchenko.registrationform.entity.Check;
import ua.skidchenko.registrationform.entity.Tour;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "check_status")
public class CheckStatus {

    public enum Status {
        WAITING_FOR_CONFIRM, CONFIRMED, DECLINED
    }

    public CheckStatus(Status status) {
        this.status = status;
    }

    private static final Map<Status, CheckStatus> map = new EnumMap<>(Status.class);

    public static CheckStatus getInstanceByEnum(Status status) {
        return map.computeIfAbsent(status, CheckStatus::new);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

}