package typetalk4s

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class TypetalkClientSpec extends FunSpec with ShouldMatchers {

  val typetalk = Typetalk()
  val topicId = 4611

  describe("#accessToken") {
    it("should work") {
      val token = typetalk.accessToken("topic.post", "client_credentials")
      token.isDefined should equal(true)
    }
  }

  describe("#post") {
    it("should work") {
      // typetalk.post(topicId, "Scala からテスト")
    }
  }

  describe("#messages") {
    it("should work") {
      println(typetalk.messages(topicId))
    }
  }

}
