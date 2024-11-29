package xyz.crosswars.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.User
import java.util.stream.Stream

@Repository
interface UserRepository : CrudRepository<User, String> {

    @Query("select u from User u where u.name = :name")
    fun findUserByName(name: String): Stream<User>

    @Query("select u from User u where u.email = :email")
    fun findUserByEmail(email: String): Stream<User>

    @Query("select u from User u where u.remind = :remind")
    fun findUsersWithEmailReminders(remind: Boolean): Stream<User>
}