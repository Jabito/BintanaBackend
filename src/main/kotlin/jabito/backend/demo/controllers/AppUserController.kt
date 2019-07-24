package jabito.backend.demo.controllers

import jabito.backend.demo.data.Global
import jabito.backend.demo.data.JsonAppUserRegister
import jabito.backend.demo.data.LoginJson
import jabito.backend.demo.services.AppUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/appUsers")
@CrossOrigin(origins= arrayOf(Global.ORIGIN1, Global.ORIGIN2, Global.ORIGIN3))
class AppUserController(@Autowired private val appUserService: AppUserService){

    @PostMapping("/createAdminUser")
    fun createAdminUser(): ResponseEntity<Any> = appUserService.createUserAdmin()

    @PostMapping("/register")
    fun register(@RequestBody @Valid registerParams: JsonAppUserRegister) : ResponseEntity<Any> =
            ResponseEntity(appUserService.registerAppUser(registerParams), HttpStatus.OK)

    @GetMapping("/getUser")
    fun getUser(@RequestParam("appUserId") appUserId: Int) : ResponseEntity<Any> =
            ResponseEntity(appUserService.getAppUser(appUserId), HttpStatus.OK)

    @PostMapping("/passwordResetRequest")
    fun passwordResetRequest(@RequestParam("email") email: String){

    }

    @PostMapping("/login")
    fun login(@RequestBody loginJson: LoginJson): ResponseEntity<Any> = ResponseEntity(appUserService.login(loginJson.username ,loginJson.password), HttpStatus.OK)
}
