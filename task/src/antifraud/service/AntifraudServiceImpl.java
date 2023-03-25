package antifraud.service;


import antifraud.dtos.IpDTO;
import antifraud.dtos.StolenCardNumberDTO;
import antifraud.entities.Card;
import antifraud.entities.StolenCard;
import antifraud.entities.SuspiciousIp;
import antifraud.exception.BadRequestException;
import antifraud.exception.DuplicateException;
import antifraud.exception.EntityNotFoundException;
import antifraud.repository.CardRepository;
import antifraud.repository.StolenCardRepository;
import antifraud.repository.SuspiciousIpRepository;

import org.springframework.stereotype.Service;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static antifraud.dtos.ResponseMessage.*;

@Service
public class AntifraudServiceImpl implements AntifraudService {

    private final StolenCardRepository cardRepository;
    private final SuspiciousIpRepository suspiciousIpRepository;
    private final static Pattern IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");

    public AntifraudServiceImpl(StolenCardRepository cardRepository, SuspiciousIpRepository suspiciousIpRepository) {
        this.cardRepository = cardRepository;
        this.suspiciousIpRepository = suspiciousIpRepository;
    }

    @Override
    public SuspiciousIp saveNewSuspiciousIP(IpDTO ipDTO) {
        if (suspiciousIpRepository.existsByIp(ipDTO.getIp())) {
            throw new DuplicateException();
        }
        return suspiciousIpRepository.save(new SuspiciousIp(ipDTO));
    }

    @Override
    public StolenCard saveNewStolenCardNumber(StolenCardNumberDTO stolenCardNumberDTO) {
        if (cardRepository.existsByNumber(stolenCardNumberDTO.getNumber())) {
            throw new DuplicateException();
        }
        return cardRepository.save(new StolenCard(stolenCardNumberDTO));
    }

    @Override
    public List<SuspiciousIp> getSuspiciousIPs() {
        if (!suspiciousIpRepository.existsFirstBy()) {
            return new ArrayList<>();
        }
        return suspiciousIpRepository.findByOrderByIdAsc();
    }

    @Override
    public List<StolenCard> getStolenCardNumbers() {
        if (!cardRepository.existsFirstBy()) {
            return new ArrayList<>();
        }
        return cardRepository.findByOrderByIdAsc();
    }

    private SuspiciousIp findIPbyIP(String ip) {
        Matcher matcher = IPV4_PATTERN.matcher(ip);
        if (!matcher.matches()) {
            throw new BadRequestException();
        }
        Optional<SuspiciousIp> susIp = suspiciousIpRepository.findByIp(ip);
        if (susIp.isPresent()) {
            return susIp.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Map<Object, Object> deleteIp(String ip) {
        SuspiciousIp suspiciousIp = findIPbyIP(ip);
        suspiciousIpRepository.deleteById(suspiciousIp.getId());
        return deletedIPMessage(suspiciousIp.getIp());
    }

    private StolenCard findCardNumberByNumber(String number) {
        luhnCheck(number);
        Optional<StolenCard> cardNum = cardRepository.findByNumber(number);
        if (cardNum.isPresent()) {
            return cardNum.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Map<Object, Object> deleteCardNumber(String cardNumber) {
        StolenCard stolenCardNumber = findCardNumberByNumber(cardNumber);
        cardRepository.deleteById(stolenCardNumber.getId());
        return deletedCardNumMessage(stolenCardNumber.getNumber());
    }

    public void luhnCheck(String value) {

        if (value == null || value.length() != 16) {
            throw new BadRequestException();
        }

        int[] ints = new int[value.length()];
        for (int i = 0; i < value.length(); i++) {
            ints[i] = Integer.parseInt(value.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            return;
        } else {
            throw new BadRequestException();
        }
    }

    @Override
    public boolean isIPRegisteredAsForbidden(String ip) {
        return suspiciousIpRepository.existsByIp(ip);
    }

    @Override
    public boolean isCardNumberRegisteredAsForbidden(String number) {
        return cardRepository.existsByNumber(number);
    }


}
