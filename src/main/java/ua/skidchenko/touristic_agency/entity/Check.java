package ua.skidchenko.touristic_agency.entity;

import lombok.*;
import ua.skidchenko.touristic_agency.entity.enums.CheckStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor

@Entity
@Table(name = "check")
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_time",updatable = false, columnDefinition = "TIMESTAMP")
    LocalDateTime creationTime;

    @Column(name = "modified_time", columnDefinition = "TIMESTAMP")
    LocalDateTime modifiedTime;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_price")
    private Long totalPrice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private CheckStatus status;

    public void setTotalPrice(Long totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative!");
        }
        this.totalPrice = totalPrice;
    }
}
