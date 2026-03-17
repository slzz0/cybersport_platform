# Lab Defense Q&A Full

Этот файл - полная, более развернутая версия шпаргалки для защиты лабораторной работы. Его удобно использовать как подробный конспект перед защитой.

## JPQL и native query

### Что такое JPQL

JPQL (`Java Persistence Query Language`) - это язык запросов поверх сущностей JPA, а не поверх таблиц базы данных.

Главная идея:
- в JPQL мы работаем с Java-сущностями и их полями
- в SQL/native query мы работаем с таблицами и колонками базы данных

Пример из лабораторной:
- `Match`
- `Tournament`
- `Game`

В моем проекте JPQL-запрос написан в [MatchRepository.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/repository/MatchRepository.java) в методе `findByFiltersJpql(...)`.

Что важно сказать на защите:
- JPQL удобен тем, что привязан к объектной модели приложения
- запрос использует связи между сущностями: `Match -> Tournament -> Game`
- если структура таблиц меняется, но объектная модель остается прежней, JPQL обычно стабильнее и удобнее сопровождать

### Что такое native query

Native query - это обычный SQL-запрос, который JPA отправляет в базу почти без преобразования.

В моем проекте native query реализован в [MatchRepository.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/repository/MatchRepository.java) в методе `findByFiltersNative(...)`.

Что важно сказать:
- native query полезен, когда нужен полный контроль над SQL
- можно использовать особенности конкретной СУБД
- он ближе к реальной схеме таблиц

### Разница JPQL и native query

JPQL:
- работает с сущностями
- использует поля классов
- более объектно-ориентирован

Native query:
- работает с таблицами и колонками БД
- дает полный контроль над SQL
- полезен для сложных или специфичных SQL-конструкций

### Как это показано в лабораторной

В лабораторной один и тот же поиск матчей реализован двумя способами:
- `findByFiltersJpql(...)`
- `findByFiltersNative(...)`

Выбор режима происходит через `queryType` в endpoint:
- `JPQL`
- `NATIVE`

Это можно показать в [MatchController.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/controller/MatchController.java) и [MatchServiceImpl.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/service/impl/MatchServiceImpl.java).

## Java Collections Framework

### Что такое Java Collections Framework

Java Collections Framework - это набор интерфейсов, реализаций и алгоритмов для работы с группами объектов.

Основная цель:
- удобно хранить данные
- быстро их искать
- изменять, сортировать, группировать

### Основные интерфейсы Collection

Самые важные интерфейсы:
- `Collection`
- `List`
- `Set`
- `Queue`
- `Deque`

Над ними отдельно стоит `Map`, но важно помнить:
- `Map` не наследуется от `Collection`
- `Map` - отдельная иерархия

### Как устроена иерархия

`Iterable`
- базовый интерфейс для перебора элементов

`Collection`
- общий интерфейс для коллекций

От `Collection` наследуются:
- `List`
- `Set`
- `Queue`

Отдельно существует:
- `Map`

### От чего наследуется Map

`Map` не наследуется от `Collection`.

Это отдельный интерфейс, потому что он хранит не просто набор элементов, а пары:
- ключ
- значение

То есть `Map<K, V>` - это отображение ключа на значение.

## ArrayList vs LinkedList

### ArrayList

`ArrayList` основан на динамическом массиве.

Плюсы:
- быстрый доступ по индексу
- хорошо подходит для чтения
- часто используется по умолчанию

Минусы:
- вставка и удаление в середине списка могут быть дорогими, потому что элементы сдвигаются

### LinkedList

`LinkedList` основан на двусвязном списке.

Плюсы:
- удобно вставлять и удалять элементы в начале или середине, если уже есть ссылка на позицию

Минусы:
- медленный доступ по индексу
- больше накладных расходов по памяти

### Когда что использовать

`ArrayList`:
- если чаще читаем данные
- если часто нужен доступ по индексу

`LinkedList`:
- если много вставок и удалений
- если индексный доступ не так важен

### Что сказать на защите

В большинстве обычных бизнес-задач чаще используют `ArrayList`, потому что чтение встречается чаще, чем сложные вставки в середину.

## Collection vs Collections

### Collection

`Collection` - это интерфейс.

Он описывает общие операции для коллекций:
- `add`
- `remove`
- `size`
- `isEmpty`

### Collections

`Collections` - это служебный utility-класс.

Он содержит статические методы:
- сортировка
- поиск
- создание неизменяемых коллекций
- обертки над коллекциями

Примеры:
- `Collections.sort(...)`
- `Collections.emptyList()`
- `Collections.unmodifiableList(...)`

### Короткая разница

`Collection` - это интерфейс.

`Collections` - это utility-класс с готовыми методами.

## HashMap, put()

### Что такое HashMap

`HashMap` - это реализация интерфейса `Map`, которая хранит данные как пары:
- ключ
- значение

Главная цель `HashMap`:
- быстро находить значение по ключу

В лабораторной это используется в [MatchSearchIndex.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/cache/MatchSearchIndex.java):
- ключ: `MatchSearchCacheKey`
- значение: `Page<MatchResponse>`

