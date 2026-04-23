import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * FormPanel.java
 * Panel yang berisi form entri data siswa (NIS, Nama, Alamat)
 * dan tombol-tombol operasi CRUD.
 *
 * Komunikasi ke luar dilakukan melalui interface FormListener
 * agar panel ini tidak bergantung langsung ke JFrame utama.
 */
public class FormPanel extends JPanel {

    // ===================== KOMPONEN =====================

    private JTextField txtNIS, txtNama, txtAlamat;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;

    /**
     * Interface listener untuk meneruskan aksi tombol ke luar panel.
     */
    public interface FormListener {
        void onTambah(String nis, String nama, String alamat);
        void onUpdate(String nis, String nama, String alamat);
        void onHapus(String nis);
        void onBersihkan();
    }

    private FormListener listener;

    // ===================== KONSTRUKTOR =====================

    public FormPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(12, 16, 4, 16));
        initKomponen();
    }

    // ===================== INISIALISASI =====================

    private void initKomponen() {
        Color warnaPrimer = new Color(30, 90, 160);
        Color warnaHapus  = new Color(200, 50, 50);
        Color warnaUpdate = new Color(40, 140, 80);
        Color warnaBersih = new Color(120, 120, 120);

        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 13);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 13);

        // ---- Panel field ----
        JPanel panelField = new JPanel(new GridBagLayout());
        panelField.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // NIS
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lNIS = buatLabel("NIS", fontLabel);
        panelField.add(lNIS, gbc);
        gbc.gridx = 1; panelField.add(new JLabel(":"), gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNIS = buatTextField(fontField);
        panelField.add(txtNIS, gbc);

        // Nama
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelField.add(buatLabel("Nama Siswa", fontLabel), gbc);
        gbc.gridx = 1; panelField.add(new JLabel(":"), gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNama = buatTextField(fontField);
        panelField.add(txtNama, gbc);

        // Alamat
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelField.add(buatLabel("Alamat", fontLabel), gbc);
        gbc.gridx = 1; panelField.add(new JLabel(":"), gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtAlamat = buatTextField(fontField);
        panelField.add(txtAlamat, gbc);

        // ---- Panel tombol ----
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelTombol.setBackground(Color.WHITE);

        btnTambah    = buatTombol("Tambah",    warnaPrimer);
        btnUpdate    = buatTombol("Update",    warnaUpdate);
        btnHapus     = buatTombol("Hapus",     warnaHapus);
        btnBersihkan = buatTombol("Bersihkan", warnaBersih);

        panelTombol.add(btnTambah);
        panelTombol.add(btnUpdate);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBersihkan);

        add(panelField,  BorderLayout.CENTER);
        add(panelTombol, BorderLayout.SOUTH);

        // ===================== EVENT HANDLER =====================

        btnTambah.addActionListener(e -> {
            if (listener != null)
                listener.onTambah(getNIS(), getNama(), getAlamat());
        });

        btnUpdate.addActionListener(e -> {
            if (listener != null)
                listener.onUpdate(getNIS(), getNama(), getAlamat());
        });

        btnHapus.addActionListener(e -> {
            if (listener != null)
                listener.onHapus(getNIS());
        });

        btnBersihkan.addActionListener(e -> {
            bersihkanForm();
            if (listener != null)
                listener.onBersihkan();
        });
    }

    // ===================== GETTER VALUE FIELD =====================

    public String getNIS()    { return txtNIS.getText().trim(); }
    public String getNama()   { return txtNama.getText().trim(); }
    public String getAlamat() { return txtAlamat.getText().trim(); }

    // ===================== SETTER (dipakai saat klik tabel) =====================

    public void setNIS(String v)    { txtNIS.setText(v); }
    public void setNama(String v)   { txtNama.setText(v); }
    public void setAlamat(String v) { txtAlamat.setText(v); }

    public void bersihkanForm() {
        txtNIS.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        txtNIS.requestFocus();
    }

    // ===================== SET LISTENER =====================

    public void setFormListener(FormListener listener) {
        this.listener = listener;
    }

    // ===================== HELPER KOMPONEN =====================

    private JLabel buatLabel(String teks, Font f) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(f);
        return lbl;
    }

    private JTextField buatTextField(Font f) {
        JTextField tf = new JTextField(20);
        tf.setFont(f);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(190, 195, 210), 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(7, 18, 7, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }
}
