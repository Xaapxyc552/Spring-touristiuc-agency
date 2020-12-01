package ua.skidchenko.touristic_agency.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "check_status")
public class CheckStatus {

    public enum Status {
        WAITING_FOR_CONFIRM, CONFIRMED, DECLINED, CANCELED
    }

    public CheckStatus(Status status) {
        this.id = (long) status.ordinal() + 1;
        this.status = status;
    }

    private static final Map<Status, CheckStatus> map = new EnumMap<>(Status.class);

    public static CheckStatus getInstanceByEnum(Status status) {
        return map.computeIfAbsent(status, CheckStatus::new);
    }

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public void setId(Long id) {
        throw new UnsupportedOperationException("Id cannot be changed!");
    }
}