### Что делает put()

Метод `put(key, value)` кладет пару ключ-значение в `HashMap`.

Если говорить просто:
1. `HashMap` вычисляет `hashCode()` у ключа
2. по хэшу определяет внутреннюю корзину (`bucket`)
3. в этой корзине проверяет, есть ли уже такой ключ через `equals()`
4. если такого ключа нет, добавляет новую пару
5. если такой ключ уже есть, старое значение заменяется новым

Пример смысла:
- если тот же фильтр уже был, можно обновить сохраненный результат
- если фильтр новый, он просто добавляется в карту

### Как именно "ложится" значение в HashMap

Это хороший вопрос для защиты.

Важно объяснить так:
- значение не просто кладется "куда-то"
- сначала по ключу считается `hashCode()`
- на основе этого `hashCode()` выбирается место хранения
- затем через `equals()` уточняется, точно ли это тот же ключ

То есть `put()` использует не только сам объект ключа, а его:
- `hashCode()`
- `equals()`

### Почему это важно в лабораторной

У меня ключ не примитивный, а составной объект `MatchSearchCacheKey`.

Если не реализовать `equals()` и `hashCode()`, то:
- два одинаковых по смыслу запроса могли бы считаться разными ключами
- индекс начал бы работать неправильно

## equals() и hashCode() для объекта-ключа HashMap

### Зачем они нужны

Если объект используется как ключ в `HashMap`, нужно корректно определить:
- когда два ключа считаются одинаковыми
- какой у них хэш

За это отвечают:
- `equals()`
- `hashCode()`

### Логика

`hashCode()`:
- помогает быстро найти потенциальную корзину

`equals()`:
- окончательно проверяет, это тот же ключ или нет

### Важно правило

Если два объекта равны по `equals()`, у них обязательно должен быть одинаковый `hashCode()`.

### Где это в лабораторной

Показывается в [MatchSearchCacheKey.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/cache/MatchSearchCacheKey.java).

Ключ включает:
- `gameName`
- `tournamentName`
- `playedFrom`
- `playedTo`
- `page`
- `size`
- `sort`
- `queryType`

Значит одинаковые параметры запроса дадут одинаковый ключ.

## Пагинация

### Что такое пагинация

Пагинация - это разбиение большого результата на страницы.

Зачем нужна:
- не грузить сразу все данные
- уменьшить нагрузку на сервер и сеть
- удобнее работать с результатами в UI и API

### Как это сделано в лабораторной

Используется `Pageable` и `Page<T>`.

Где смотреть:
- [MatchController.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/controller/MatchController.java)
- [MatchService.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/service/MatchService.java)
- [MatchRepository.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/repository/MatchRepository.java)

Параметры пагинации:
- `page`
- `size`
- `sort`

В ответе возвращаются:
- `content`
- `totalElements`
- `totalPages`
- `number`
- `size`

## Проверяемые и непроверяемые исключения

### Проверяемые исключения

Проверяемые (`checked`) исключения:
- проверяются компилятором
- нужно либо обработать, либо объявить через `throws`

Примеры:
- `IOException`
- `SQLException`

### Непроверяемые исключения

Непроверяемые (`unchecked`) исключения:
- наследуются от `RuntimeException`
- компилятор не заставляет их явно обрабатывать

Примеры:
- `NullPointerException`
- `IllegalArgumentException`
- пользовательские `RuntimeException`

### Что используется в лабораторной

В лабораторной `NotFoundException` - это непроверяемое исключение.

Смотреть:
- [NotFoundException.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/exception/NotFoundException.java)

Почему это удобно:
- сервисный слой может выбрасывать ошибку, когда сущность не найдена
- глобальный обработчик перехватывает ее централизованно

## @ControllerAdvice, @ExceptionHandler, @Valid

### @ControllerAdvice

`@ControllerAdvice` - это глобальный механизм обработки ошибок для контроллеров.

Он позволяет:
- не писать одинаковый `try/catch` в каждом endpoint
- централизованно формировать ответы об ошибках

В лабораторной:
- [GlobalExceptionHandler.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/exception/GlobalExceptionHandler.java)

### @ExceptionHandler

`@ExceptionHandler` указывает, какое исключение должен обработать конкретный метод.

Например:
- `NotFoundException`
- `MethodArgumentNotValidException`

### @Valid

`@Valid` запускает валидацию DTO по bean validation-аннотациям.

