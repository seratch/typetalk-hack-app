package typetalk4s

import com.m3.curly.scala._
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
case class Typetalk(
    clientKey: String,
    clientSecret: String) extends JSONStringOps {

  def accessToken(scope: String = "topic.post,topic.read", grantType: String = "client_credentials"): Option[AccessToken] = {
    val jsonBody = HTTP.post("https://typetalk.in/oauth2/access_token", Map(
      "client_id" -> clientKey,
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
    // {"topic":{"name":"Typetalk Hack Tokyo","updatedAt":"2014-04-18T04:47:01Z","description":null,"id":4611,"lastPostedAt":"2014-04-23T11:12:59Z","createdAt":"2014-04-18T04:47:01Z","suggestion":"Typetalk Hack Tokyo"},"post":{"updatedAt":"2014-04-23T11:12:59Z","talks":[],"replyTo":null,"url":"https://typetalk.in/topics/4611/posts/246927","topicId":4611,"likes":[],"links":[],"id":246927,"mention":null,"createdAt":"2014-04-23T11:12:59Z","message":"Scala からテスト","account":{"name":"seratch","updatedAt":"2014-04-23T10:18:41Z","fullName":"seratch","id":1534,"createdAt":"2014-02-04T05:57:12Z","suggestion":"seratch","imageUrl":"https://typetalk.in/accounts/1534/profile_image.png?t=1398248321512"},"attachments":[]}}
    println(HTTP.post(request).textBody())
  }

  def messages(token: AccessToken, topicId: Long): Option[Messages] = {
    val request = Request(s"https://typetalk.in/api/v1/topics/${topicId}")
      .header("Authorization", s"Bearer ${token.accessToken}")
    fromJSONString[Messages](HTTP.get(request).textBody())
  }

}
