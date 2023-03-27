package backend.manager

interface IStateManager {
    fun updateState(newState: State)
    fun getCurrentState(): State
}