Например:
- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Min`
- `@Size`

Как это работает:
1. запрос приходит в контроллер
2. Spring валидирует DTO
3. если данные невалидны, выбрасывается исключение
4. `@ControllerAdvice` превращает его в единый JSON ошибки

## Логирование, уровни логирования, основные библиотеки, ротация

### Зачем нужно логирование

Логирование нужно для:
- диагностики ошибок
- анализа поведения приложения
- контроля выполнения бизнес-логики

### Уровни логирования

Основные уровни:
- `TRACE`
- `DEBUG`
- `INFO`
- `WARN`
- `ERROR`

Смысл:
- `TRACE` - очень подробная отладка
- `DEBUG` - технические детали
- `INFO` - нормальная работа приложения
- `WARN` - подозрительные ситуации
- `ERROR` - ошибки

### Основные библиотеки

В Java/Spring часто используются:
- `SLF4J` - фасад логирования
- `Logback` - популярная реализация
- `Log4j2` - еще одна известная реализация

В Spring Boot по умолчанию часто используется:
- `SLF4J + Logback`

### Ротация логов

Ротация логов нужна, чтобы:
- лог-файлы не росли бесконечно
- старые логи архивировались

В лабораторной это настроено в:
- [logback-spring.xml](/home/slzzxd/cybersport_platform/src/main/resources/logback-spring.xml)

Там есть:
- уровни логирования
- консольный и файловый appender
- ротация по времени и размеру

## AOP, Aspect, JoinPoint, Advice

### Что такое AOP

AOP (`Aspect-Oriented Programming`) - это аспектно-ориентированное программирование.

Главная идея:
- вынести сквозную логику отдельно от бизнес-кода

Примеры сквозной логики:
- логирование
- безопасность
- транзакции
- метрики

### Aspect

`Aspect` - это класс, в котором описано дополнительное поведение.

В лабораторной:
- [ServiceExecutionLoggingAspect.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/aop/ServiceExecutionLoggingAspect.java)

### JoinPoint

`JoinPoint` - это точка выполнения программы, куда может быть "подключен" аспект.

В простом объяснении:
- это место, где аспект может сработать

Например:
- вызов метода сервиса

### Advice

`Advice` - это действие, которое выполняется аспектом.

Типы advice:
- `Before`
- `After`
- `AfterReturning`
- `AfterThrowing`
- `Around`

В лабораторной используется `Around`, потому что он позволяет:
- запустить код до метода
- выполнить сам метод
- запустить код после
- измерить время выполнения

## Логирование через AOP vs логирование в коде

### Логирование в коде

Это когда мы прямо в бизнес-методе пишем:
- `log.info(...)`

Плюсы:
- просто
- сразу видно в методе

Минусы:
- дублирование
- засорение бизнес-логики
- труднее сопровождать

### Логирование через AOP

Это когда логирование вынесено в отдельный аспект.

Плюсы:
- не засоряет бизнес-код
- централизованное решение
- легко применить ко многим методам сразу

Минусы:
- чуть сложнее понять новичку
- поведение не всегда видно прямо в бизнес-методе

### Что выбрано в лабораторной

В лабораторной время выполнения сервисных методов логируется через AOP.

Это правильно, потому что:
- логирование времени - типичная сквозная задача
- ее удобно вынести из сервисов

## Как в лабораторной реализован индекс и инвалидация

### Что такое in-memory индекс в этой работе

Это кэш результатов поиска, который хранится в памяти приложения.

Он сделан на:
- `HashMap<MatchSearchCacheKey, Page<MatchResponse>>`

Смотреть:
- [MatchSearchIndex.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/cache/MatchSearchIndex.java)

### Логика работы

1. приходит запрос на фильтрацию матчей
2. из параметров формируется составной ключ
3. сервис пытается найти готовый результат в `HashMap`
4. если результат найден, он возвращается из памяти
5. если результата нет, выполняется запрос в БД и результат сохраняется в индекс

### Что такое инвалидация

Инвалидация - это очистка кэша, когда данные изменились.

Зачем нужна:
- чтобы не отдавать устаревшие результаты

В лабораторной это делается через:
- `invalidateAll()`

Где смотреть:
- [MatchSearchIndex.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/cache/MatchSearchIndex.java)
- [MatchServiceImpl.java](/home/slzzxd/cybersport_platform/src/main/java/com/example/cybersport_platform/service/impl/MatchServiceImpl.java)

После:
- `create`
- `update`
- `delete`

индекс очищается.

## Короткие ответы для устной защиты

### Что такое JPQL

JPQL - это язык запросов JPA, который работает с сущностями и их полями, а не напрямую с таблицами базы данных.

### Что такое native query

Native query - это обычный SQL-запрос, который выполняется напрямую в базе.

### Что такое пагинация

Пагинация - это разбиение большого результата на страницы через `page`, `size` и `sort`.

### Что такое HashMap

`HashMap` - это структура данных для хранения пар ключ-значение с быстрым доступом по ключу.

### Зачем нужны equals() и hashCode() у ключа

Чтобы `HashMap` корректно понимал, какие ключи одинаковы, и правильно находил сохраненное значение.

### Что такое @ControllerAdvice

Это глобальный обработчик исключений для всех контроллеров приложения.

### Что делает @Valid

`@Valid` запускает автоматическую валидацию DTO перед выполнением бизнес-логики.

### Что такое AOP

AOP - это способ вынести сквозную логику, например логирование, отдельно от бизнес-кода.

### Почему логирование времени через AOP лучше

Потому что это убирает дублирование и не засоряет сервисные методы техническим кодом.
