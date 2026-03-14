package com.Nexora.NexoraFinance.exceptions;

public class InvalidTransactionException extends RuntimeException{
    public InvalidTransactionException(String message) {
        super(message);
    }
}
