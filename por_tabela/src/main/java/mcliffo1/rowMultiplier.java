package mcliffo1;

public class rowMultiplier extends Items {
    private double multiplier;

    public rowMultiplier(double multiplier) {
        super("Even Row Multiplier", "Increases score of cells in odd columns by " + multiplier + "x for this round.");
        this.multiplier = multiplier;
    }

    @Override
    public void applyEffect() {
        
    }


    @Override
    public int applyScoringEffect(int colNum, int rowNum, int valor) {
        if (rowNum % 2 == 0) {
            valor = (int) (valor * multiplier);
        }

        return valor;
    }
    @Override
    public String getImagePath() {
        return "/Images/RowMult.png";
    }

}

