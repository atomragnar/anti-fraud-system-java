package antifraud.entities;

import antifraud.dtos.StolenCardNumberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class StolenCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "card_number")
    private String number;

        public StolenCard(StolenCardNumberDTO stolenCardNumberDTO) {
                this.number = stolenCardNumberDTO.getNumber();

        }


}
