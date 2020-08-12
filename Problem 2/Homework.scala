import scala.collection.Set

object Homework extends App {

  val requirement = """
###
### Story 2 - Optional - time approx 30 min
### Authorize API access depending on user security context
###

Say we have countries to store mappings like:
RO - Store1,Store2,...Store10
FR - Store1,Store2,...Store10
...
similar for DE and all Metro countries

Given an Authorization Context for a given role

// can access all stores from all countries - no restrictions
"STORE_MANAGER_ROLE": []

// can access only Store1 for RO and FR
"STORE_MANAGER_ROLE": [
   { "country": ["RO", "FR"], "store": ["Store1"] }
]

// can access only Store1 for RO and Store2 for FR
"STORE_MANAGER_ROLE": [
   { "country": ["RO"], "store": ["Store1"] },
   { "country": ["FR"], "store": ["Store2"] }
]

Implement canAccess(..) function below
- should allow access if no restrictions exist (see 1st test)
- if ctx has restriction for given resource, make sure it is properly enforced - 2nd test
- make sure restrictions are fully applied (all conditions) and not partially applied - 3rd test
- see asserts for expected behaviour
"""

  case class Resource(name: String, value: String)

  type Ctx = Map[String, Set[String]]
  type RoleCtx = Set[Ctx]

  def canAccess(rctx: RoleCtx, res: Resource*): Boolean =
    rctx.forall(_.isEmpty) || rctx.exists(ctx => res.forall(r => ctx.get(r.name).exists(set => set.contains(r.value))))

  val ro = Resource("country", "RO")
  val fr = Resource("country", "FR")
  val de = Resource("country", "DE")
  val store1 = Resource("store", "Store1")
  val store2 = Resource("store", "Store2")
  val categoryB = Resource("category", "beverages")

  {
    val ctx: RoleCtx = Set(Map())
    assert( canAccess(ctx, ro, store1), "ctx should allow")
    assert( canAccess(ctx, ro, store2), "ctx should allow")
    assert( canAccess(ctx, fr, store1), "ctx should allow")
    assert( canAccess(ctx, de, store1), "ctx should allow")
    println("ctx0 tests OK")
  }

  {
    val ctx: RoleCtx = Set(Map(
      ("country" -> Set("RO", "FR")),
      ("store" -> Set("Store1"))))
    assert( canAccess(ctx, ro, store1), "ctx should allow")
    assert(!canAccess(ctx, ro, store2), "outside ctx bounds")
    assert( canAccess(ctx, fr, store1), "ctx should allow")
    assert(!canAccess(ctx, de, store1), "outside ctx bounds")
    println("ctx1 tests OK")
  }

  {
    val ctx: RoleCtx = Set(
      Map(
        ("country" -> Set("RO")),
        ("store" -> Set("Store1"))),
      Map(
        ("country" -> Set("FR")),
        ("store" -> Set("Store2"))))
    assert( canAccess(ctx, ro, store1), "ctx should allow")
    assert(!canAccess(ctx, ro, store2), "outside ctx bounds")
    assert(!canAccess(ctx, fr, store1), "outside ctx bounds")
    assert( canAccess(ctx, fr, store2), "ctx should allow")
    assert(!canAccess(ctx, de, store1), "outside ctx bounds")
    assert(!canAccess(ctx, de, store2), "outside ctx bounds")
    println("ctx2 tests OK")
  }

  println("Done")

}
