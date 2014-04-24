package typetalk4s

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import skinny.Logging

class TypetalkClientSpec extends FunSpec with ShouldMatchers with Logging {

  val typetalk = Typetalk()
  val topicId = 4611

  describe("#accessToken") {
    it("should work") {
      val token = typetalk.accessToken("topic.post", "client_credentials")
      token.isDefined should equal(true)
    }
  }

  lazy val accessToken = typetalk.accessToken().get

  describe("#post") {
    it("should work") {
      // typetalk.post(accessToken, topicId, "Scala からテスト")
    }
  }

  describe("#messages") {
    it("should work") {
      logger.info(typetalk.messages(accessToken, topicId))
    }
  }

}
