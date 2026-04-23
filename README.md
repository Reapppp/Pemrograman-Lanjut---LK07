# Aplikasi Data Siswa Perpustakaan

Aplikasi desktop berbasis Java Swing untuk mengelola data siswa di perpustakaan sekolah. Dibuat sebagai bagian dari Latihan Kerja (LK07) mata kuliah Pemrograman Berorientasi Objek.

---

## Deskripsi

Program ini memungkinkan pengguna untuk melakukan pengelolaan data siswa secara lengkap, mulai dari menambah, melihat, mengubah, hingga menghapus data. Semua data disimpan secara permanen di file `siswa.csv` sehingga tidak hilang saat program ditutup.

Data yang dikelola terdiri dari tiga informasi utama: **NIS** (Nomor Induk Sekolah), **Nama Siswa**, dan **Alamat**.

---

## Fitur

- **Tambah Data** — Memasukkan data siswa baru. NIS bersifat unik, tidak boleh ada yang sama.
- **Lihat Data** — Semua data ditampilkan otomatis dalam bentuk tabel saat program dibuka.
- **Update Data** — Mengubah nama atau alamat siswa berdasarkan NIS yang dipilih.
- **Hapus Data** — Menghapus data siswa dengan konfirmasi terlebih dahulu.
- **Klik baris tabel** — Data langsung otomatis terisi ke form untuk mempermudah update atau hapus.
- **Penyimpanan otomatis** — Setiap perubahan langsung tersimpan ke file `siswa.csv`.

---

## Struktur Program

Program dibagi menjadi beberapa file sesuai tanggung jawabnya masing-masing:

| File | Peran |
|---|---|
| `Siswa.java` | Representasi data satu siswa (NIS, Nama, Alamat) |
| `NisDuplikatException.java` | Exception khusus jika NIS yang dimasukkan sudah ada |
| `SiswaCSV.java` | Menangani semua operasi baca dan tulis ke file CSV |
| `FormPanel.java` | Tampilan form input dan tombol-tombol CRUD |
| `TabelPanel.java` | Tampilan tabel daftar siswa |
| `AplikasiSiswa.java` | Jendela utama program sekaligus titik awal (`main`) |

---

## Cara Menjalankan

Pastikan Java sudah terinstal di komputer (minimal JDK 8).

```bash
# 1. Kompilasi semua file sekaligus
javac *.java

# 2. Jalankan program
java AplikasiSiswa
```

File `siswa.csv` akan dibuat otomatis di folder yang sama saat program pertama kali dijalankan.

---

## Teknologi yang Digunakan

- **Java SE** — Bahasa pemrograman utama
- **Java Swing** — Library untuk tampilan antarmuka grafis (GUI)
- **File I/O (BufferedReader / BufferedWriter)** — Untuk membaca dan menulis data ke file CSV

---

## Penanganan Error

Program sudah dilengkapi dengan beberapa penanganan error, di antaranya:

- Jika NIS yang dimasukkan sudah ada, program akan menampilkan pesan error dan membatalkan proses penambahan.
- Jika file `siswa.csv` tidak ditemukan saat program dibuka, file baru akan dibuat secara otomatis.
- Jika terjadi kegagalan baca/tulis file, program menampilkan pesan error tanpa langsung crash.
