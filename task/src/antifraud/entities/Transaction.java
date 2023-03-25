package antifraud.entities;


import antifraud.dtos.TransactionDTO;
import antifraud.common.TransactionResult;
import antifraud.exception.DuplicateException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;


import java.sql.Timestamp;
import java.util.Objects;

// DateFormat "yyyy-MM-ddTHH:mm:ss"

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = @Index( name = "date_index", columnList = "date"))
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private String ip;
    private String region;
    @Column(name = "date")
    private Timestamp date;
    private String number;

    @Enumerated(EnumType.STRING)
    private TransactionResult result;
    @Enumerated(EnumType.STRING)
    private TransactionResult feedback;


    public Transaction(TransactionDTO transactionDTO) {
        this.amount = transactionDTO.getAmount();
        this.ip = transactionDTO.getIp();
        this.region = transactionDTO.getRegion();
        this.date = Timestamp.valueOf(transactionDTO.getDate().replace("T", " "));
        this.number = transactionDTO.getNumber();
    }

    public void setFeedback(TransactionResult feedback) {
        if (this.feedback == null) {
            this.feedback = feedback;
        } else {
            throw new DuplicateException();
        }
    }

    public void setResult(TransactionResult result) {
        if (this.result == null) {
            this.result = result;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
