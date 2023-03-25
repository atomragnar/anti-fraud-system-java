package antifraud.service;

import antifraud.entities.Card;

public interface CardService {

    void saveCard(Card card);

    Card findByNumber(String number);

    void setUpCard(String cardNumber);


}
