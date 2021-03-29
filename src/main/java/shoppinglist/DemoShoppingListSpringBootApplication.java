package shoppinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaRepo;
import shoppinglist.service.ShoppingListService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class DemoShoppingListSpringBootApplication implements CommandLineRunner
{
    @Autowired
    private DaftarBelanjaRepo repo;
    private ShoppingListService service;

    public static void main(String[] args)
    {
        SpringApplication.run(DemoShoppingListSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("Membaca Semua Record DaftarBelanja:");
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            System.out.println("[" + db.getId() + "] " + db.getJudul());

            List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : listBarang) {
                System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        }
        
        Scanner keyb = new Scanner(System.in);
        
        // Baca berdasarkan ID
        System.out.print("Masukkan ID dari objek DaftarBelanja yg ingin ditampilkan: ");
        long id = Long.parseLong(keyb.nextLine());
        System.out.println("Hasil pencarian: ");
        
        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("\tJudul: " + db.getJudul());
        }
        else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }
    }

    //Lokal
    //Mencari Daftar Belanja berdasarkan Judul
    public void CariDaftarBarangBerdasarkanJudul()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Masukkan Judul : ");
        String Judul = scan.nextLine();
        System.out.println("Hasil Pencarian Judul : ");
        DaftarBelanja tempDb = null;
        List<DaftarBelanja> dbAll = repo.findAll();
        for (DaftarBelanja db : dbAll) {
            if(db.getJudul().equals(Judul)){
                tempDb = db;
            }
        }
        if(tempDb != null){
            System.out.println("Judul : " + tempDb.getJudul());
            List<DaftarBelanjaDetil> listDbd = tempDb.getDaftarBarang();
            System.out.println("Detail Daftar Belanja :");
            for (DaftarBelanjaDetil dbd : listDbd) {
                System.out.println("\t Nama Barang : " + dbd.getNamaBarang() + "\n\t Jumlah : " +
                        dbd.getByk() + "\n\t Satuan : " + dbd.getSatuan());
            }
        } else {
            System.out.println("Data tidak ditemukan!");
        }
    }

    //Insert Daftar Belanja
    public void InsertDaftarBelanja()
    {
        LocalDateTime tanggal = LocalDateTime.now().withNano(0);
        Scanner scan = new Scanner(System.in);
        System.out.println("Menambahkan DaftarBelanja");
        System.out.print("Masukkan Judul : ");
        String Judul = scan.nextLine();
        DaftarBelanja db = new DaftarBelanja();

        db.setJudul(Judul);
        db.setTanggal(tanggal);
        repo.save(db);
        System.out.println("Data Berhasil Ditambahkan!");
    }

    //Update Daftar Belanja
    public void UpdateDaftarBelanja()
    {
        LocalDateTime tanggal = LocalDateTime.now().withNano(0);
        Scanner scan = new Scanner(System.in);
        System.out.println("Update Daftar Belanja");
        System.out.print("Masukkan ID Daftar Belanja : ");
        String tempId = scan.nextLine();
        long id = Long.parseLong(tempId);
        System.out.println("Hasil pencarian : ");

        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("Judul : " + db.getJudul());
            List<DaftarBelanjaDetil> listDbd = db.getDaftarBarang();
            System.out.println("Detail Daftar Belanja : ");
            for (DaftarBelanjaDetil dbd : listDbd) {
                System.out.println("\t Nama Barang : " + dbd.getNamaBarang() + "\n\t Jumlah : " +
                        dbd.getByk() + "\n\t Satuan : " + dbd.getSatuan());
            }
            System.out.println("Data Untuk Update");
            System.out.print("Masukan Judul : ");
            String judul = scan.nextLine();
            db.setJudul(judul);
            db.setTanggal(tanggal);
            repo.save(db);
            System.out.println("Berhasil Terupdate!");
        } else {
            System.out.println("\tData tidak ditemukan!");
        }
    }

    //Delete Daftar Belanja
    public void DeleteDaftarBelanja()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hapus Daftar Belanja");
        System.out.print("Masukkan ID : ");
        String tempId = scan.nextLine();
        long id = Long.parseLong(tempId);

        Optional<DaftarBelanja> optDB = repo.findById(id);
        System.out.println("Data yang akan terhapus");
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("Judul : " + db.getJudul());
            List<DaftarBelanjaDetil> listDbd = db.getDaftarBarang();
            System.out.println("DetailDaftarBelanja : ");
            for (DaftarBelanjaDetil dbd : listDbd) {
                System.out.println("\t Nama Barang : " + dbd.getNamaBarang() + "\n\t Jumlah : " +
                        dbd.getByk() + "\n\t Satuan : " + dbd.getSatuan());
            }
            System.out.println("Ingin menghapus?(yes/no)");
            String del = scan.nextLine();
            if(del.equals("yes")){
                repo.deleteById(id);
                System.out.println("Penghapusan Berhasil");
            }
        } else {
            System.out.println("\tData tidak ditemukan!");
        }

    }
}
