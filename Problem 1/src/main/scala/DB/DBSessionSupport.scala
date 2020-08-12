package DB

import org.squeryl.Session
import org.squeryl.SessionFactory
import org.scalatra._

object DBSessionSupport {
  val key: String = {
    val name = getClass.getName
    if (name.endsWith("$")) name.dropRight(1) else name
  }
}

trait DBSessionSupport { this: ScalatraBase =>
  import DBSessionSupport._

  def dbSession: Session = request.get(key).orNull.asInstanceOf[Session]

  before() {
    request(key) = SessionFactory.newSession
    dbSession.bindToCurrentThread
  }

  after() {
    dbSession.close
    dbSession.unbindFromCurrentThread
  }

}