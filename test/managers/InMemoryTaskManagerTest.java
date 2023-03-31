package managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManagerTest() {
        super(Managers::getInMemoryMananger);
    }

}