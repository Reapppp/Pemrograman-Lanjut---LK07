import java.io.*;
import java.util.*;

/**
 * SiswaCSV.java
 * Kelas yang menangani semua operasi File I/O ke/dari file siswa.csv.
 * Bertanggung jawab untuk membaca, menulis, update, dan hapus data di CSV.
 */
public class SiswaCSV {

    private final String namaFile;

    // ===================== KONSTRUKTOR =====================

    public SiswaCSV(String namaFile) {
        this.namaFile = namaFile;
    }

    // ===================== READ: Muat semua data =====================

    /**
     * Membaca semua data dari file CSV.
     * Jika file tidak ada, file baru akan dibuat secara otomatis.
     *
     * @return List berisi objek Siswa dari file CSV
     * @throws IOException jika file gagal dibaca atau dibuat
     */
    public List<Siswa> muatSemua() throws IOException {
        List<Siswa> daftarSiswa = new ArrayList<>();
        File file = new File(namaFile);

        // Penanganan exception: jika file tidak ada, buat baru
        if (!file.exists()) {
            file.createNewFile();
            return daftarSiswa; // kembalikan list kosong
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                Siswa s = Siswa.dariCSV(baris);
                if (s != null) {
                    daftarSiswa.add(s);
                }
            }
        }

        return daftarSiswa;
    }

    // ===================== CREATE: Tambah satu baris =====================

    /**
     * Menambahkan satu data siswa ke akhir file CSV.
     *
     * @param siswa objek Siswa yang akan ditambahkan
     * @throws IOException jika gagal menulis ke file
     */
    public void tambah(Siswa siswa) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaFile, true))) {
            bw.write(siswa.keCSV());
            bw.newLine();
        }
    }

    // ===================== UPDATE & DELETE: Tulis ulang semua data =====================

    /**
     * Menulis ulang seluruh isi file CSV dari list yang diberikan.
     * Digunakan setelah operasi update atau delete.
     *
     * @param daftarSiswa list lengkap data siswa yang akan disimpan
     * @throws IOException jika gagal menulis ke file
     */
    public void simpanSemua(List<Siswa> daftarSiswa) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaFile, false))) {
            for (Siswa s : daftarSiswa) {
                bw.write(s.keCSV());
                bw.newLine();
            }
        }
    }
}
