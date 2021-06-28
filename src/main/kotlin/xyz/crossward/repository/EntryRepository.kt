package xyz.crossward.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crossward.entities.Entry
import java.time.LocalDate

@Repository
interface EntryRepository : CrudRepository<Entry, String> {

    @Query("select count(e) from Entry e where e.userId = :userId and e.date = :date")
    fun count(userId: String, date: LocalDate?): Long
}