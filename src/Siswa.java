/**
 * Siswa.java
 * Kelas model yang merepresentasikan data satu siswa.
 * Berisi NIS, Nama Siswa, dan Alamat.
 */
public class Siswa {

    private String nis;
    private String nama;
    private String alamat;

    // ===================== KONSTRUKTOR =====================

    public Siswa(String nis, String nama, String alamat) {
        this.nis    = nis;
        this.nama   = nama;
        this.alamat = alamat;
    }

    // ===================== GETTER & SETTER =====================

    public String getNis()    { return nis; }
    public String getNama()   { return nama; }
    public String getAlamat() { return alamat; }

    public void setNis(String nis)       { this.nis = nis; }
    public void setNama(String nama)     { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    /**
     * Konversi objek Siswa ke format satu baris CSV.
     * Format: NIS,Nama,Alamat
     */
    public String keCSV() {
        return nis + "," + nama + "," + alamat;
    }

    /**
     * Buat objek Siswa dari satu baris string CSV.
     * @param baris  satu baris dari file CSV
     * @return objek Siswa, atau null jika format tidak valid
     */
    public static Siswa dariCSV(String baris) {
        if (baris == null || baris.trim().isEmpty()) return null;
        String[] kolom = baris.split(",", 3);
        if (kolom.length < 3) return null;
        return new Siswa(kolom[0].trim(), kolom[1].trim(), kolom[2].trim());
    }

    @Override
    public String toString() {
        return "Siswa{NIS='" + nis + "', Nama='" + nama + "', Alamat='" + alamat + "'}";
    }
}
