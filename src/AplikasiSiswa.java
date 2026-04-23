import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * AplikasiSiswa.java
 * Frame utama aplikasi CRUD Data Siswa Perpustakaan.
 *
 * Struktur frame:
 *  - NORTH  : PanelJudul
 *  - CENTER : FormPanel (entri + tombol) + TabelPanel
 *  - SOUTH  : Status bar
 *
 * Kelas ini menghubungkan FormPanel, TabelPanel, dan SiswaCSV.
 */
public class AplikasiSiswa extends JFrame {

    // ===================== KOMPONEN =====================

    private FormPanel  formPanel;
    private TabelPanel tabelPanel;
    private JLabel     lblStatus;

    private final SiswaCSV siswaCSV = new SiswaCSV("siswa.csv");

    // ===================== KONSTRUKTOR =====================

    public AplikasiSiswa() {
        setTitle("Aplikasi Data Siswa Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 570);
        setLocationRelativeTo(null);
        setResizable(false);

        initKomponen();
        muatDataAwal();
    }

    // ===================== INISIALISASI =====================

    private void initKomponen() {
        Color warnaPrimer = new Color(30, 90, 160);
        Color warnaPanel  = new Color(245, 247, 250);

        JPanel panelUtama = new JPanel(new BorderLayout(0, 0));
        panelUtama.setBackground(warnaPanel);
        setContentPane(panelUtama);

        // ---- Judul ----
        JPanel panelJudul = new JPanel();
        panelJudul.setBackground(warnaPrimer);
        panelJudul.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lblJudul = new JLabel("Manajemen Data Siswa Perpustakaan");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        panelJudul.add(lblJudul);
        panelUtama.add(panelJudul, BorderLayout.NORTH);

        // ---- Panel tengah ----
        JPanel panelTengah = new JPanel(new BorderLayout(0, 8));
        panelTengah.setBackground(warnaPanel);
        panelTengah.setBorder(new EmptyBorder(12, 14, 8, 14));

        // Kartu putih untuk FormPanel
        JPanel kartuForm = new JPanel(new BorderLayout());
        kartuForm.setBackground(Color.WHITE);
        kartuForm.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(210, 215, 225), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        formPanel = new FormPanel();
        kartuForm.add(formPanel, BorderLayout.CENTER);

        tabelPanel = new TabelPanel();

        panelTengah.add(kartuForm,   BorderLayout.NORTH);
        panelTengah.add(tabelPanel,  BorderLayout.CENTER);
        panelUtama.add(panelTengah,  BorderLayout.CENTER);

        // ---- Status bar ----
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        panelStatus.setBackground(new Color(230, 235, 245));
        panelStatus.setBorder(new MatteBorder(1, 0, 0, 0, new Color(200, 205, 215)));
        lblStatus = new JLabel("Siap.");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(60, 60, 80));
        panelStatus.add(lblStatus);
        panelUtama.add(panelStatus, BorderLayout.SOUTH);

        // ===================== SAMBUNGKAN LISTENER =====================

        // Saat baris tabel diklik -> isi form
        tabelPanel.setTabelListener((nis, nama, alamat) -> {
            formPanel.setNIS(nis);
            formPanel.setNama(nama);
            formPanel.setAlamat(alamat);
        });

        // Aksi dari tombol form
        formPanel.setFormListener(new FormPanel.FormListener() {

            @Override
            public void onTambah(String nis, String nama, String alamat) {
                aksiTambah(nis, nama, alamat);
            }

            @Override
            public void onUpdate(String nis, String nama, String alamat) {
                aksiUpdate(nis, nama, alamat);
            }

            @Override
            public void onHapus(String nis) {
                aksiHapus(nis);
            }

            @Override
            public void onBersihkan() {
                tabelPanel.hapusSeleksi();
                setStatus("Form dibersihkan.");
            }
        });
    }

    // ===================== MUAT DATA AWAL =====================

