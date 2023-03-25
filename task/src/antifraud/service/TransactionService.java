package antifraud.service;

import antifraud.dtos.FeedbackDTO;
import antifraud.dtos.TransactionDTO;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    Map<Object, Object> processTransaction(TransactionDTO transactionDTO);

    boolean existsById(Long transactionId);

    boolean existsByCardNumber(String number);

    List<Map<Object, Object>> getTransactionHistory(String number);

    List<Map<Object, Object>> getAllTransactionHistory();

    Map<Object, Object> handleFeedback(FeedbackDTO feedback);
}
