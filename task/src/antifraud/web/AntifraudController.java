package antifraud.web;


import antifraud.dtos.FeedbackDTO;
import antifraud.dtos.IpDTO;
import antifraud.dtos.StolenCardNumberDTO;
import antifraud.entities.SuspiciousIp;
import antifraud.dtos.TransactionDTO;
import antifraud.exception.EntityNotFoundException;
import antifraud.service.AntifraudService;
import antifraud.service.TransactionService;
import antifraud.validation.IsValidIp;
import antifraud.validation.ValidateCardNumber;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/")
public class AntifraudController {

    private final AntifraudService antifraudService;
    private final TransactionService transactionService;

    public AntifraudController(AntifraudService antifraudService, TransactionService transactionService) {
        this.antifraudService = antifraudService;
        this.transactionService = transactionService;
    }

    @PostMapping("transaction")
    public ResponseEntity<Map<Object, Object>> handleTransactionReq(@Valid @RequestBody TransactionDTO transaction) {
        return new ResponseEntity<>(transactionService.processTransaction(transaction), HttpStatus.OK);
    }

    @PutMapping("transaction")
    public ResponseEntity<Map<Object, Object>> handleFeedback(@Valid @RequestBody FeedbackDTO feedback) {
        return new ResponseEntity<>(transactionService.handleFeedback(feedback), HttpStatus.OK);
    }

    @GetMapping("history/{number}")
    public ResponseEntity<List<Map<Object, Object>>> getTransactionHistory(@PathVariable("number") String number) {
        antifraudService.luhnCheck(number);
        if (transactionService.existsByCardNumber(number)) {
            return new ResponseEntity<>(transactionService.getTransactionHistory(number), HttpStatus.OK);
        }
        throw new EntityNotFoundException();
    }

    @GetMapping("history")
    public ResponseEntity<List<Map<Object, Object>>> getAllTransactionHistory() {
        return new ResponseEntity<>(transactionService.getAllTransactionHistory(), HttpStatus.OK);
    }


    @PostMapping("suspicious-ip")
    public ResponseEntity<SuspiciousIp> handleSusIp(@Valid @RequestBody IpDTO ipDTO) {
        return new ResponseEntity<>(antifraudService.saveNewSuspiciousIP(ipDTO), HttpStatus.OK);
    }

    @DeleteMapping("suspicious-ip/{ip}")
    public ResponseEntity<Map<Object, Object>> deleteSusIp(@Valid @PathVariable("ip") @IsValidIp String ip) {
        return new ResponseEntity<>(antifraudService.deleteIp(ip), HttpStatus.OK);
    }

    @GetMapping("suspicious-ip")
    public ResponseEntity<Object> getSusIp() {
        return new ResponseEntity<>(antifraudService.getSuspiciousIPs(), HttpStatus.OK);
    }

    @PostMapping("stolencard")
    public ResponseEntity<Object> handleStolenCard(@Valid @RequestBody StolenCardNumberDTO stolenCardNumberDTO) {
        return new ResponseEntity<>(antifraudService.saveNewStolenCardNumber(stolenCardNumberDTO), HttpStatus.OK);
    }

    @DeleteMapping("stolencard/{cardNumber}")
    public ResponseEntity<Map<Object, Object>> deleteStolenCard(@PathVariable("cardNumber") @ValidateCardNumber String cardNumber) {
        return new ResponseEntity<>(antifraudService.deleteCardNumber(cardNumber), HttpStatus.OK);
    }

    @GetMapping("stolencard")
    public ResponseEntity<Object> getStolenCard() {
        return new ResponseEntity<>(antifraudService.getStolenCardNumbers(), HttpStatus.OK);
    }



}
