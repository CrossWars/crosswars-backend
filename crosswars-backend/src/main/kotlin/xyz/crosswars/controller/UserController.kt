package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crosswars.config.Authorized
import xyz.crosswars.entities.User
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.service.UserService

@RestController
@RequestMapping("/users")
@Api(tags = ["Users"])
class UserController(
    var service: UserService
) {

    @PostMapping("/telegram")
    @ResponseStatus(HttpStatus.CREATED)
    @Authorized(googleIdToken = false)
    fun createTelegramUser(@RequestBody user: User): User {
        return service.createTelegramUser(user)
    }

    @PostMapping("/website")
    @ResponseStatus(HttpStatus.CREATED)
    @Authorized
    fun createUser(@RequestBody user: User): User {
        return service.createWebsiteUser(user)
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @Authorized
    fun getUserById(
        @RequestParam("user_id", required = false) id: String?,
        @RequestAttribute("auth_user") user: User?
    ): ResponseEntity<User> {
        if (user != null) {
            return ResponseEntity.ok(user)
        } else {
            id?.let {
                return ResponseEntity.ok(service.findUserById(id))
            } ?: throw BadRequestException("User id query param is required if using auth token")
        }
    }

    @GetMapping("/emails")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @Authorized
    fun getUserByEmail(
        @RequestParam("user_email", required = false) email: String?,
        @RequestAttribute("auth_user") user: User?
    ): ResponseEntity<User> {
        if (user != null) {
            return ResponseEntity.ok(user)
        } else {
            email?.let {
                return ResponseEntity.ok(service.findUserByEmail(email))
            } ?: throw BadRequestException("user_email query param is required if using auth token")
        }
    }

    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUserByName(
        @RequestParam("user_name", required = false) name: String?,
        @RequestAttribute("auth_user") user: User?
    ): ResponseEntity<User> {
        if (user != null) {
            return ResponseEntity.ok(user)
        } else {
            name?.let {
                return ResponseEntity.ok(service.findUserByName(name))
            } ?: throw BadRequestException("user_name query param is required if using auth token")
        }
    }
}