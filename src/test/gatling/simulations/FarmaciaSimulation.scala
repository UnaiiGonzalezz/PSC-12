package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FarmaciaSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  /* ---------- escenario helpers ---------- */

  val feeder = csv("users.csv").circular // email,pass

  val login =
    exec(feed(feeder))
      .exec(
        http("Login")
          .post("/auth/login")
          .body(StringBody("""{"email":"${email}","contrasena":"${pass}"}"""))
          .check(jsonPath("$.token").saveAs("jwt"))
      )

  val listMeds =
    exec(
      http("Medicamentos paginados")
        .get("/medicamentos/pag?page=0&size=20")
        .header("Authorization", "Bearer ${jwt}")
        .check(status.is(200))
        .check(jsonPath("$.content[0].id").saveAs("medId"))
    )

  val addCart =
    exec(
      http("Añadir al carrito")
        .post("/carrito/agregar")
        .header("Authorization", "Bearer ${jwt}")
        .body(StringBody("""{"medicamentoId":${medId},"cantidad":1}"""))
        .check(status.is(200))
    )

  val checkout =
    exec(
      http("Checkout")
        .post("/carrito/checkout")
        .header("Authorization", "Bearer ${jwt}")
        .check(status.is(200))
    )

  /* ---------- Escenario principal ---------- */

  val scn = scenario("Flujo completo")
    .exec(login, listMeds, addCart, checkout)

  setUp(
    scn.inject(rampUsers(50).during(10))   // 50 usuarios en 10 s
  ).protocols(httpProtocol)
}
