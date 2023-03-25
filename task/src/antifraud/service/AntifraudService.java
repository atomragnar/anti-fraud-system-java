package antifraud.service;

import antifraud.dtos.IpDTO;
import antifraud.dtos.StolenCardNumberDTO;

import antifraud.entities.Card;

import antifraud.entities.StolenCard;
import antifraud.entities.SuspiciousIp;

import java.util.List;
import java.util.Map;


public interface AntifraudService {

    SuspiciousIp saveNewSuspiciousIP(IpDTO ipDTO);

    StolenCard saveNewStolenCardNumber(StolenCardNumberDTO stolenCardNumberDTO);

    List<SuspiciousIp> getSuspiciousIPs();

    List<StolenCard> getStolenCardNumbers();

    Map<Object, Object> deleteIp(String ip);
    Map<Object, Object> deleteCardNumber(String cardNumber);

    void luhnCheck(String value);

    boolean isIPRegisteredAsForbidden(String ip);

    boolean isCardNumberRegisteredAsForbidden(String number);


}
