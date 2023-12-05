## Задание
1. Создать сервис для работы с сессиями (Spring boot api)
2. В сервисе должны быть методы создания и получения текущей сессии по логину
3. В сессии должен быть айдишник, логин, которому принадлежит сессия и время открытия сессии
4. Сессия активна до конца текущего дня, когда она была открыта (придумать механизм очистки сессий)
5. Создать стартер, который сможет реализовать следующий функционал:
   - нужна аннотация, для того, что бы обернуть методы, которые будут реализовывать данный функционал
   - аннотация должна применятся к методам
   - одним из обязательных параметров методов, над которыми будет аннотация, должен быть объект, который будет хранить информацию о сессии(айдишник, логин, время открытия сессии)
   - еще одним их обязательных параметров методов, над которыми будет аннотация, должен быть объект, который содержит в себе логин  
   - там где висит аннотация над методом, мы должны сходить в сервис сессий, получить или создать и получить сессию, и заинжектить в наш параметр метода)
   - сделать настройку для стартера как blackList(список логинов, для которых мы не создаём сессии и выкидываем ошибку) и откуда blackList будет подтягиваться, по дефолту из пропертей, но сделать возможность расширяемости, что бы можно было передать другой источник, откуда будет подтягиваться blackList
## Модули
- [`session`](session) - отдает или создает, сохраняет и отдает объект Session(хранит в себе id, login, datetimecreate)
  - Каждый день в 00:00 удаляет сессии, созданные до этого
   ```java
    @Scheduled(cron = "@daily")
    public void deleteSessions() {
        sessionRepository.deleteAllBySessionOpenDateBefore(LocalDateTime.now());
    }
    ```
- [`starter`](starter) - выполнение 5го пункта задания
- [`person`](person) - испольхуется для демонстрации работы аннотации
## Запуск приложения
- перед запуском необходимо опубликовать в mavenlocal starter, выполнив таску publishToMavenLocal.
- заимплиментить starter в модуль person `implementation 'ru.clevertec:starter:0.0.7'`. starter подтягивает за собой
  - `api 'org.springframework.boot:spring-boot-starter-web:3.2'`
  - `api 'org.springframework.boot:spring-boot-starter-data-jpa:3.2'`
  - `api 'org.springframework.boot:spring-boot-starter-webflux:3.1.2'`
  - `api 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2'`
- выполнить [docker-compose.yaml](docker-compose.yaml) будет создано 2 бд для session и person
- запустить [session](https://github.com/MaksLaptsev/SpringBootStarter/blob/d86440722faaa0a26a8f5551e56c955143f6448e/session/src/main/java/ru/clevertec/session/SessionApplication.java#L9), после запуска liquibase создаст таблицу в бд для хранения сессий

- запустить [person](https://github.com/MaksLaptsev/SpringBootStarter/blob/d86440722faaa0a26a8f5551e56c955143f6448e/person/src/main/java/ru/clevertec/person/PersonApplication.java#L9), после запуска liquibase создаст таблицу в бд для хранения персон
## Настйрока аннотации
- [application.yaml](person/src/main/resources/application.yaml)

  ```java
  session:
    check:
      enable: true
      default:
        default-black-list:
          - "RDN"
          - "aaa"
        default-login-black-list:
          - ru.clevertec.starter.service.SessionBlackListDefault
        url: http://localhost:8081/session
  ```

  url - адрес для запросов в session
## Http запросы
- [http](person/src/main/resources/requests.http)