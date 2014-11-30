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
public class IllegalAmountException extends ATMException {

    /**
     * Constructs an instance of <code>IllegalAmountException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalAmountException(String msg) {
        super(msg);
    }
}
