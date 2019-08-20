package jabito.backend.demo.data

import com.fasterxml.jackson.annotation.JsonProperty

class JsonAppUserRegister {

    @JsonProperty("firstName")
    var firstName: String = ""
    @JsonProperty("lastName")
    var lastName: String = ""
    @JsonProperty("username")
    var username: String = ""
    @JsonProperty("email")
    var email: String = ""
    @JsonProperty("roleId")
    var roleId: Int = -1
    @JsonProperty("appUsername")
    var appUsername: String = ""
}
