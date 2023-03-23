package managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends HistoryManagerTest <InMemoryHistoryManager> {

    InMemoryHistoryManagerTest() {
        super(Managers::getDefaultHistory);
    }

}