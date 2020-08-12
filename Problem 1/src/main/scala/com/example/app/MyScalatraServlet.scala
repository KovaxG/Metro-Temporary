package com.example.app

import DB.DBSessionSupport
import DB.UserSchema
import Types.User
import org.scalatra._
import org.squeryl.PrimitiveTypeMode._
import io.circe.syntax._
import io.circe.generic.auto._

/*
  Implement a restful endpoint which fetches a user by id from a datastore (cassandra or an sql db)
  REQ: HTTP GET /user/{id}
  RESP:
  - if user found: HTTP 200 OK '{
      "user": {
        "name":..., "id": 111..
      }
    }'
  - if not found: HTTP 404
  - use in mem sql db or embedded cassandra for unit testing the persistence api
  - for running the app, you can provide your own in memory datastore, but you can also use the embedded cassandra from tests, or in mem sql db
  - aim for code simplicity and functional style
 */

case class UserResponse(user: User)

class MyScalatraServlet extends ScalatraServlet with DBSessionSupport {

  get("/users/:id") {

    def parseID(id: String): Either[ActionResult, Int] =
      params(id).toIntOption.toRight(NotFound("Invalid ID"))

    def lookupUserWithID(id: Int): Either[ActionResult, User] =
      from(UserSchema.users)(user => where(user.id === id) select(user))
        .headOption.toRight(NotFound("There is no user with that ID"))

    val result = for {
      id <- parseID("id")
      user <- lookupUserWithID(id)
      content = UserResponse(user).asJson
    } yield Ok(content)

    result.merge
  }

}
