package visualize
import reftree.render.{Renderer, RenderingOptions}
import reftree.diagram.Diagram
import java.nio.file.Paths

object PersonVisualize extends App{
// refer

  val renderer = Renderer(
    renderingOptions = RenderingOptions(density = 75),
    directory = Paths.get("/Users/mbn2671/Downloads", "overview")
  )
  import renderer._

  case class Person(firstName: String, age: Int)

  Diagram.sourceCodeCaption(Person("Bob", 42)).render("example")

}
