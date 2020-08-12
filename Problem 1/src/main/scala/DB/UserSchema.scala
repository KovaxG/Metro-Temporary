package DB

import Types.User
import org.squeryl.{Schema, Table}
import org.squeryl.PrimitiveTypeMode._

object UserSchema extends Schema {
  val users: Table[User] = table[User]
}