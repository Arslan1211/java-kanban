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

<details>  
<summary>Спринт №6</summary>

## Техническое задание

Пришло время потренироваться и усовершенствовать код трекера задач с помощью списков и хеш-таблиц! В этом спринте вам предстоит поработать над историей просмотров: сделать её неограниченной и избавиться от повторных просмотров в ней. А ещё вы добавите в приложение больше тестов. Поехали!

## Подготавливаем ветку

В этом задании вы откажетесь от привычного процесса разработки в главной ветке. Теперь вы будете выполнять каждую проверочную работу в отдельной ветке.

Для выполнения задания текущего спринта создайте в локальном репозитории ветку с названием `sprint_6-solution`.

## Продумываем реализацию

Вернёмся к трекеру задач! Итак, вам нужно:

- Сделать историю посещений неограниченной по размеру.
- Избавиться от повторных просмотров в истории. Если какую-либо задачу посещали несколько раз, то в истории должен остаться только её последний просмотр. Предыдущий должен быть удалён.

Недостаточно реализовать код таким образом, чтобы программа проходила по всей истории просмотров и только после этого удаляла предыдущий просмотр. Ведь тогда время работы этой программы будет линейно зависеть от длины истории.

Ваша цель — реализовать функциональность так, чтобы время просмотра задачи не зависело от общего количества задач в истории.

### Интерфейс`HistoryManager`

В приложении уже есть интерфейс для управления историей просмотров — `HistoryManager`. Осталось добавить метод `void remove(int id)` для удаления задачи из просмотра и реализовать его в классе `InMemoryHistoryManager`. Добавьте вызов метода при удалении задач, чтобы они удалялись также из истории просмотров.
<details>
<summary>Подсказка: структура интерфейса HistoryManager</summary>
   
Интерфейс HistoryManager будет иметь следующую структуру.
```java 
public interface HistoryManager {
   void add(Task task);
   void remove(int id);
   List<Task> getHistory();
}
```
### Дальнейшая разработка алгоритма со связным списком и `HashMap`

Программа должна запоминать порядок вызовов метода `add`, ведь именно в этом порядке просмотры будут выстраиваться в истории. Для хранения порядка вызовов удобно использовать список.

Если какая-либо задача просматривалась несколько раз, в истории должен отобразиться только последний просмотр. Предыдущий просмотр должен быть удалён сразу после появления нового — за O(1)O(1).

Из темы о списках вы узнали, что константное время выполнения операции может гарантировать связный список `LinkedList`. Однако эта стандартная реализация в данном случае не подойдёт: удалить элементы из списка можно по индексу или по значению с помощью методов `remove`, при этом на поиск удаляемого узла тратится время. Поэтому вам предстоит написать собственную реализацию связного списка с индексом по `id` задачи.

Вариант связного списка, который мы предлагаем реализовать, должен удалять элемент из произвольного места за O(1)O(1) с одним важным условием — программа уже знает нужное место в списке и сама управляет его узлами (в отличие от `LinkedList`).

Чтобы выполнить условие, создайте стандартную `HashMap`. Её ключом будет `id` задачи, просмотр которой требуется удалить, а значением — место просмотра этой задачи в списке, то есть узел связного списка. С помощью номера задачи можно получить соответствующий ему узел связного списка и удалить его.

Чтобы реализовать узел двусвязного списка, вспомните материал урока о `LinkedList` из темы о списках.

