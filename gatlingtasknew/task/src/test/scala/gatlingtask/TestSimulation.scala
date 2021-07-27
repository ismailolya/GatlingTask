package gatlingtask

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import Requests._

class TestSimulation extends Simulation {

	val environment = System.getProperty("apiUrl")
	val ramp_users = Integer.getInteger("ramp_users", 5)
	val ramp_duration = Integer.getInteger("ramp_duration", 5)
	val duration = Integer.getInteger("duration")

	val httpProtocol = http
		.baseUrl("https://challenge.flood.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0")
		.disableFollowRedirect


	val th_min = 1
	val th_max = 2

	val scn = scenario("TestSimulation")
			.exec(Requests.openHomePage)
				.pause(th_min, th_max)

			.exec(Requests.pressStartButton)
		     .pause(th_min, th_max)

			.exec(Requests.getAgePage)
				.pause(th_min, th_max)

			.exec(Requests.selectAge)
				.pause(th_min, th_max)

		  .exec(Requests.getOrdersListPage)
				.pause(th_min, th_max)

		.exec(session => {

			val orderValues = session("OrderValue").as[List[String]]
			val orderSelected = session("OrderSelected").as[List[String]]
			val intOrderValues = orderValues.flatMap(_.toString.toIntOption)
			val maxOrederValueIndex = intOrderValues.zipWithIndex.maxBy(_._1)._2
			val maxOrederValue = intOrderValues(maxOrederValueIndex)
			val maxSelectedValue = orderSelected(maxOrederValueIndex)

			session.set("returnMaxValue", maxOrederValue)
				.set("returnSelectedValue", maxSelectedValue)
		} )

		.exec(Requests.selectLargestOrder)
				.pause(th_min, th_max)

		.exec(session => {

			val challengerList = session("challengerNameList").as[List[String]]

			val challengerName1 = challengerList.lift(0)
			val challengerName2 = challengerList.lift(1)
			val challengerName3 = challengerList.lift(2)
			val challengerName4 = challengerList.lift(3)
			val challengerName5 = challengerList.lift(4)
			val challengerName6 = challengerList.lift(5)
			val challengerName7 = challengerList.lift(6)
			val challengerName8 = challengerList.lift(7)
			val challengerName9 = challengerList.lift(8)
			val challengerName10 = challengerList.lift(9)

			session.set("challengerName_1", challengerName1)
				.set("challengerName_2", challengerName2)
				.set("challengerName_3", challengerName3)
				.set("challengerName_4", challengerName4)
				.set("challengerName_5", challengerName5)
				.set("challengerName_6", challengerName6)
				.set("challengerName_7", challengerName7)
				.set("challengerName_8", challengerName8)
				.set("challengerName_9", challengerName9)
				.set("challengerName_10", challengerName10)
		} )

		  .exec(Requests.getContinuePage)
				.pause(th_min, th_max)

			.exec(Requests.pressContinueButton)
				.pause(th_min, th_max)

			.exec(Requests.getTimeTokenPage)
				.pause(th_min, th_max)

			.exec(Requests.enterTimeToken)
				.pause(th_min, th_max)

			.exec(Requests.getResultPage)
				.pause(th_min, th_max)

			.exec(Requests.getFindOutMoreButton)
				.pause(th_min, th_max)

		.exec(
					session => {
						println("INFO LOG")
						println(session("title").as[String])
						println("Age")
						println(session("selectedRandomAge").as[String])
						println("maxOrederVAlue")
						println(session("returnMaxValue").as[String])
						session
					}
				)

	setUp(
		scn.inject(rampUsers(ramp_users) during(ramp_duration)))
		.assertions(
			global.successfulRequests.percent.gt(95),
			global.responseTime.max.lt(5000)
		)

		.protocols(httpProtocol)
}