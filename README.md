# java-kanban
Repository for homework project.

![Java](https://img.shields.io/badge/java-21-%23ED8B00.svg?style=flat&logo=openjdk&logoColor=white)
![Intellj_Idea](https://img.shields.io/badge/IntelliJ_IDEA-v2024.2.3_Community_Edition-blue.svg?style=flat&logo=intellij-idea&logoColor=white)

# 02.12.2024
## Трекер задач
![image.png](src%2Fimg%2Fimage.png)
<details>
<summary>Спринт №4</summary>   

## Техническое задание спринта №4
### **Типы задач**[]()

Простейший кирпичик трекера — **задача** (англ. task_). У неё есть следующие свойства:

1. **Название**, кратко описывающее суть задачи (например, «Переезд»).
2. **Описание**, в котором раскрываются детали.
3. **Уникальный идентификационный номер задачи**, по которому её можно будет найти.
4. **Статус**, отображающий её прогресс. Вы будете выделять следующие этапы жизни задачи, используя `enum`:
    - `NEW` — задача только создана, но к её выполнению ещё не приступили.
    - `IN_PROGRESS` — над задачей ведётся работа.
    - `DONE` — задача выполнена.

Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на **подзадачи** (англ. subtask_). Большая задача, которая делится на подзадачи, называется **эпиком** (англ. epic_).

- Для каждой подзадачи известно, в рамках какого эпика она выполняется.
- Каждый эпик знает, какие подзадачи в него входят.
- Завершение всех подзадач эпика считается завершением эпика.
## **Менеджер**[]()
Менеджер запускается на старте программы и управлять всеми задачами.
В нём реализованы следующие функции:

    1. Хранение задачь всех типов. 
    2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
        - Получение списка всех задач.
        - Удаление всех задач.
        - Получение по идентификатору.
        - Создание. 
        - Обновление. 
        - Удаление по идентификатору.
    3. Дополнительные методы:
        - Получение списка всех подзадач определённого эпика.
    4. Управление статусами осуществляется по следующему правилу:
        Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе 
        с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, 
        в других будет рассчитывать.
    5. Для эпиков: 
        если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        во всех остальных случаях статус должен быть IN_PROGRESS.

</details>

<details>
<summary>Спринт №5</summary>

 ## Техническое задание спринта №5

1. Класс TaskManager станет интерфейсом. В нём нужно собрать список методов, которые должны быть у любого объекта-менеджера. Вспомогательные методы, если вы их создавали, переносить в интерфейс не нужно.


2. Созданный ранее класс менеджера нужно переименовать в InMemoryTaskManager. Именно то, что менеджер хранит всю информацию в оперативной памяти, и есть его главное свойство, позволяющее эффективно управлять задачами. Внутри класса должна остаться реализация методов. При этом важно не забыть имплементировать TaskManager, ведь в Java класс должен явно заявить, что он подходит под требования интерфейса.

## История просмотров задач

Добавьте в программу новую функциональность — нужно, чтобы трекер отображал последние просмотренные пользователем задачи. Для этого добавьте метод `getHistory` в `TaskManager` и реализуйте его — он должен возвращать последние 10 просмотренных задач. Просмотром будем считать вызов тех методов, которые получают задачу по идентификатору, — `getTask(int id)`, `getSubtask(int id)` и `getEpic(int id)`. От повторных просмотров избавляться не нужно.

Пример формирования истории просмотров задач после вызовов методов менеджера:

![Image (1).png](src%2Fimg%2FImage%20%281%29.png)

Утилитарный класс
Со временем в приложении трекера появится несколько реализаций интерфейса `TaskManager`. Чтобы не зависеть от реализации, создайте утилитарный класс `Managers`. На нём будет лежать вся ответственность за создание менеджера задач. То есть `Managers` должен сам подбирать нужную реализацию `TaskManager` и возвращать объект правильного типа.

У `Managers` будет метод `getDefault`. При этом вызывающему неизвестен конкретный класс — только то, что объект, который возвращает `getDefault`, реализует интерфейс `TaskManager`.

## Сделайте историю задач интерфейсом

В этом спринте возможности трекера ограничены — в истории просмотров допускается дублирование, и она может содержать только десять задач. В следующем спринте вам нужно будет убрать дубли и расширить её размер. Чтобы подготовиться к этому, проведите рефакторинг кода.

Создайте отдельный интерфейс для управления историей просмотров — `HistoryManager`. У него будет два метода: `add(Task task)` должен помечать задачи как просмотренные, а `getHistory` — возвращать их список.

Объявите класс `InMemoryHistoryManager` и перенесите в него часть кода для работы с историей из класса InMemoryTaskManager. Новый класс `InMemoryHistoryManager` должен реализовывать интерфейс `HistoryManager`.

Добавьте в служебный класс `Managers` статический метод `HistoryManager` `getDefaultHistory`. Он должен возвращать объект `InMemoryHistoryManager` — историю просмотров.

Проверьте, что теперь `InMemoryTaskManager` обращается к менеджеру истории через интерфейс `HistoryManager` и использует реализацию, которую возвращает метод `getDefaultHistory`.






#  JUnit5 Покрыть код тестами

* проверьте, что экземпляры класса Task равны друг другу, если равен их id;


* проверьте, что наследники класса Task равны друг другу, если равен их id;


* проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;


* проверьте, что объект Subtask нельзя сделать своим же эпиком;


* убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;


* проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;


* проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;


* создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер


* убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.

</details>
