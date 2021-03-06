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

}
