package github.bed72.testing.model

import github.bed72.core.domain.model.Event
import github.bed72.testing.model.EventFactory.FakeEvent.FakeEventOne

class EventFactory {
    fun create(event: FakeEvent) = when (event) {
        FakeEventOne -> Event(
            1,
            "https://fakeeventurl.jpg"
        )
    }

    sealed class FakeEvent {
        object FakeEventOne : FakeEvent()
    }
}