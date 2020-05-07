package inf112.skeleton.app.cards;

import java.util.ArrayList;
import java.util.Arrays;

public class Registers {

    private final ArrayList<Register> registers;

    public Registers() {
        this.registers = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            registers.add(new Register(i));
        }
    }

    public Register getRegister(ProgramCard card) {
        for (Register reg : registers) {
            if (reg.hasCard() && reg.getProgramCard().equals(card)) {
                return reg;
            }
        }
        return null;
    }

    public Register getRegister(int i) {
        return registers.get(i);
    }

    public ArrayList<Register> getRegisters() {
        return registers;
    }

    public void addCard(ProgramCard card) {
        for (Register reg : registers) {
            if (!reg.hasCard()) {
                reg.setProgramCard(card);
                return;
            }
        }
    }

    public void remove(ProgramCard card) {
        boolean foundCard = false;
        for (int i = 0; i < 5; i++) {
            Register register = registers.get(i);
            if (!foundCard && register.hasCard() && card.equals(register.getProgramCard())) {
                foundCard = true;
            }
            if (foundCard) {
                if (i < 4 && registers.get(i + 1).isOpen()) {
                    register.setProgramCard(registers.get(i + 1).getProgramCard());
                } else {
                    register.setProgramCard(null);
                    return;
                }
            }
        }
    }

    public ProgramCard getCard(int i) {
        return registers.get(i).getProgramCard();
    }

    public boolean contains(ProgramCard card) {
        for (Register register : registers) {
            if (register.hasCard() && register.getProgramCard().equals(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRegistersWithoutCard() {
        for (Register register : registers) {
            if (!register.hasCard()) {
                return true;
            }
        }
        return false;
    }

    public int getOpenRegisters() {
        int open = 0;
        for (Register register : registers) {
            if (register.isOpen()) {
                open++;
            }
        }
        return open;
    }

    public int getNumberOfCardsSelected() {
        int cards = 0;
        for (Register register : registers) {
            if (!register.hasCard()) {
                cards++;
            }
        }
        return cards;
    }

    public void setSelectedCards(ProgramCard... cards) {
        for (ProgramCard card : new ArrayList<>(Arrays.asList(cards))) {
            this.addCard(card);
        }
    }

    public void clear(boolean clearOnlyOpen) {
        for (Register register : registers) {
            if (!clearOnlyOpen || register.isOpen()) {
                register.setProgramCard(null);
            }
        }
    }

    public ArrayList<ProgramCard> getCards() {
        ArrayList<ProgramCard> cards = new ArrayList<>();
        for (Register register : registers) {
            if (register.hasCard()) {
                cards.add(register.getProgramCard());
            }
        }
        return cards;
    }

    public void updateRegisters(int damageToken) {
        int lockedRegisters = Math.max(damageToken - 4, 0);
        for (int i = 0; i < 5; i++) {
            registers.get(4 - i).setOpen(i >= lockedRegisters);
        }
    }

    @Override
    public String toString() {
        return registers.toString();
    }
}
