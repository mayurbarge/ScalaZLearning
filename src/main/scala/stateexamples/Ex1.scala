package stateexamples

class Ex1 {
  case class Master(workers: Map[String, Worker])
  case class Worker(elapsed: Long, result: Vector[String])
  case class Message(workerId: String, work: String, elapsed: Long)

  import scalaz._, Scalaz._

  type WorkerState[A] = State[Worker, A]

  def update(message: Message): WorkerState[Unit] = State.modify { w =>
    w.copy(
      elapsed = w.elapsed + message.elapsed,
      result = w.result :+ message.work
    )
  }

  def getWork: WorkerState[Vector[String]] = State.gets(_.result)
  def getElapsed: WorkerState[Long] = State.gets(_.elapsed)
  def updateAndGetElapsed(message: Message): WorkerState[Long] = for {
    _ <- update(message)
    elapsed <- getElapsed
  } yield elapsed

  val workersLens: Lens[Master, Map[String, Worker]] = Lens.lensu(
    (m, ws) => m.copy(workers = ws),
    _.workers
  )

  def workerLens(workerId: String): PLens[Master, Worker] =
    workersLens.partial andThen PLens.mapVPLens(workerId)

  def updateAndGetElapsedTime(message: Message): State[Master, Option[Long]] =
    workerLens(message.workerId) %%= updateAndGetElapsed(message)
}
