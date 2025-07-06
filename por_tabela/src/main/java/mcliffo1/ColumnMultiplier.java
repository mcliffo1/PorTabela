package mcliffo1;

public class ColumnMultiplier extends Items {
    private double multiplier;

    public ColumnMultiplier(double multiplier) {
        super("Odd Column Multiplier", "Increases score of cells in odd columns by " + multiplier + "x for this round.");
        this.multiplier = multiplier;
    }

    @Override
    public void applyEffect() {
        
    }


    @Override
    public int applyScoringEffect(int colNum, int rowNum, int valor) {
        if (colNum % 2 == 1) {
            valor = (int) (valor * multiplier);
        }

        return valor;
    }

}

