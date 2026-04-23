/**
 * NisDuplikatException.java
 * Exception kustom yang dilempar ketika NIS yang dimasukkan
 * sudah terdaftar di daftar siswa.
 *
 * Digunakan dalam operasi CREATE agar penanganan error lebih spesifik
 * dan mudah dibedakan dari exception lainnya.
 */
public class NisDuplikatException extends Exception {

    public NisDuplikatException(String pesan) {
        super(pesan);
    }
}
