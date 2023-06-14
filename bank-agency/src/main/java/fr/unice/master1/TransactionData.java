package fr.unice.master1;

public class TransactionData {

    private Integer destinationClientId;
    private Double amount;
    private Integer clientsId;

    public TransactionData(Integer destinationClientId, Double amount, Integer clientsId) {
        this.destinationClientId = destinationClientId;
        this.amount = amount;
        this.clientsId = clientsId;
    }

    public Integer getDestinationClientId() {
        return destinationClientId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getClientsId() {
        return clientsId;
    }
}
