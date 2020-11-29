package ua.skidchenko.registrationform.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.skidchenko.registrationform.entity.Tour;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "tour")

@Entity
@Table(name = "tour_type")
public class TourType {

    public enum Type {
        RECREATION, EXCURSION, SHOPPING
    }

    public TourType(Type type) {
        this.id = (long) (type.ordinal() + 1);
        this.type = type;
    }

    private static final Map<Type, TourType> map = new EnumMap<>(Type.class);

    public static TourType getInstanceByType(Type type) {
        return map.computeIfAbsent(type, TourType::new);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @ManyToMany(fetch = FetchType.EAGER/*, cascade = CascadeType.MERGE*/)
    @JoinTable(name = "tour__tour_type",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_type_id"))
    private List<Tour> tour;

    public void setId(Long id) {
        throw new UnsupportedOperationException("Id cannot be changed!");
    }
}
