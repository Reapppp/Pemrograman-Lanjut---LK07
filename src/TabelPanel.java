import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * TabelPanel.java
 * Panel yang menampilkan daftar data siswa dalam bentuk tabel.
 *
 * Menyediakan listener agar frame utama bisa tahu
 * baris mana yang sedang dipilih user (untuk mengisi form).
 */
public class TabelPanel extends JPanel {

    // ===================== KOMPONEN =====================

    private JTable tabel;
    private DefaultTableModel modelTabel;

    /**
     * Interface listener: dipanggil saat user mengklik baris tabel.
     */
    public interface TabelListener {
        void onBarisDipilih(String nis, String nama, String alamat);
    }

    private TabelListener listener;

    // ===================== KONSTRUKTOR =====================

    public TabelPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        initKomponen();
    }

    // ===================== INISIALISASI =====================

    private void initKomponen() {
        Color warnaPrimer = new Color(30, 90, 160);

        String[] header = {"NIS", "Nama Siswa", "Alamat"};
        modelTabel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setRowHeight(26);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabel.getTableHeader().setBackground(warnaPrimer);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(200, 220, 255));
        tabel.setGridColor(new Color(225, 228, 235));
        tabel.setShowGrid(true);

        // Klik baris -> panggil listener
        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int baris = tabel.getSelectedRow();
                if (baris >= 0 && listener != null) {
                    listener.onBarisDipilih(
                        modelTabel.getValueAt(baris, 0).toString(),
                        modelTabel.getValueAt(baris, 1).toString(),
                        modelTabel.getValueAt(baris, 2).toString()
                    );
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(210, 215, 225), 1, true));
        add(scroll, BorderLayout.CENTER);
    }

    // ===================== OPERASI TABEL =====================

    /**
     * Tambah satu baris ke tabel.
     */
    public void tambahBaris(Siswa siswa) {
        modelTabel.addRow(new Object[]{siswa.getNis(), siswa.getNama(), siswa.getAlamat()});
    }

    /**
     * Muat ulang seluruh tabel dari list siswa.
     */
    public void muatData(List<Siswa> daftarSiswa) {
        modelTabel.setRowCount(0);
        for (Siswa s : daftarSiswa) {
            modelTabel.addRow(new Object[]{s.getNis(), s.getNama(), s.getAlamat()});
        }
    }

    /**
     * Update satu baris berdasarkan NIS.
     * @return true jika ditemukan dan diupdate
     */
    public boolean updateBaris(String nis, String nama, String alamat) {
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            if (modelTabel.getValueAt(i, 0).toString().equals(nis)) {
                modelTabel.setValueAt(nama,   i, 1);
                modelTabel.setValueAt(alamat, i, 2);
                return true;
            }
        }
        return false;
    }

    /**
     * Hapus satu baris berdasarkan NIS.
     * @return true jika ditemukan dan dihapus
     */
    public boolean hapusBaris(String nis) {
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            if (modelTabel.getValueAt(i, 0).toString().equals(nis)) {
                modelTabel.removeRow(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Cek apakah NIS sudah ada di tabel.
     */
    public boolean nisAda(String nis) {
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            if (modelTabel.getValueAt(i, 0).toString().equalsIgnoreCase(nis))
                return true;
        }
        return false;
    }

    /**
     * Kembalikan semua data tabel sebagai List<Siswa>.
     */
    public java.util.List<Siswa> semuaData() {
        java.util.List<Siswa> list = new java.util.ArrayList<>();
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            list.add(new Siswa(
                modelTabel.getValueAt(i, 0).toString(),
                modelTabel.getValueAt(i, 1).toString(),
                modelTabel.getValueAt(i, 2).toString()
            ));
        }
        return list;
    }

    public void hapusSeleksi() {
        tabel.clearSelection();
    }

    // ===================== SET LISTENER =====================

    public void setTabelListener(TabelListener listener) {
        this.listener = listener;
    }
}
