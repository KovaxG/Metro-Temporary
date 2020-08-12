import DB.DBInit
import com.example.app._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle with DBInit {
  override def init(context: ServletContext) {
    configureDb()
    generateSchema(List())
    context.mount(new MyScalatraServlet, "/*")
  }

  override def destroy(context: ServletContext) {
    closeDbConnection()
  }
}
