package antifraud.service;


import antifraud.entities.Card;
import antifraud.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }


    @Override
    public Card findByNumber(String number) {
        Optional<Card> card = cardRepository.findByNumberAndLocked(number, false);
        if (card.isPresent()) {
            return card.get();
        }
        Card newCard = new Card(number, false);
        return cardRepository.save(newCard);
    }

    @Override
    public void setUpCard(String cardNumber) {
        if (!cardRepository.existsByNumber(cardNumber)) {
            Card card = new Card(cardNumber, false);
            cardRepository.save(card);
        }
    }

}
