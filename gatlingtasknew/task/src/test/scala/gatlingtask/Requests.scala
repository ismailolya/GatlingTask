package gatlingtask

import io.gatling.core.Predef.{exec, regex}
import io.gatling.http.Predef.{http, status}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Requests {

  val headers_0 = Map(
    "Cache-Control" -> "max-age=0",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "none",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_1 = Map(
    "Origin" -> "https://challenge.flood.io",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_5 = Map(
    "Accept" -> "*/*",
    "X-Requested-With" -> "XMLHttpRequest")

  val headers_8 = Map(
    "Accept" -> "text/html, application/xhtml+xml",
    "Turbolinks-Referrer" -> "https://challenge.flood.io/")


  val openHomePage = exec(http("GET Home page")
    .get("/")
    .headers(headers_0)
    .check(regex("""<title>(.+?)</title>""").is("Flood IO Script Challenge"),
      regex("""<title>(.+?)</title>""").saveAs("title"),
      regex("authenticity_token.+?value=\"(.+?)\"").find.saveAs("token"),
      regex("step_id.+?value=\"(.+?)\"").find.saveAs("stepID1")))

    val pressStartButton = exec(http("POST Start")
      .post("/start")
      .headers(headers_1)
      .formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}")
      .formParam("challenger[step_id]", "${stepID1}")
      .formParam("challenger[step_number]", "1")
      .formParam("commit", "Start")
      .check(status.is(302)))

      val getAgePage = exec(http("GET age page")
        .get("/step/2")
        .check(regex("<option value=.+?>(.*?)<").findRandom.saveAs("selectedRandomAge"))
        .check(regex("step_id.+?value=\"(.+?)\"").find.saveAs("stepID2"),
               status.is(200)))

      val selectAge = exec(http("Post age")
          .post("/start")
          .headers(headers_1)
          .formParam("utf8", "✓")
          .formParam("authenticity_token", "${token}")
          .formParam("challenger[step_id]", "${stepID2}")
          .formParam("challenger[step_number]", "2")
          .formParam("challenger[age]", "${selectedRandomAge}")
          .formParam("commit", "Next")
          .check(status.is(302)))

      val getOrdersListPage = exec(http("GET order page")
          .get("/step/3")
          .headers(headers_0)
          .check(regex("(?<=label class=\\\"collection_radio_buttons\\\".*\\\">).*[0-9]{2,3}").findAll.saveAs("OrderValue"))
          .check(regex("step_id.+?value=\"(.+?)\"").find.saveAs("stepID3"),
                 regex("order_selected.+?value=\\\"(.+?)\\\"").findAll.saveAs("OrderSelected"),
                 status.is(200)))

    val selectLargestOrder = exec(http("POST largest order")
          .post("/start")
          .headers(headers_1)
          .formParam("utf8", "✓")
          .formParam("authenticity_token", "${token}")
          .formParam("challenger[step_id]", "${stepID3}")
          .formParam("challenger[step_number]", "3")
          .formParam("challenger[largest_order]", "${returnMaxValue}")
          .formParam("challenger[order_selected]", "${returnSelectedValue}")
          .formParam("commit", "Next")
          .check(status.is(302)))


  val getContinuePage = exec(http("GET continue page")
          .get("/step/4")
          .headers(headers_0)
          .check(regex("step_id.+?value=\"(.+?)\"").find.saveAs("stepID4"),
                 regex("challenger_order.+?name=\"(.*?)\"").findAll.saveAs("challengerNameList"),
                 regex("challenger_order.+?value=\"(.*?)\"").find.saveAs("challengerOrder"),
                 status.is(200)))

      val pressContinueButton = exec(http("POST continue")
          .post("/start")
          .headers(headers_1)
          .formParam("utf8", "✓")
          .formParam("authenticity_token", "${token}")
          .formParam("challenger[step_id]", "${stepID4}")
          .formParam("challenger[step_number]", "4")
          .formParam("${challengerName_1}", "${challengerOrder}")
          .formParam("${challengerName_2}", "${challengerOrder}")
          .formParam("${challengerName_3}", "${challengerOrder}")
          .formParam("${challengerName_4}", "${challengerOrder}")
          .formParam("${challengerName_5}", "${challengerOrder}")
          .formParam("${challengerName_6}", "${challengerOrder}")
          .formParam("${challengerName_7}", "${challengerOrder}")
          .formParam("${challengerName_8}", "${challengerOrder}")
          .formParam("${challengerName_9}", "${challengerOrder}")
          .formParam("${challengerName_10}", "${challengerOrder}")
          .formParam("commit", "Next")
          .resources(http("GET accept page")
            .get("/code")
            .check(regex("[0-9]{10}").saveAs("timeToken"))
            .headers(headers_5)
            ))


      val getTimeTokenPage = exec(http("GET time token page")
          .get("/step/5")
          .headers(headers_0)
          .check(regex("step_id.+?value=\"(.+?)\"").find.saveAs("stepID5"),
                 status.is(200)))

      val enterTimeToken = exec(http("POST token")
          .post("/start")
          .headers(headers_1)
          .formParam("utf8", "✓")
          .formParam("authenticity_token", "${token}")
          .formParam("challenger[step_id]", "${stepID5}")
          .formParam("challenger[step_number]", "5")
          .formParam("challenger[one_time_token]", "${timeToken}")
          .formParam("commit", "Next")
          .check(status.is(302)))


  val getResultPage = exec(http("GET result page")
    .get("/done")
    .headers(headers_0)
    .check(status.is(200)))

  val getFindOutMoreButton = exec(http("GET Click to find out more button")
    .get("/")
    .headers(headers_8)
    .check(status.is(200)))
}