![](https://pictures.s3.yandex.net/resources/Untitled-177_1705593404.png)

Реализация метода `getHistory` должна перекладывать задачи из связного списка в `ArrayList` для формирования ответа.

### Подсказки

<details>  
<summary>Подсказка 1</summary>

Вы уже знакомы с классом `LinkedHashMap`. Он представляет собой очень похожую реализацию контейнера, который сохраняет все полезные свойства `LinkedList` и добавляет к ним удобство индексации значений через интерфейс `Map`. Предлагаем вам реализовать аналог этого класса самостоятельно.
</details>  

<details>  
<summary>Подсказка 2</summary>

Сначала напишите свою реализацию двусвязного списка задач с методами `linkLast` и `getTasks`. `linkLast` будет добавлять задачу в конец этого списка, а `getTasks` — собирать все задачи из него в обычный `ArrayList`. Убедитесь, что решение работает. Отдельный класс для списка создавать не нужно — реализуйте его прямо в классе `InMemoryHistoryManager`. А вот отдельный класс `Node` для узла списка необходимо добавить.
</details>  

<details>  
<summary>Подсказка 3</summary>

Добавьте `private` метод `removeNode` в класс `InMemoryHistoryManager`. В качестве параметра этот метод должен принимать объект `Node` — узел связного списка — и удалять его.
</details>  

<details>  
<summary>Подсказка 4</summary>

Создайте `HashMap` — будет достаточно её стандартной реализации. В ключах будут храниться `id` задач, а в значениях `Node` — узлы связного списка. Изначально `HashMap` пустая. Она будет заполняться по мере добавления новых задач. Напишите реализацию метода `add(Task task)`. Теперь с помощью `HashMap` и метода удаления `removeNode` метод `add(Task task)` будет быстро удалять задачу из списка, если она там есть, а затем вставлять её в конец двусвязного списка. После добавления задачи не забудьте обновить значение узла в `HashMap`.
</details>  

## Совершенствуем unit-тесты

Поработайте над покрытием классов менеджера тестами. Отдельно протестируйте новую функциональность менеджера истории.

- Скорректируйте предыдущие тесты под изменившийся алгоритм хранения версий задачи.
- Проверьте, что встроенный связный список версий, а также операции добавления и удаления работают корректно.
- Покройте тестами ещё несколько функций менеджера задач, которые вы реализовали в предыдущих спринтах. Отдельно уделите внимание целостности данных:
    - ~~-  Удаляемые подзадачи не должны хранить внутри себя старые `id`.~~
      Данная формулировка некорректна, поэтому не обращаем на нее внимания🙂. Комментарий от наставника.
    - Внутри эпиков не должно оставаться неактуальных `id` подзадач.
    - С помощью сеттеров экземпляры задач позволяют изменить любое своё поле, но это может повлиять на данные внутри менеджера. Протестируйте эти кейсы и подумайте над возможными вариантами решения проблемы.

## Дополнительное задание. Реализуем пользовательский сценарий

Если у вас останется время, вы можете выполнить дополнительное задание. Реализуйте в классе `Main` опциональный пользовательский сценарий:

1. Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
2. Запросите созданные задачи несколько раз в разном порядке.
3. После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
4. Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
5. Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.

Обратите внимание, что выполнение этого задания необязательно.

Интересного вам программирования!
</details>
</details>

<details>  
<summary>Спринт №7</summary>

# Техническое задание

В этом спринте вы добавите в трекер задач ещё одну полезную функциональность. Текущая реализация хранит состояние менеджера в оперативной памяти — из-за этого после перезапуска приложения все нужные данные теряются. Решить эту проблему может такой класс менеджера, который после каждой операции будет автоматически сохранять все задачи и их состояние в специальный файл.

Вам предстоит создать вторую реализацию менеджера. У неё будет такая же система классов и интерфейсов, как и у текущей. Они будут различаться только деталями реализации методов: один хранит информацию в оперативной памяти, другой — в файле. Приступим!

## Подготавливаем ветку

Для выполнения задания этого спринта создайте в локальном репозитории ветку с названием `sprint_7-solution-in-file-manager`.

## Вторая реализация менеджера

Создайте класс `FileBackedTaskManager`. В нём вы будете прописывать логику автосохранения в файл. Этот класс, как и `InMemoryTaskManager`, должен имплементировать интерфейс менеджера `TaskManager`.

![](https://pictures.s3.yandex.net/resources/Untitled-183_1707158495.png)

Теперь нужно написать реализацию для нового класса. Если вам захочется просто скопировать код из `InMemoryTaskManager` и дополнить его в нужных местах функцией сохранения в файл, остановитесь! Старайтесь избегать дублирования кода — это признак плохого стиля.

Есть более изящное решение: можно наследовать `FileBackedTaskManager` от `InMemoryTaskManager` и получить от класса-родителя желаемую логику работы менеджера. Останется только дописать в некоторых местах вызовы метода автосохранения.

![](https://pictures.s3.yandex.net/resources/Untitled-184_1707158495.png)

### Метод автосохранения

Пусть новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле. Создайте метод `save` без параметров — он будет сохранять текущее состояние менеджера в указанный файл.

Теперь достаточно переопределить каждую модифицирующую операцию так, чтобы сначала выполнялась версия, унаследованная от предка, а затем — метод `save`. Например:

Скопировать код

JAVA

```
@Override
public void addSubtask(Subtask subtask) {
    super.addSubtask(subtask);
    save();
}
 
```

Затем нужно продумать логику метода `save`. Он должен сохранять все задачи, подзадачи и эпики.

Для удобства рекомендуем выбрать текстовый формат **CSV** (англ. _**C**omma-**S**eparated **V**alues_, «значения, разделённые запятыми»). Реализовывать полноценную работу с этим форматом не нужно: в этом задании достаточно реализовать табличный формат записи данных. Для простоты также допустим, что названия и описания задач не будут содержать кавычек или запятых, так как эти символы — служебные в CSV. Тогда файл с сохранёнными данными будет выглядеть так:

```Java
id,type,name,status,description,epic
1,TASK,Task1,NEW,Description task1,
2,EPIC,Epic2,DONE,Description epic2,
3,SUBTASK,Sub Task2,DONE,Description sub task3,2 
```

Разберём построчно:

- в первой строке через запятую перечисляются все поля задач;
- во второй, третьей строках и далее (строк будет столько, сколько задач в менеджере) находится список задач — каждая записана с новой строки.

Файл из нашего примера можно прочитать так: в трекер добавлены задача, эпик и подзадача. Эпик и подзадача просмотрены и выполнены. Задача осталась в состоянии новой и не была просмотрена.

### Подсказки

<details>  
<summary>Как сохранять задачи в файл и считывать их из него</summary>

1. Создайте `enum` с типами задач.
2. Напишите метод сохранения задачи в строку `String toString(Task task)` или переопределите базовый.
3. Напишите метод создания задачи из строки `Task fromString(String value)`.
</details>  

<details>  
<summary>Как прочитать файл</summary>

В Java есть несколько способов чтения файлов. Вы можете использовать такой:

```Java
Files.readString(file.toPath()); 
```
</details>

### Особенности работы с файлами

Исключения вида `IOException` нужно отлавливать внутри метода `save` и выкидывать собственное непроверяемое исключение `ManagerSaveException`. Благодаря этому можно не менять сигнатуру методов интерфейса менеджера.

💡 Мы исходим из того, что приложение работает в идеальных условиях. Над ним не совершаются недопустимые операции, и все его действия со средой (например, сохранение файла) завершаются успешно.

Кроме метода сохранения, создайте статический метод `static FileBackedTaskManager` `loadFromFile(File file)`, который будет восстанавливать данные менеджера из файла при запуске программы.

## Проверяем работу нового менеджера

Не забудьте убедиться, что новый менеджер задач работает так же, как предыдущий. Ещё проверьте работу сохранения и восстановления менеджера из файла (сериализацию).

Для тестирования методов, которые работают с файлами, используйте функцию создания временных файлов `File.createTempFile(…)`. Она создаёт файлы в специальных папках ОС и не захламляет каталог проекта при выполнении тестов.

В первую очередь проверьте новые методы `FileBackedTaskManager`:

- сохранение и загрузку пустого файла;
- сохранение нескольких задач;
- загрузку нескольких задач.

## Дополнительное задание. Реализуем пользовательский сценарий

Если у вас останется время, вы можете выполнить дополнительное задание. Создайте метод `static void main(String[] args)` в классе `FileBackedTaskManager` и реализуйте небольшой сценарий:

1. Заведите несколько разных задач, эпиков и подзадач.
2. Создайте новый `FileBackedTaskManager`-менеджер из этого же файла.
3. Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом.

Обратите внимание, что выполнение этого задания необязательно.

## Итог

У вас должно появиться несколько новых классов, а также новый менеджер с опцией сохранения состояния. Убедитесь, что он работает корректно, и отправляйте свой код на ревью.
</details>  