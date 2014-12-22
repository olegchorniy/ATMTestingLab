package ua.pti.myatm;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;

/**
 *
 * @author oleg
 */
public class ATMTest {

    public static final double DEFAULT_MONEY = 1000.0;

    public static final int DEFAULT_PIN = 1234;

    @Test(expected = IllegalArgumentException.class)
    public void createATMWithNegativeMoney() {
        ATM atm = new ATM(-5);
    }

    @Test
    public void getMoneyInATM() {
        ATM atm = new ATM(1000.0);
        double expected = 1000.0;
        double actual = atm.getMoneyInATM();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void validateCardBlockedCard() {
        ATM atm = new ATM(DEFAULT_MONEY);
        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        assertFalse(atm.validateCard(card, DEFAULT_PIN));
        verify(card).isBlocked();
    }

    @Test
    public void validateCardNotBlockedIncorrectPin() {
        ATM atm = new ATM(DEFAULT_MONEY);
        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.FALSE);

        assertFalse(atm.validateCard(card, 1234));
        InOrder inOrder = inOrder(card);
        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(anyInt());
    }

    @Test
    public void validateCardNotBlockedCorrectPin() {
        ATM atm = new ATM(DEFAULT_MONEY);
        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);

        assertTrue(atm.validateCard(card, 1234));
        InOrder inOrder = inOrder(card);
        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(anyInt());
    }

    @Test(expected = NoCardInserted.class)
    public void checkBalanceNoCardInserted() throws NoCardInserted {
        ATM atm = new ATM(DEFAULT_MONEY);
        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.TRUE);
        atm.validateCard(card, DEFAULT_PIN);
        atm.checkBalance();
    }

    @Test
    public void checkBalanceAllOk() throws NoCardInserted {
        ATM atm = new ATM(DEFAULT_MONEY);
        double expected = 100.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(expected);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        double actual = atm.checkBalance();

        assertEquals(expected, actual, 0.0);
    }

    @Test(expected = NoCardInserted.class)
    public void getCashNoCardInserted() throws Exception {
        ATM atm = new ATM(DEFAULT_MONEY);
        atm.getCash(DEFAULT_MONEY);
    }

    @Test(expected = IllegalAmountException.class)
    public void getCashNegativeAmount() throws Exception {
        ATM atm = new ATM(DEFAULT_MONEY);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);

        atm.validateCard(card, DEFAULT_PIN);
        atm.getCash(-1.0);
    }

    @Test(expected = NotEnoughMoneyInAccount.class)
    public void getCashNotEnoughMoneyInAccount() throws Exception {
        ATM atm = new ATM(DEFAULT_MONEY);

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(100.0);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        try {
            atm.getCash(200.0);
        } catch (NotEnoughMoneyInAccount ex) {
            verify(account).getBalance();
            throw ex;
        }
    }

    @Test(expected = NotEnoughMoneyInATM.class)
    public void getCashNotEnoughMoneyInATM() throws Exception {
        ATM atm = new ATM(1000.0);

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(5000.0);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        atm.getCash(2000.0);
    }

    @Test(expected = ATMTransactionError.class)
    public void getCashWithdrawnNotEqualToAmount() throws Exception {
        ATM atm = new ATM(1000.0);

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(5000.0);
        when(account.withdraw(200.0)).thenReturn(150.0);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        try {
            atm.getCash(200.0);
        } catch (ATMTransactionError err) {
            InOrder inOrder = inOrder(account);
            inOrder.verify(account).getBalance();
            inOrder.verify(account).withdraw(200.0);
            throw err;
        }
    }

    @Test
    public void getCashWithdrawnAllOk() throws Exception {
        ATM atm = new ATM(1000.0);
        double withdraw = 200.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(5000.0);
        when(account.withdraw(withdraw)).thenReturn(withdraw);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        atm.getCash(withdraw);

        InOrder inOrder = inOrder(account);
        inOrder.verify(account).getBalance();
        inOrder.verify(account).withdraw(withdraw);
        assertEquals(800.0, atm.getMoneyInATM(), 0.0);
    }

    /**
     * ****Boundary tests*****
     */
    @Test
    public void ATMCreatingBoundaryTest() {
        //just create ATM with boundary money value
        ATM atm = new ATM(0);
    }
    
    @Test
    public void getCashBoundaryAmountTest() throws Exception {
        ATM atm = new ATM(1000.0);
        double withdraw = 0.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(5000.0);
        when(account.withdraw(withdraw)).thenReturn(withdraw);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        atm.getCash(withdraw);
    }
    
    @Test
    public void getCashAccountBalanceEqualToAmountTest() throws Exception {
        ATM atm = new ATM(1000.0);
        double withdraw = 200.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(withdraw);
        when(account.withdraw(withdraw)).thenReturn(withdraw);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        atm.getCash(withdraw);
    }
    
    @Test
    public void getCashMoneyInATMEqualToAmountTest() throws Exception {
        double withdraw = 200.0;
        ATM atm = new ATM(withdraw);

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(withdraw);
        when(account.withdraw(withdraw)).thenReturn(withdraw);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(anyInt())).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);

        atm.validateCard(card, DEFAULT_PIN);
        atm.getCash(withdraw);
    }
}