package backend.manager

import jakarta.inject.Singleton

/**
 * 状態管理用のクラス
 */
@Singleton
class BooksStateManager {

    // 現在の状態
    private var currentState: State = State.Initial

    // 状態を更新する関数
    fun updateState(newState: State) {
        currentState = newState
    }

    // 現在の状態を取得する関数
    fun getCurrentState(): State {
        return currentState
    }
}
