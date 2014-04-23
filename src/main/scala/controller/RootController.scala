package controller

import typetalk4s._

class RootController extends ApplicationController {

  val typetalk = Typetalk()
  val token = typetalk.accessToken().get
  val topicId = 4611

  def index = {
    set("posts", typetalk.messages(token, topicId).map(_.posts).getOrElse(Nil))
    render("/root/index")
  }

  def post = {
    params.getAs[String]("message").foreach { message =>
      typetalk.post(token, topicId, message)
    }
    set("posts", typetalk.messages(token, topicId).map(_.posts).getOrElse(Nil))
    render("/root/index")
  }

}

