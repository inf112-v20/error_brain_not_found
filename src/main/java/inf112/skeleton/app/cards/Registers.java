package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class Registers {

    private final ArrayList<Register> registers;
    private int nextReg;

    public Registers() {
        this.registers = new ArrayList<>();
        this.nextReg = 0;
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
        registers.get(nextReg).setProgramCard(card);
        nextReg++;
    }

    public void remove(ProgramCard card) {
        for (Register register : registers) {
            if (card.equals(register.getProgramCard())) {
                register.setProgramCard(null);
                nextReg--;
                return;
            }
        }
    }

    public ProgramCard getCard(int i) {
        return registers.get(i).getProgramCard();
    }

    public boolean contains(ProgramCard card) {
        for (Register register : registers) {
            if (register.getProgramCard().equals(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRegistersWithoutCard() {
        return registers.get(nextReg).isOpen() && registers.get(nextReg).hasCard();
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

    public void clear() {
        for (Register register : registers) {
            register.setProgramCard(null);
        }
    }

    public void lockRegister() {
        for (int i = 4; i >= 0; i--) {
            if (registers.get(i).isOpen()) {
                registers.get(i).setOpen(false);
                return;
            }
        }
    }

    public void openRegister() {
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

    public void lockRegisters(int num) {
        for (int i = 4; i > num; i--) {
            registers.get(i).setOpen(false);
        }
    }

    public void updateRegisters(int damageToken) {
        int lockedRegisters = Math.max(damageToken - 4, 0);
        for (int i = 0; i < 5; i++) {
            registers.get(i).setOpen(4 - i >= lockedRegisters);
        }
    }

    static class Register {
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
    }
}
