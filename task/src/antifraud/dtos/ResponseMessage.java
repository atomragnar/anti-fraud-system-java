package antifraud.dtos;

import antifraud.common.TransactionResult;
import antifraud.entities.Transaction;


import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseMessage {



    public static Map<Object, Object> updatedAccessMessage(boolean operation, String username) {
        Map<Object, Object> returnConfirmation = new LinkedHashMap<>();

        String returnString = operation ? "locked" : "unlocked";
        returnConfirmation.put("status",
                "User %s %s!".formatted(username, returnString)
        );

        return returnConfirmation;

    }

    public static Map<Object, Object> deletedUserMessage(String username) {
        Map<Object, Object> returnConfirmation = new LinkedHashMap<>();
        returnConfirmation.put("username", username);
        returnConfirmation.put("status", "Deleted successfully!");
        return returnConfirmation;
    }


    public static Map<Object, Object> statusMessage(String message) {
        Map<Object, Object> returnConfirmation = new LinkedHashMap<>();
        returnConfirmation.put("status", message);
        return returnConfirmation;
    }

    public static Map<Object, Object> resultInfoMessage(TransactionResult result, String info) {
        Map<Object, Object> returnConfirmation = new LinkedHashMap<>();
        returnConfirmation.put("result", result.name());
        returnConfirmation.put("info", info);
        return returnConfirmation;
    }


    public static Map<Object, Object> deletedIPMessage(String ipNumber) {
        Map<Object, Object> returnConfirmation = new LinkedHashMap<>();
        returnConfirmation.put("status", String.format("IP %s successfully removed!", ipNumber));
        return returnConfirmation;
    }

    public static Map<Object, Object> deletedCardNumMessage(String cardNumber) {
        Map<Object, Object> returnConfirmation = new LinkedHashMap<>();
        returnConfirmation.put("status", String.format("Card %s successfully removed!", cardNumber));
        return returnConfirmation;
    }

    public static Map<Object, Object> transactionHistoryMessage(Transaction t) {
        Map<Object, Object> returnMessage = new LinkedHashMap<>();
        returnMessage.put("transactionId", t.getId());
        returnMessage.put("amount", t.getAmount());
        returnMessage.put("ip", t.getIp());
        returnMessage.put("number", t.getNumber());
        returnMessage.put("region", t.getRegion());
        returnMessage.put("date", t.getDate().toString().split("\\.")[0].replace(" ", "T"));
        returnMessage.put("result", t.getResult());
        returnMessage.put("feedback", t.getFeedback() == null ? "" : t.getFeedback());
        return returnMessage;
    }

    public static Map<Object, Object> transactionHistoryMessage(Transaction t, TransactionResult tR) {
        Map<Object, Object> returnMessage = new LinkedHashMap<>();
        returnMessage.put("transactionId", t.getId());
        returnMessage.put("amount", t.getAmount());
        returnMessage.put("ip", t.getIp());
        returnMessage.put("number", t.getNumber());
        returnMessage.put("region", t.getRegion());
        returnMessage.put("date", t.getDate().toString().split("\\.")[0].replace(" ", "T"));
        returnMessage.put("result", t.getResult());
        returnMessage.put("feedback", tR);
        return returnMessage;
    }


}
