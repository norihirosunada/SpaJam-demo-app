package com.norihiro.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodDao {
    // データを追加
    @Insert
    fun insert(Food: Foods)

    // データを更新
    @Update
    fun update(Food: Foods)

    // データを削除
    @Delete
    fun delete(Food: Foods)

    // 全てのデータを取得
    @Query("select * from Foods")
    fun getAll(): List<Foods>

    // 全てのデータを削除
    @Query("delete from Foods")
    fun deleteAll()

    // FoodのuidがidのFoodを取得
    @Query("select * from Foods where id = :id")
    fun getFood(id: Int): Foods
}