    /**
     * Muat semua data dari siswa.csv ke tabel saat program pertama kali dibuka.
     */
    private void muatDataAwal() {
        try {
            List<Siswa> daftar = siswaCSV.muatSemua();
            tabelPanel.muatData(daftar);
            setStatus("Berhasil memuat " + daftar.size() + " data siswa dari siswa.csv.");
        } catch (IOException ex) {
            tampilError("Gagal membaca file siswa.csv:\n" + ex.getMessage());
            setStatus("Gagal memuat data.");
        }
    }

    // ===================== LOGIKA CRUD =====================

    /**
     * CREATE: Tambah data siswa baru.
     */
    private void aksiTambah(String nis, String nama, String alamat) {
        // Validasi input kosong
        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            tampilPeringatan("Semua field (NIS, Nama, Alamat) harus diisi.");
            return;
        }

        // Exception handling: NIS duplikat
        try {
            if (tabelPanel.nisAda(nis)) {
                throw new NisDuplikatException("NIS " + nis + " sudah terdaftar. Gunakan NIS yang berbeda.");
            }
        } catch (NisDuplikatException ex) {
            tampilError(ex.getMessage());
            return;
        }

        Siswa siswa = new Siswa(nis, nama, alamat);

        // Simpan ke file CSV
        try {
            siswaCSV.tambah(siswa);
        } catch (IOException ex) {
            tampilError("Gagal menyimpan ke file:\n" + ex.getMessage());
            return;
        }

        tabelPanel.tambahBaris(siswa);
        setStatus("Data siswa '" + nama + "' (NIS: " + nis + ") berhasil ditambahkan.");
        formPanel.bersihkanForm();
    }

    /**
     * UPDATE: Perbarui data siswa yang sudah ada.
     */
    private void aksiUpdate(String nis, String nama, String alamat) {
        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            tampilPeringatan("Semua field harus diisi untuk melakukan update.");
            return;
        }

        boolean berhasil = tabelPanel.updateBaris(nis, nama, alamat);
        if (!berhasil) {
            tampilPeringatan("NIS " + nis + " tidak ditemukan di daftar.");
            return;
        }

        // Tulis ulang file CSV
        try {
            siswaCSV.simpanSemua(tabelPanel.semuaData());
        } catch (IOException ex) {
            tampilError("Gagal menyimpan perubahan ke file:\n" + ex.getMessage());
            return;
        }

        setStatus("Data siswa dengan NIS " + nis + " berhasil diupdate.");
        formPanel.bersihkanForm();
        tabelPanel.hapusSeleksi();
    }

    /**
     * DELETE: Hapus data siswa berdasarkan NIS.
     */
    private void aksiHapus(String nis) {
        if (nis.isEmpty()) {
            tampilPeringatan("Masukkan atau pilih NIS yang ingin dihapus.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data siswa dengan NIS " + nis + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirmasi != JOptionPane.YES_OPTION) return;

        boolean berhasil = tabelPanel.hapusBaris(nis);
        if (!berhasil) {
            tampilPeringatan("NIS " + nis + " tidak ditemukan di daftar.");
            return;
        }

        // Tulis ulang file CSV
        try {
            siswaCSV.simpanSemua(tabelPanel.semuaData());
        } catch (IOException ex) {
            tampilError("Gagal menyimpan perubahan ke file:\n" + ex.getMessage());
            return;
        }

        setStatus("Data siswa dengan NIS " + nis + " berhasil dihapus.");
        formPanel.bersihkanForm();
        tabelPanel.hapusSeleksi();
    }

    // ===================== HELPER =====================

    private void setStatus(String pesan) {
        lblStatus.setText(pesan);
    }

    private void tampilPeringatan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private void tampilError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ===================== MAIN =====================

    public static void main(String[] args) {
        try {
            // Pakai Cross Platform (Java Metal) LAF agar warna tombol tidak di-override sistem
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Gunakan default Look and Feel jika gagal
        }

        SwingUtilities.invokeLater(() -> {
            AplikasiSiswa app = new AplikasiSiswa();
            app.setVisible(true);
        });
    }
}
