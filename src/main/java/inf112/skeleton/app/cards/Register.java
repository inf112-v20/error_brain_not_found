package inf112.skeleton.app.cards;

public class Register {

    private final int registerNumber;
    private ProgramCard programCard;
    private boolean isOpen;

    public Register(int registerNumber) {
        this.registerNumber = registerNumber;
        this.isOpen = true;
        this.programCard = null;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

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
