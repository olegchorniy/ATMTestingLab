/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

/**
 *
 * @author Олег
 */
public class ATMTransactionError extends ATMException {

    /**
     * Constructs an instance of <code>ATMTransactionError</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ATMTransactionError(String msg) {
        super(msg);
    }
}
