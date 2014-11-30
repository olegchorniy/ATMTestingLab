package ua.pti.myatm;

public class ATM {

    private Card insertedCard;

    private double moneyInATM;

    private boolean valid;

    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM) {
        if (moneyInATM < 0) {
            throw new IllegalArgumentException("moneyInATM = " + moneyInATM);
        }
        this.moneyInATM = moneyInATM;
    }

    // Возвращает каоличестов денег в банкомате
    public double getMoneyInATM() {
        return this.moneyInATM;
    }

    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. 
    //При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode) {
        if (!card.isBlocked() && card.checkPin(pinCode)) {
            this.insertedCard = card;
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }

    //Возвращает сколько денег есть на счету
    public double checkBalance() throws NoCardInserted {
        if (!valid) {
            throw new NoCardInserted();
        }
        return insertedCard.getAccount().getBalance();
    }

    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount 
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM 
    //При успешном снятии денег, указанная сумма должна списываться со счета,
    // и в банкомате должно уменьшаться количество денег
    public double getCash(double amount) throws NoCardInserted,
            IllegalAmountException,
            NotEnoughMoneyInATM,
            NotEnoughMoneyInAccount,
            ATMTransactionError {
        //вставлена ли карточка
        if (!valid) {
            throw new NoCardInserted();
        }
        //корректная ли сума передана
        if (amount < 0) {
            throw new IllegalAmountException(amount + "");
        }

        Account account = insertedCard.getAccount();
        double accountBalance = account.getBalance();

        //достаточно ли денег на счету
        if (accountBalance < amount) {
            throw new NotEnoughMoneyInAccount();
        }
        //достаточно ли денег в банкомате
        if (this.moneyInATM < amount) {
            throw new NotEnoughMoneyInATM();
        }

        double withdrawn = account.withdraw(amount);
        if (withdrawn != amount) {
            throw new ATMTransactionError("withdrawn(" + withdrawn + ") is not "
                    + "equal to amount(" + amount + ")");
        }
        this.moneyInATM -= amount;
        return account.getBalance();
    }
}
