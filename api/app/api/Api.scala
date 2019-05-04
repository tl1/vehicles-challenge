package api

import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import vehicles.{Query, VehicleRepository}

class Api @Inject() (cc: ControllerComponents, vehicleRepository: VehicleRepository) extends AbstractController(cc) {

  def vehicles = Action(parse.json) { request =>
    request.body.validate[Query] match {
      case JsSuccess(query, _) => Ok(Json.toJson(vehicleRepository.get(query)))
      case x: JsError => BadRequest(JsError.toJson(x))
    }
  }

}
