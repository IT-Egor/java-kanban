# java-kanban
Repository for homework project.

## Документация к классу TaskManager

### Получить список всех задач определенного типа
`HashMap<Integer, Task> getTasks()`- вернет хэш-таблицу с ключом id задачи и значением `Task`\
`HashMap<Integer, Epic> getEpics()`- вернет хэш-таблицу с ключом id эпика и значением `Epic`\
`HashMap<Integer, Subtask> getSubtasks()`- вернет хэш-таблицу с ключом id подзадачи и значением `Subtask`

### Удаление всех задач определенного типа
Возвращают хэш-таблицу с ключом id задачи и значением тип удаляемого класса\
`HashMap<Integer, Task> clearTasks()`-удалит все `Task`\
`HashMap<Integer, Epic> clearEpics()`- удалит все `Epic` и содержащиеся в них `Subtask`\
`HashMap<Integer, Subtask> clearSubtasks()`- удалит все `Subtask` и очистит от них соответсвующее `Epic`

### Получить объект определенного типа по id
`Task findTask(int id)`\
`Epic findEpic(int id)`\
`Subtask findSubtask(int id)`

### Создание задач определенного типа
Возвращают id созданной задачи\
`int addTask(Task task)`\
`int addEpic(Epic epic)`\
`int addSubtask(Subtask subtask, int epicId)`- параметр `epicId`
 определяет в какой Epic будет добавлена подзадача.\
Метод `addSubtask()` возвращает\
-1, если `Epic` с таким `id` не существует\
-4, если подзадача уже привязана к какому-то `Epic`

### Обновление задач определенного типа
Методы возвращают id обновленной задачи и\
-1, если `id` переданной задачи не существует\
-2, если у объекта `Epic` список его подзадач не соответствует списку подзадач его обновленной версии. 
Или если у объекта `Subtask` не совпадает его содержащий `Epic` с обновленной версией
-3, если у обновленной версии и старой разные статусы

`int updateTask(Task updatedTask)`
`int updateEpic(Epic updatedEpic)`
`int updateSubtask(Subtask updatedSubtask)`

### Удаление задач определенного типа по id
Возвращают удаляемый объект или `null`, если объект не нашелся\
Методы следят за очисткой Epic и Subtask\
`Task removeTask(int id)`\
`Epic removeEpic(int id)`\
`Subtask removeSubtask(int id)`

### Изменение статуса
Возвращают обновленную версию объекта или `null`, если задачи с таким `id` не существует\
`Task setTaskStatus(int id, Status status)`\
`Subtask setSubtaskStatus(int id, Status status)`

### Получить все подзадачи эпика
`ArrayList<Integer> getEpicSubtasksIds(int epicId)`- вернет список `id` подзадач, принадлежащих эпику с указанным `id` или `null`, если `id` не существует\