package controller

import typetalk4s._

class RootController extends ApplicationController {

  val typetalk = Typetalk()
  val token = typetalk.accessToken().get
  val defaultTopicId: Long = 4611L

  def index = {
    val topicId = params.getAs[Long]("topic_id").getOrElse(defaultTopicId)
    set("topicId", topicId)
    set("posts", typetalk.messages(token, topicId).map(_.posts).getOrElse(Nil))
    render("/root/index")
  }

  def post = {
    val topicId = params.getAs[Long]("topic_id").getOrElse(defaultTopicId)
    set("topicId", topicId)

    params.getAs[String]("message").foreach { message =>
      typetalk.post(token, topicId, message)
    }
    set("posts", typetalk.messages(token, topicId).map(_.posts).getOrElse(Nil))
    render("/root/index")
  }

}

