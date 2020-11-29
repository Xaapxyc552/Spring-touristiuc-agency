package ua.skidchenko.registrationform.entity;

import lombok.*;
import ua.skidchenko.registrationform.entity.enums.CheckStatus;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_price")
    private Long totalPrice;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "status_id")
    private CheckStatus status;

    public void setTotalPrice(Long totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative!");
        }
        this.totalPrice = totalPrice;
    }
}
