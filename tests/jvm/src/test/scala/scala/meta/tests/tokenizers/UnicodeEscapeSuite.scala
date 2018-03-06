package scala.meta.tests.tokenizers

import scala.meta._

import java.nio.charset.StandardCharsets
import scala.meta.testkit.DiffAssertions
import org.langmeta.internal.io.InputStreamIO
import org.scalameta.logger

class UnicodeEscapeSuite extends BaseTokenizerSuite with DiffAssertions {

  def fromResource(path: String): String = 
    new String(
      InputStreamIO.readBytes(this.getClass.getClassLoader.getResourceAsStream(path)),
      StandardCharsets.UTF_8
    )

  // Read tests from external file because scalac processes string literals in source
  // @ """ s"${x}\uef17" """.length
  // res0: Int = 10
  // by reading from external file escapes like `\uef17` are represented
  // as 6 characters instead of one.
  val tests = fromResource("unicode.txt")

  // asserts that tokenize(code).syntax == code
  def checkRoundtrip(original: String): Unit = {
    test("roundtrip: " + logger.revealWhitespace(original)) {
      val tokens = tokenize(original)
      val obtained = tokens.mkString
      assertNoDiff(obtained, original)
    }
  }

  def checkOneWay(original: String): Unit = {
    test("one way " + logger.revealWhitespace(original)) {
      val obtained = 
        original.parse[Term].get match {
          case Lit.String(value) => value
        }

      assert(obtained == original)
    }
  }

  tests.lines.foreach { line =>
    checkRoundtrip(line)
  }

  val tests2 = fromResource("unicode2.txt")
  tests2.lines.foreach { line =>
    checkRoundtrip(line)
  }

  tests2.lines.foreach { line =>
    checkOneWay(line)
  }
}
