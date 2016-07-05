package main.traits

trait Player {
  def sendMsgToPlayer(playerID: Int, msg: Message)
  def receiveMsgFromPlayer(playerId: Int, msg: Message)
  def receiveMsgFromServer(msg: Message)
  def getReceivedData():List[Message]
  def action:Action
}