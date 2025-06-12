package moneyexpense.models;

public class Pengeluaran {
    private int id;
    private int idPengguna;
    private String tanggal;
    private double jumlah;
    private String keterangan;

    public Pengeluaran(int idPengguna, String tanggal, double jumlah, String keterangan) {
        this.idPengguna = idPengguna;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    public Pengeluaran() {

    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}