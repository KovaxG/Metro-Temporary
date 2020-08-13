package com.example.app

import DB.DBInit
import Types.User
import org.scalatest.Assertion
import org.scalatra.test.scalatest._

class MyScalatraServletTests extends ScalatraFunSuite with DBInit {

  addServlet(classOf[MyScalatraServlet], "/*")

  test("A get request with a badly formatted id should return a 400") {
    withDB(List()) { () =>
      get("/users/badness") {
        status should equal (400)
      }
    }
  }

  test("A get request with an id present in the db should return the user") {
    withDB(List(User(42, "Mark Twain"))) { () =>
      get("/users/42") {
        status should equal (200)
      }
    }
  }

  test("A get request with an id not present in the db should return a 404") {
    withDB(List(User(13, "Mark Twain"))) { () =>
      get("/users/42") {
        status should equal (404)
      }
    }
  }

  def withDB(users: List[User])(assertion: () => Assertion): Assertion = {
    configureDb()
    generateSchema(users)
    assertion()
  }

}
