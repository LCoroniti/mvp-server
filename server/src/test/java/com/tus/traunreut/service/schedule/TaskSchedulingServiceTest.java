package com.tus.traunreut.service.schedule;

import com.tus.traunreut.AbstractIntegrationTest;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskSchedulingServiceTest extends AbstractIntegrationTest {

//    @Autowired
//    private TaskSchedulingService schedulingService;
//
//    @Autowired
//    private ScheduledTaskRepository repository;
//
//    @Autowired
//    private ApplicationEventPublisher eventPublisher;
//
//    @BeforeEach
//    void cleanDb() {
//        repository.deleteAll();
//    }
//
//    @Test
//    void testInitializeSchedulesFutureTasks() {
//        // given: a task in the future
//        LocalDateTime futureTime = LocalDateTime.now().plusSeconds(10);
//        ScheduledTask task = repository.save(new UpdateAllMatchesTask(futureTime));
//
//        // when: trigger initialization
//        eventPublisher.publishEvent(new InitializeScheduledTasksEvent());
//
//        // then: task should be scheduled (internal map contains it)
//        boolean found = schedulingService.cancelScheduledTask(task.getId());
//        assertTrue(found, "Task should be scheduled after initialization");
//    }
//
//    @Test
//    void testAddTask_schedulesTaskImmediately() {
//        // given
//        LocalDateTime futureTime = LocalDateTime.now().plusMinutes(1);
//        ScheduledTask task = new UpdateAllMatchesTask(futureTime);
//
//        // when
//        ScheduledTask saved = schedulingService.addTask(task);
//
//        // then: task saved and scheduled
//        assertNotNull(saved.getId());
//        boolean found = schedulingService.cancelScheduledTask(saved.getId());
//        assertTrue(found, "Task should be scheduled after addTask()");
//    }
//
//    @Test
//    void testCancelScheduledTask_removesFromMemory() {
//        // given
//        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
//        ScheduledTask task = schedulingService.addTask(new UpdateAllMatchesTask(time));
//
//        // when
//        boolean cancelled = schedulingService.cancelScheduledTask(task.getId());
//
//        // then
//        assertTrue(cancelled, "Task should be cancelled");
//
//        boolean existsInDb = repository.existsById(task.getId());
//        assertFalse(existsInDb, "Task should be removed from database if it was canceled");
//    }
//
//    @Test
//    void testExecuteAndRemove_executesTaskAndDeletesFromDb() {
//        // given: task saved & scheduled
//        LocalDateTime time = LocalDateTime.now().plusSeconds(2);
//        ScheduledTask task = repository.save(new UpdateAllMatchesTask(time));
//
//        // when: execute
//        schedulingService.executeAndRemove(task);
//
//        // then: task removed from DB
//        assertFalse(repository.findById(task.getId()).isPresent(), "Task should be removed from DB after execution");
//    }
//
//    @Test
//    void testUpdatedScheduledTasksFromDatabase_addsNewTask() {
//        // given: add new task directly to DB (simulate external insert)
//        LocalDateTime time = LocalDateTime.now().plusSeconds(5);
//        ScheduledTask task = repository.save(new UpdateAllMatchesTask(time));
//
//        // when: call update method
//        schedulingService.updatedScheduledTasksFromDatabase();
//
//        // then: task should be scheduled
//        boolean found = schedulingService.cancelScheduledTask(task.getId());
//        assertTrue(found, "Task added directly to DB should be scheduled after sync");
//    }
//
//    @Test
//    void testUpdatedScheduledTasksFromDatabase_cancelsRemovedTask() {
//        // given: add and schedule task
//        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
//        ScheduledTask task = schedulingService.addTask(new UpdateAllMatchesTask(time));
//
//        // then: remove task from DB to simulate external delete
//        repository.deleteById(task.getId());
//
//        // when: sync
//        schedulingService.updatedScheduledTasksFromDatabase();
//
//        // then: task should no longer be found (cancelScheduledTask should return false)
//        boolean found = schedulingService.cancelScheduledTask(task.getId());
//        assertFalse(found, "Task removed from DB should be cancelled and removed from memory");
//    }
//
//    @Test
//    void testUpdatedScheduledTasksFromDatabase_reschedulesOnExecutionTimeChange() {
//        // given: task scheduled with initial time
//        LocalDateTime initialTime = LocalDateTime.now().plusSeconds(60);
//        ScheduledTask task = repository.save(new UpdateAllMatchesTask(initialTime));
//        eventPublisher.publishEvent(new InitializeScheduledTasksEvent());
//
//        // Capture initial ScheduledTaskInfo
//        ScheduledTaskInfo initialInfo = schedulingService.getScheduledTasks().get(task.getId());
//        assertNotNull(initialInfo);
//
//        // Update execution time in DB to a new future time
//        LocalDateTime updatedTime = initialTime.plusMinutes(5);
//        task.setExecutionTime(updatedTime);
//        repository.save(task);
//
//        // when
//        schedulingService.updatedScheduledTasksFromDatabase();
//
//        // then: scheduled task future should be replaced (rescheduled)
//        ScheduledTaskInfo updatedInfo = schedulingService.getScheduledTasks().get(task.getId());
//        assertNotNull(updatedInfo);
//        assertNotEquals(initialInfo.executionTime(), updatedInfo.executionTime(),
//                "Execution time should be updated after DB sync");
//        assertEquals(updatedTime.atZone(ZoneId.of("Europe/Berlin")).toInstant(), updatedInfo.executionTime());
//    }
}
