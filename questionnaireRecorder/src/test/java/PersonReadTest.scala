import com.mazhangjing.recorder.bean.Person
import org.scalatest.{FlatSpec, Matchers}
import org.yaml.snakeyaml.Yaml

class PersonReadTest extends FlatSpec with Matchers{
  "yaml" should "work well" in {
    val group = new Yaml().loadAs(getClass.getClassLoader.getResourceAsStream("config.yaml"), classOf[Person])
    println(group)
  }

  "com" should "work well" in {
    def canCastToInt(in:String) = try {
      in.toInt; true
    } catch {
      case e: Throwable => e.printStackTrace(System.err)
    }
    val res = canCastToInt("2131231231232")
    println(res)
  }
}
