package antifraud.service;

import antifraud.common.Constants;
import antifraud.dtos.FeedbackDTO;
import antifraud.dtos.ResponseMessage;
import antifraud.dtos.TransactionDTO;
import antifraud.common.TransactionResult;


import antifraud.entities.Card;
import antifraud.entities.Transaction;
import antifraud.exception.DuplicateException;
import antifraud.exception.EntityNotFoundException;
import antifraud.exception.UnprocessableFeedbackException;
import antifraud.repository.*;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;

import static antifraud.common.TransactionResult.*;
import static antifraud.dtos.ResponseMessage.resultInfoMessage;
import static antifraud.dtos.ResponseMessage.transactionHistoryMessage;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AntifraudService antifraudService;
    private final CardService cardService;

    private static ConcurrentHashMap<Long, List<Transaction>> transactionListCache = new ConcurrentHashMap<>();

    public TransactionServiceImpl(TransactionRepository transactionRepository, AntifraudService antifraudService, CardService cardService) {
        this.transactionRepository = transactionRepository;
        this.antifraudService = antifraudService;
        this.cardService = cardService;
    }

    @Override
    public Map<Object, Object> processTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = saveTransaction(transactionDTO);

        Card card = cardService.findByNumber(transaction.getNumber());

        long manualLimit = card.getAllowedLimit();
        long maxLimit = card.getProhibitedLimit();

        TransactionResult result = authorizeTransaction(transaction, maxLimit, manualLimit);

        transaction.setResult(result);
        transactionRepository.save(transaction);
        Map<Object, Object> response = buildReturnMessage(result, transaction, maxLimit, manualLimit);
        clearCache(transaction);

        return response;
    }

    private List<Transaction> getLastHourList(Transaction transaction) {
        Timestamp pastHour = Timestamp.from(transaction.getDate().toInstant().minus(1, ChronoUnit.HOURS));
        return transactionRepository.findByNumberAndDateBetween(transaction.getNumber(), pastHour, transaction.getDate());
    }



    private List<Transaction> getListFromCacheOrRepo(Transaction transaction) {
        if (transactionListCache.get(transaction.getId()) == null) {
            List<Transaction> list = getLastHourList(transaction);
            transactionListCache.put(transaction.getId(), list);
            return list;
        }
        return transactionListCache.get(transaction.getId());
    }

    private void clearCache(Transaction transaction) {
        transactionListCache.remove(transaction.getId(),
                transactionListCache.get(transaction.getId()));
    }

    BiPredicate<Set<String>, String> ifProhibited = (set, s) -> (set.size() > 3 && set.contains(s)) || set.size() > 4;
    BiPredicate<Set<String>, String> ifManual = (set, s) -> (set.size() > 1 && !set.contains(s)) || (set.size() > 2 && set.contains(s));

    private boolean checkRegionProhibited(Transaction transaction) {
        List<Transaction> transactionList = getListFromCacheOrRepo(transaction);
        if (transactionList.size() < 3) {
            return false;
        }
        Set<String> regionSet = transactionList.stream()
                .map(Transaction::getRegion)
                .collect(Collectors.toCollection(HashSet::new));

        return ifProhibited.test(regionSet, transaction.getRegion());
    }
    private boolean checkRegionManual(Transaction transaction) {
        List<Transaction> transactionList = getListFromCacheOrRepo(transaction);

        Set<String> regionSet = transactionList.stream()
                .map(Transaction::getRegion)
                .collect(Collectors.toCollection(HashSet::new));

        if (transactionList.size() < 2) {
            return false;
        }

        return ifManual.test(regionSet, transaction.getRegion());

    }
    private boolean checkIPProhibited(Transaction transaction) {
        List<Transaction> transactionList = getListFromCacheOrRepo(transaction);
        if (transactionList.size() < 3) {
            return false;
        }

        Set<String> ipSet = transactionList.stream()
                .map(Transaction::getIp)
                .collect(Collectors.toCollection(HashSet::new));

        return ifProhibited.test(ipSet, transaction.getIp());
    }
    private boolean checkIPManual(Transaction transaction) {
        List<Transaction> transactionList = getListFromCacheOrRepo(transaction);
        if (transactionList.size() < 2) {
            return false;
        }

        Set<String> ipSet = transactionList.stream()
                .map(Transaction::getIp)
                .collect(Collectors.toCollection(HashSet::new));

        return ifManual.test(ipSet, transaction.getIp());
    }

    private boolean isProhibited(Transaction transaction, long maxLimit) {
        return checkRegionProhibited(transaction) || checkIPProhibited(transaction)
                || transaction.getAmount() > maxLimit || checkIP(transaction) || checkCardNum(transaction);
    }

    private boolean isManual(Transaction transaction, long manualLimit) {
        return checkRegionManual(transaction) || checkIPManual(transaction)
                || transaction.getAmount() > manualLimit;
    }

    public TransactionResult authorizeTransaction(Transaction transaction, long maxLimit, long manualLimit) {
        if (isProhibited(transaction, maxLimit)) {
            return PROHIBITED;
        }
        if (isManual(transaction, manualLimit)) {
            return MANUAL_PROCESSING;
        }
        return ALLOWED;
    }

    private List<String> listInfoMessageProhibited(Transaction transaction, long maxLimit) {
        List<String> info = new ArrayList<>();
        if (transaction.getAmount() > maxLimit) {
            info.add(Constants.INFO_AMOUNT);
        }
        if (checkCardNum(transaction)) {
            info.add(Constants.INFO_FORBIDDEN_CARD);
        }
        if (checkIP(transaction)) {
            info.add(Constants.INFO_FORBIDDEN_IP);
        }
        if (checkIPProhibited(transaction)) {
            info.add(Constants.INFO_IP_CORRELATION);
        }
        if (checkRegionProhibited(transaction)) {
            info.add(Constants.INFO_REGION_CORRELATION);
        }
        return info;
    }

    private String infoMessageProhibited(Transaction transaction, long maxLimit) {
        List<String> info = listInfoMessageProhibited(transaction, maxLimit);
        return switch (info.size()) {
            case 1 -> info.get(0);
            case 2 -> String.format("%s, %s", info.get(0), info.get(1));
            case 3 -> String.format("%s, %s, %s", info.get(0), info.get(1), info.get(2));
            case 4 -> String.format("%s, %s, %s, %s", info.get(0), info.get(1), info.get(2), info.get(3));
            default ->
                    String.format("%s, %s, %s, %s, %s", info.get(0), info.get(1), info.get(2), info.get(3), info.get(4)); // "case 5"
            //default -> "none";
        };
    }

    private List<String> listInfoMessageManual(Transaction transaction, long manualLimit) {
        List<String> info = new ArrayList<>();
        if (transaction.getAmount() > manualLimit) {
            info.add(Constants.INFO_AMOUNT);
        }
        if (checkIPManual(transaction)) {
            info.add(Constants.INFO_IP_CORRELATION);
        }
        if (checkRegionManual(transaction)) {
            info.add(Constants.INFO_REGION_CORRELATION);
        }
        return info;
    }

    private String buildInfoMessageManual(Transaction transaction, long manualLimit) {
        List<String> info = listInfoMessageManual(transaction, manualLimit);
        return switch (info.size()) {
            case 1 -> info.get(0);
            case 2 -> String.format("%s, %s", info.get(0), info.get(1));
            default -> String.format("%s, %s, %s", info.get(0), info.get(1), info.get(2)); // "case 3"
        };
    }

    public Map<Object, Object> buildReturnMessage(TransactionResult transactionResult, Transaction transaction, long maxLimit, long manualLimit) {
        return switch (transactionResult) {
            case PROHIBITED -> resultInfoMessage(transactionResult, infoMessageProhibited(transaction, maxLimit));
            case MANUAL_PROCESSING ->  resultInfoMessage(transactionResult, buildInfoMessageManual(transaction, manualLimit));
            case ALLOWED -> resultInfoMessage(transactionResult, Constants.INFO_ALLOWED);
        };
    }




    /**
     *  Function for handling feedback request
     */

    Function<String, TransactionResult> stringToEnum = TransactionResult::valueOf;

    @Override
    public Map<Object, Object> handleFeedback(FeedbackDTO feedbackDTO) {
        Transaction transaction = findById(feedbackDTO.getTransactionId());

        if (transaction.getFeedback() != null) {
            throw new DuplicateException();
        }

        TransactionResult feedback = stringToEnum.apply(feedbackDTO.getFeedback());
        TransactionResult result = transaction.getResult();

        if (feedback == result) {
            throw new UnprocessableFeedbackException();
        }

        String number = transaction.getNumber();
        Card card = cardService.findByNumber(number);
        transactionRepository.updateFeedbackById(feedback, transaction.getId());

        long increasedAllowed = (long) Math.ceil(0.8 * card.getAllowedLimit() + 0.2 * transaction.getAmount());
        long decreasedAllowed = (long) Math.ceil(0.8 * card.getAllowedLimit() - 0.2 * transaction.getAmount());
        long increasedProhibited = (long) Math.ceil(0.8 * card.getProhibitedLimit() + 0.2 * transaction.getAmount());
        long decreasedProhibited = (long) Math.ceil(0.8 * card.getProhibitedLimit() - 0.2 * transaction.getAmount());

        switch (feedback) {
            case ALLOWED -> {
                switch (result) {
                    case MANUAL_PROCESSING -> card.setAllowedLimit(increasedAllowed);
                    case PROHIBITED -> {
                        card.setAllowedLimit(increasedAllowed);
                        card.setProhibitedLimit(increasedProhibited);
                    }
                }
            }
            case MANUAL_PROCESSING -> {
                switch (result) {
                    case ALLOWED -> card.setAllowedLimit(decreasedAllowed);
                    case PROHIBITED -> card.setProhibitedLimit(increasedProhibited);
                }
            }
            case PROHIBITED -> {
                switch (result) {
                    case ALLOWED -> {
                        card.setAllowedLimit(decreasedAllowed);
                        card.setProhibitedLimit(decreasedProhibited);
                    }
                    case MANUAL_PROCESSING -> card.setProhibitedLimit(decreasedProhibited);
                }
            }
        }

        cardService.saveCard(card);

        Transaction trans = findById(feedbackDTO.getTransactionId());

        return transactionHistoryMessage(trans);

    }


    @Override
    public boolean existsById(Long transactionId) {
        return transactionRepository.existsById(transactionId);
    }

    @Override
    public boolean existsByCardNumber(String number) {
        return transactionRepository.existsByNumber(number);
    }

    @Override
    public List<Map<Object, Object>> getAllTransactionHistory() {
        List<Transaction> transactionList = transactionRepository.findByIdNotNullOrderByIdAsc();
        if (transactionList.isEmpty()) {
            return new ArrayList<>();
        }
        return transactionList.stream().map(ResponseMessage::transactionHistoryMessage)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<Object, Object>> getTransactionHistory(String number) {
        List<Transaction> transactionList = transactionRepository.findByNumberOrderByIdAsc(number);
        if (transactionList.isEmpty()) {
            return new ArrayList<>();
        }
        return transactionList.stream().map(ResponseMessage::transactionHistoryMessage)
                .collect(Collectors.toList());
    }

    private void saveResult(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    private Transaction saveTransaction(TransactionDTO transactionDTO) {
        cardService.setUpCard(transactionDTO.getNumber());
        Transaction transaction = new Transaction(transactionDTO);
        return transactionRepository.save(transaction);
    }

    private boolean checkIP(Transaction transaction) {
        return antifraudService.isIPRegisteredAsForbidden(transaction.getIp());
    }

    private boolean checkCardNum(Transaction transaction) {
        String number = transaction.getNumber();
        return antifraudService.isCardNumberRegisteredAsForbidden(number);
    }

    private Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }



}
