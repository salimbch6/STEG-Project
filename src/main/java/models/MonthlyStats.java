package models;

public class MonthlyStats {
    private String titre;
    private int janvier;
    private int fevrier;
    private int mars;
    private int avril;
    private int mai;
    private int juin;
    private int juillet;
    private int aout;
    private int septembre;
    private int octobre;
    private int novembre;
    private int decembre;

    // Constructor
    public MonthlyStats(String titre, int janvier, int fevrier, int mars, int avril, int mai, int juin, int juillet, int aout, int septembre, int octobre, int novembre, int decembre) {
        this.titre = titre;
        this.janvier = janvier;
        this.fevrier = fevrier;
        this.mars = mars;
        this.avril = avril;
        this.mai = mai;
        this.juin = juin;
        this.juillet = juillet;
        this.aout = aout;
        this.septembre = septembre;
        this.octobre = octobre;
        this.novembre = novembre;
        this.decembre = decembre;
    }

    // Getters and setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public int getJanvier() { return janvier; }
    public void setJanvier(int janvier) { this.janvier = janvier; }

    public int getFevrier() { return fevrier; }
    public void setFevrier(int fevrier) { this.fevrier = fevrier; }

    public int getMars() { return mars; }
    public void setMars(int mars) { this.mars = mars; }

    public int getAvril() { return avril; }
    public void setAvril(int avril) { this.avril = avril; }

    public int getMai() { return mai; }
    public void setMai(int mai) { this.mai = mai; }

    public int getJuin() { return juin; }
    public void setJuin(int juin) { this.juin = juin; }

    public int getJuillet() { return juillet; }
    public void setJuillet(int juillet) { this.juillet = juillet; }

    public int getAout() { return aout; }
    public void setAout(int aout) { this.aout = aout; }

    public int getSeptembre() { return septembre; }
    public void setSeptembre(int septembre) { this.septembre = septembre; }

    public int getOctobre() { return octobre; }
    public void setOctobre(int octobre) { this.octobre = octobre; }

    public int getNovembre() { return novembre; }
    public void setNovembre(int novembre) { this.novembre = novembre; }

    public int getDecembre() { return decembre; }
    public void setDecembre(int decembre) { this.decembre = decembre; }
}
