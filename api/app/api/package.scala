import play.api.libs.json._
import play.api.libs.json.Json._
import vehicles._

import scala.concurrent.duration.Duration

package object api {

  // Output formats ----------------------------------------------------------------------------------------------------

  implicit val durationWrites = new Writes[Duration] {
    override def writes(o: Duration): JsValue = JsString(s"${o.toMinutes} minutes")
  }
  implicit val vehicleWrites = writes[Vehicle]

  // Input formats -----------------------------------------------------------------------------------------------------

  implicit val locationSpecReads = reads[LocationSpec]
  implicit val stopSpecReads = reads[StopSpec]
  implicit val scheduledArrivalAtSpecReads = reads[ScheduledArrivalAtSpec]
  implicit val estimatedArrivalAtOrAfterSpecReads = reads[EstimatedArrivalAtOrAfterSpec]
  implicit val specReads: Reads[Spec] = {
    case JsObject(props) if props.size == 1 =>
      val key = props.head._1
      val value = props.head._2
      key match {
        case "location" => value.validate(locationSpecReads)
        case "stop" => value.validate(stopSpecReads)
        case "scheduledArrivalAt" => value.validate(scheduledArrivalAtSpecReads)
        case "estimatedArrivalAtOrAfter" => value.validate(estimatedArrivalAtOrAfterSpecReads)
        case "and" => value.validate(andSpecReads)
      }
    case _ =>
      JsError("expected object with 1 key")
  }
  implicit val andSpecReads = reads[AndSpec]

  implicit val sortReads: Reads[Sort] = {
    case JsString("sta") => JsSuccess(SortByScheduledArrival)
    case JsString("eta") => JsSuccess(SortByEstimatedArrival)
    case _ => JsError("expected 'eta' or 'sta' for sorting")
  }

  implicit val queryReads = reads[Query]
}
