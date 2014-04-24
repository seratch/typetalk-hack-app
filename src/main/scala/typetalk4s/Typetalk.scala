package typetalk4s

import com.m3.curly.scala._
import skinny.logging.Logging
import skinny.util.JSONStringOps
import org.joda.time.DateTime

object Typetalk {

  def apply(): Typetalk = {
    Typetalk(
      sys.env.get(Typetalk.TYPETALK_API_CLIENT_ID).getOrElse {
        throw new IllegalArgumentException("REQUIRED: export TYPETALK_API_CLIENT_ID=xxx")
      }, sys.env.get(Typetalk.TYPETALK_API_CLIENT_SECRET).getOrElse {
        throw new IllegalArgumentException("REQUIRED: export TYPETALK_API_CLIENT_SECRET=yyy")
      })
  }

  val TYPETALK_API_CLIENT_ID = "TYPETALK_API_CLIENT_ID"
  val TYPETALK_API_CLIENT_SECRET = "TYPETALK_API_CLIENT_SECRET"
}

case class AccessToken(accessToken: String, expiresIn: Long, scope: String, refreshToken: String, tokenType: String)
case class Messages(posts: Seq[Post])
case class Post(id: Long, account: Account, message: String, createdAt: DateTime, updatedAt: DateTime)
case class Account(id: Long, name: String, fullName: String)

case class Typetalk(clientId: String, clientSecret: String) extends JSONStringOps with Logging {

  def accessToken(scope: String = "topic.post,topic.read", grantType: String = "client_credentials"): Option[AccessToken] = {
    val jsonBody = HTTP.post("https://typetalk.in/oauth2/access_token", Map(
      "client_id" -> clientId,
      "client_secret" -> clientSecret,
      "scope" -> scope,
      "grant_type" -> grantType
    )).textBody

    fromJSONString[AccessToken](jsonBody)
  }

  def post(token: AccessToken, topicId: Long, message: String): Unit = {
    val request = Request(s"https://typetalk.in/api/v1/topics/${topicId}")
      .formParams(Map("message" -> message))
      .header("Authorization", s"Bearer ${token.accessToken}")
    val response = HTTP.post(request)
    if (response.status == 404) logger.info(s"Failed to post a message to the topic (${topicId})")
    else logger.info(HTTP.post(request).textBody())
  }

  def messages(token: AccessToken, topicId: Long): Option[Messages] = {
    val request = Request(s"https://typetalk.in/api/v1/topics/${topicId}")
      .header("Authorization", s"Bearer ${token.accessToken}")
    fromJSONString[Messages](HTTP.get(request).textBody())
  }

}
