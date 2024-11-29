package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.entities.User
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.exception.NoContentException
import xyz.crosswars.repository.UserRepository
import xyz.crosswars.util.unwrap
import javax.sql.DataSource

@Service
class UserService(
    var dataSource: DataSource,
    val userRepository: UserRepository
) {

    // Example of executing a statement directly with the JDBC driver
    fun select(): String {
        val sql = "SELECT id, name FROM user where id = 1"
        val stmt = dataSource.connection.createStatement()

        val result = stmt.executeQuery(sql)
        while (result.next()) {
            val id = result.getInt(1)
            val name = result.getString(2)

            return "Retrieved $name from database with id $id"
        }

        return "nothing found"
    }

    fun findUserById(id: String): User =
        userRepository.findById(id).unwrap()
            ?: throw NoContentException("Could not find user with id $id")

    fun findUserByEmail(email: String): User =
        userRepository.findUserByEmail(email.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find user with email $email")

    fun findUserByName(name: String): User =
        userRepository.findUserByName(name.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find user with name $name")

    fun findUsersByEmailReminder(remind: Boolean): List<User> =
        userRepository.findUsersWithEmailReminders(remind).toList()

    /**
     * Creates a telegram user (email is optional)
     *
     * @param user user to create
     * @return user saved to the database
     */
    fun createTelegramUser(user: User): User {
        if (userRepository.existsById(user.userId)) {
            throw BadRequestException("A user with ID ${user.userId} already exists")
        }
        // create a telegram user
        val savedUser = User(
            userId = user.userId,
            name = user.name.lowercase(),
            email = user.email?.lowercase(),
            remind = user.remind ?: false
        )
        userRepository.save(savedUser)
        return savedUser
    }

    /**
     * Creates a website user (ID is assumed to be the email)
     *
     * @param user user to create
     * @return user saved to the database
     */
    fun createWebsiteUser(user: User): User {
        if (userRepository.existsById(user.userId)) {
            throw BadRequestException("A user with ID ${user.userId} already exists")
        }
        val savedUser = User(
            userId = user.userId.lowercase(),
            name = user.name,
            email = user.email!!.lowercase(),
            remind = user.remind ?: false
        )
        userRepository.save(savedUser)
        return savedUser
    }

    fun userExists(userId: String): Boolean =
        userRepository.existsById(userId)
}
