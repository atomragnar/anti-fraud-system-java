package antifraud.entities;

import antifraud.common.Constants;

import antifraud.dtos.StolenCardNumberDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "card_number")
    private String number;
    @JsonIgnore
    long allowedLimit = Constants.INITIAL_ALLOWED_THRESHOLD;

    @JsonIgnore
    long prohibitedLimit = Constants.INITIAL_PROHIBITED_LIMIT;
    @JsonIgnore
    boolean locked;

    public Card(String number, boolean locked) {
        this.number = number;
        this.locked = locked;
    }



}



