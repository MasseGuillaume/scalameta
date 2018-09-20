package scala.meta.tests
package inputs

import org.scalatest._

import scala.meta._

class InputFromResource extends FunSuite {
  check("read resource", "test-resource", "test resource content\n")
  check("read nested resource", "nested/my-nested-resource", "nested resource content\n")

  def check(testName: String, path: String, content: String): Unit = {
    test(testName) {
      val obtained = Input.Stream.fromResource().text
      val expected = content

      assert(obtained == expected)
    }
  }
}
