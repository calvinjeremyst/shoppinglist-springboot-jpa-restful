/*
 * ShoppingListService.java
 *
 * Created on Mar 22, 2021, 01.20
 */
package shoppinglist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author iON
 */
@Service
public class ShoppingListService
{
    @Autowired
    private DaftarBelanjaRepo repo;

    public Iterable<DaftarBelanja> getAllData()
    {
        return repo.findAll();
    }

    public boolean create(DaftarBelanja entity, DaftarBelanjaDetil[] arrDetil)
    {
        try {
            // Pertama simpan dahulu objek DaftarBelanja tanpa mengandung detil apapun.
            repo.save(entity);

            // Setelah berhasil tersimpan, baca primary key auto-generate lalu set sebagai bagian dari
            // ID composite di DaftarBelanjaDetil.
            int noUrut = 1;
            for (DaftarBelanjaDetil detil : arrDetil) {
                detil.setId(entity.getId(), noUrut++);
                entity.addDaftarBarang(detil);
            }
            repo.save(entity);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }
    //Mencari Daftar Belanja berdasarkan Judul
    @GetMapping("/shoppinglist")
    public ResponseEntity<List<DaftarBelanja>> findByJudul(@RequestParam String title){
        try{
            List<DaftarBelanja> db = new ArrayList<DaftarBelanja>();
            repo.findByTitle(title).forEach(db::add);
            if (db.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Insert Daftar Belanja
    @PostMapping("shoppinglist")
    public ResponseEntity<DaftarBelanja> insertDaftarBelanja(@RequestBody DaftarBelanja db){
        try{
            DaftarBelanja db2 = repo.save(new DaftarBelanja(db.getJudul(),db.getTanggal(),db.getDaftarBarang()));
            //DaftarBelanjaDetil dbd2 = repo.save(new DaftarBelanjaDetil(dbd.getNoUrut(),dbd.getNamaBarang(),dbd.getByk(),dbd.getSatuan(),dbd.getMemo()));
            return new ResponseEntity<>(db2, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Daftar Belanja
    @PutMapping("shoppinglist/{id}")
    public ResponseEntity<DaftarBelanja> updateDaftarBelanja(@PathVariable("id") long id,
                                                             @RequestBody DaftarBelanja db) {
        Optional<DaftarBelanja> dbData = repo.findById(id);
        if(dbData.isPresent()) {
            DaftarBelanja db2 = dbData.get();
            db2.setJudul(db.getJudul());
            db.setTanggal(db.getTanggal());
            return new ResponseEntity<>(repo.save(db2),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete Daftar Belanja
    @DeleteMapping("shoppinglist/{id}")
    public ResponseEntity<HttpStatus> deleteDaftarBelanja(@PathVariable("id") long id){
        try{
            repo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
