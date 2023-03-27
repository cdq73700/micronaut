package backend.manager

import jakarta.inject.Singleton

/**
 * 状態管理用のクラス
 */
@Singleton
class HogesStateManager : IStateManager {
    private var currentState: State = State.Initial

    override fun updateState(newState: State) {
        currentState = newState
    }

    override fun getCurrentState(): State {
        return currentState
    }
}
