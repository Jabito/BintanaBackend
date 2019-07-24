package jabito.backend.demo.data

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class Global {
    companion object {
        val CURRENT_TIMESTAMP: DateTime? = DateTime.now(DateTimeZone.getDefault())
        const val ORIGIN1: String = "null"
        const val ORIGIN2: String = "http://localhost:3000"
        const val ORIGIN3: String = "null"
    }
}
