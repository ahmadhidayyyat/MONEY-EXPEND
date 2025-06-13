# Money-Expense

Money-Expense adalah aplikasi desktop yang dirancang untuk membantu pengguna melacak pengeluaran keuangan pribadi mereka. Dibangun dengan JavaFX, aplikasi ini menyediakan antarmuka yang bersih dan intuitif untuk manajemen keuangan sehari-hari.

## 🌟 Fitur Utama

- **Autentikasi Pengguna**: Sistem registrasi dan login yang aman untuk menjaga privasi data pengguna.
- **Keamanan Password**: Password pengguna di-hash menggunakan jBCrypt sebelum disimpan ke database untuk keamanan maksimal.
- **CRUD Pengeluaran**: Pengguna dapat dengan mudah Menambah, Melihat, Memperbarui, dan Menghapus (CRUD) catatan pengeluaran mereka.
- **Dashboard Utama**: Setelah login, pengguna disambut dengan dashboard yang menampilkan daftar semua pengeluaran dan ringkasan total untuk bulan berjalan.
- **Penyimpanan Data Lokal**: Semua data pengguna dan pengeluaran disimpan secara lokal dalam database SQLite.
- **Antarmuka Adaptif**: Ukuran jendela aplikasi akan dipertahankan saat navigasi antar halaman untuk pengalaman pengguna yang lebih baik.

## 📸 Tampilan Aplikasi

*(Catatan: Ganti path/to/your/screenshot.png dengan path sebenarnya dari gambar Anda setelah Anda mengambil screenshot.)*

<p align="center">
<strong>Tampilan Login & Dashboard</strong><br><br>
<img src="path/to/your/login_view_screenshot.png" width="45%" alt="Tampilan Login">
&nbsp; &nbsp;
<img src="path/to/your/main_view_screenshot.png" width="45%" alt="Tampilan Dashboard Utama">
</p>

## 💻 Teknologi yang Digunakan

- **Bahasa Pemrograman**: Java
- **Framework UI**: JavaFX
- **Database**: SQLite
- **Build Tool**: Gradle
- **Library**:
  - jBCrypt untuk hashing password
  - SQLite-JDBC untuk koneksi database

## 🚀 Cara Menjalankan Aplikasi

Bagian ini akan memandu Anda untuk mengunduh, mengonfigurasi, dan menjalankan aplikasi di komputer Anda.

### Prasyarat:

- Java Development Kit (JDK) versi 11 atau lebih tinggi.
- Gradle terinstal dan terkonfigurasi dengan benar di sistem Anda.

### Langkah-langkah:

1. **Clone Repositori**
   
   Buka terminal atau command prompt Anda dan jalankan perintah berikut untuk mengunduh proyek:
   ```bash
   git clone https://github.com/ahmadhidayyyat/money-expend.git
   ```
   
   Masuk ke direktori proyek yang baru saja dibuat:
   ```bash
   cd money-expend
   ```

2. **Build dan Jalankan dengan Gradle**
   
   Proyek ini menggunakan Gradle untuk manajemen dependensi dan proses build. Jalankan aplikasi dengan perintah berikut dari direktori root proyek:
   ```bash
   gradle run
   ```

3. **Penggunaan Pertama Kali**
   
   Saat aplikasi dijalankan untuk pertama kalinya, sebuah direktori bernama `.moneyexpense` akan otomatis dibuat di folder home Anda (`C:\Users\NAMA_ANDA\.moneyexpense` di Windows atau `~/.moneyexpense` di macOS/Linux).
   
   Di dalam direktori tersebut, file database `database.db` akan dibuat dan diinisialisasi secara otomatis.

4. **Mulai Menggunakan Aplikasi**
   
   - Anda akan disambut oleh halaman login.
   - Jika Anda belum memiliki akun, klik tombol untuk menuju halaman registrasi, buat akun baru, dan Anda akan diarahkan kembali ke halaman login.
   - Masuk dengan akun yang telah Anda buat untuk mulai melacak dan mengelola pengeluaran Anda.

## 🏗️ Alur Kerja & Arsitektur Proyek

Proyek ini mengadopsi arsitektur berlapis (layered architecture) untuk memisahkan antara logika antarmuka (UI), logika bisnis, dan logika akses data.

- **Presentation Layer (Controller & FXML)**
  
  Bertanggung jawab untuk menampilkan antarmuka pengguna (UI) dan menangani input dari pengguna.

- **Service Layer (Services)**
  
  Berisi logika bisnis utama aplikasi dan bertindak sebagai jembatan antara Controller dan DAO.
  Contoh: `ManajemenAkun.java`, `AuthService.java`, `NavigatorService.java`.

- **Data Access Layer (DAO)**
  
  Bertanggung jawab untuk semua operasi CRUD ke database.

- **Data Model (Models)**
  
  Merepresentasikan entitas data dalam aplikasi.

## 🗄️ Skema Database (SQLite)

Aplikasi ini menggunakan dua tabel utama yang dibuat secara otomatis.

### Tabel pengguna
```sql
CREATE TABLE IF NOT EXISTS pengguna (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);
```

### Tabel pengeluaran
```sql
CREATE TABLE IF NOT EXISTS pengeluaran (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_pengguna INTEGER NOT NULL,
    tanggal TEXT NOT NULL,
    jumlah REAL NOT NULL,
    keterangan TEXT,
    FOREIGN KEY (id_pengguna) REFERENCES pengguna(id) ON DELETE CASCADE
);
```
