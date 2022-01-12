package com.example.addverb.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface countriesDao {

    @Query("Select * from countries")
     List<entity> getallcountries();
    @Query("SELECT EXISTS (SELECT 1 FROM countries WHERE id = :id)")
    Boolean exists(int id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserCountry(entity entity);
    @Query("DELETE FROM countries")
    void deleteAll();


}
