package inf112.skeleton.app.cards;

import java.util.ArrayList;
import java.util.Arrays;

public class Registers {

    private final ArrayList<Register> registers;

    public Registers() {
        this.registers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            registers.add(new Register());
        }
    }

    public Register getRegister(int i) {
        return registers.get(i);
    }

    public ArrayList<Register> getRegisters() {
        return registers;
    }

    public boolean canAddCard() {
        for (Register register : registers) {
            if (register.isOpen() && register.getProgramCard() == null) {
                return true;
            }
        }
        return false;
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
        for (Register register : registers) {
            if (card.equals(register.getProgramCard())) {
                register.setProgramCard(null);
                return;
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
            if (!register.isOpen) {
                open++;
            }
        }
        return open;
    }

    public int getCardsSelected() {
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

    public void openOneRegister() {
        for (Register register : registers) {
            if (!register.isOpen()) {
                register.setOpen(true);
                return;
            }
        }
    }

    public void openAllRegisters() {
        for (Register register : registers) {
            register.setOpen(true);
        }
    }

    public void updateRegisters(int damageToken) {
        int lockedRegisters = Math.max(damageToken - 4, 0);
        for (int i = 0; i < 5; i++) {
            registers.get(4 - i).setOpen(i < lockedRegisters);
        }
    }

    @Override
    public String toString() {
        return registers.toString();
    }

    class Register {
        private ProgramCard programCard = null;
        private boolean isOpen = true;

        public ProgramCard getProgramCard() {
            return programCard;
        }

        public void setProgramCard(ProgramCard programCard) {
            this.programCard = programCard;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            this.isOpen = open;
        }

        public boolean hasCard() {
            return programCard != null;
        }

        @Override
        public String toString() {
            return hasCard() ? programCard.toString() : "no card";
        }
    }
}
