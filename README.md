# Корпоративное приложение Myphotos.com

Учебный проект для курса http://devstudy.net/course/jee-myphotos

## Инструкция по запуску проекта в docker контейнерах

Для сборки и запуска данного проекта в docker контейнерах на компьютер необходимо установить ТОЛЬКО **docker** и **docker-compose**. 
Все необходимые для сборки и запуска программные компоненты доступны в виде docker образов на docker hub и поэтому docker подтянет их из интернета. 
(https://hub.docker.com/u/devstudy)

*Т.е. на компьютер **НЕ НУЖНО устанавливать**: git, java, maven, wildfly и postgres.*


### Настройка системы разработчика:

Установка **docker** зависит от операционной системы, поэтому на официальном сайте необходимо выбрать Вашу операционную систему и 
следуя инструкциям установить **docker** и **docker-compose**:

* [Инструкция по установке docker](https://docs.docker.com/install/#supported-platforms)
* [Инструкция по установке docker-compose](https://docs.docker.com/compose/install/#install-compose)

*FYI: Для операционных систем на базе **Ubuntu** можно использовать упрощенные команды:*

* Установка **docker** одной командой: 
~~~~
sudo apt update && sudo apt install -y docker.io && sudo systemctl start docker
~~~~
* Установка **docker-compose** одной командой: 
~~~~
sudo apt install -y curl && sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && sudo chmod +x /usr/local/bin/docker-compose
~~~~

**FYI: Самой удобной операционной системой для docker является Linux, самой неудобной - Windows!**

### Сборка и запуск проекта:

###### ! Если Вы залогинены в Вашу систему не администратором, то необходимо: (Особенно актуально для Linux пользователей):
1. Добавить текущего пользователя в группу docker:
~~~~
sudo usermod -aG docker $USER
logout
login
~~~~
2. При клонировании и сборки проекта добавлять параметр `-u 1000` после параметра `run`, который запускает docker не от имени `root` пользователя.
(`1000` - это uid Вашего пользователя)

###### 1. Клонировать github репозиторий в текущую папку используя docker образ devstudy/git:
~~~~
docker run -it --rm -v "$PWD":/opt/src/ -w /opt/src devstudy/git git clone "https://github.com/devstudy-net/myphotos.com"
~~~~
###### 2. Изменить текущую папку на корневую папку модуля 'myphotos':
~~~~
cd myphotos.com/myphotos/
~~~~
###### 3. Собрать модуль 'myphotos' с помощью maven, используя docker образ 'devstudy/maven:jdk8':
~~~~
docker run -v ~/:/home/mvn/ -it --rm -e MAVEN_CONFIG=/home/mvn/.m2 -v "$PWD":/opt/src/ -w /opt/src devstudy/maven:jdk8 mvn -Duser.home=/home/mvn clean install
~~~~
###### 4. Изменить текущую папку на корневую папку модуля 'myphotos-mdb':
~~~~
cd ../myphotos-mdb/
~~~~
###### 5. Собрать модуль 'myphotos-mdb' с помощью maven, используя docker образ 'devstudy/maven:jdk8':
~~~~
docker run -v ~/:/home/mvn/ -it --rm -e MAVEN_CONFIG=/home/mvn/.m2 -v "$PWD":/opt/src/ -w /opt/src devstudy/maven:jdk8 mvn -Duser.home=/home/mvn clean install
~~~~
###### 6. Изменить текущую папку на корневую папку модуля 'myphotos-remote-project':
~~~~
cd ../myphotos-remote-project/
~~~~
###### 7. Собрать модуль 'myphotos-remote-project' с помощью maven, используя docker образ 'devstudy/maven:jdk8':
~~~~
docker run -v ~/:/home/mvn/ -it --rm -e MAVEN_CONFIG=/home/mvn/.m2 -v "$PWD":/opt/src/ -w /opt/src devstudy/maven:jdk8 mvn -Duser.home=/home/mvn clean install
~~~~
###### 8. Изменить текущую папку на корневую папку проекта 'myphotos.com':
~~~~
cd ../
~~~~
###### 9. Создать файл '.env' в папке 'myphotos.com' и указать переменные окружения:
*(Если данный файл не создавать, то в проекте не будут работать модули **myphotos-remote**, **google** и **facebook**):*
~~~~
DOCKER_HOST_IP=${IP адрес docker хоста}

DEVSTUDY_MYPHOTOS_GOOGLE_CLIENT_ID=TODO

DEVSTUDY_MYPHOTOS_FACEBOOK_CLIENT_ID=TODO
DEVSTUDY_MYPHOTOS_FACEBOOK_SECRET=TODO
~~~~
*FYI: По-умолчанию ip адрес docker хоста равен 172.17.0.1, если у Вас другой ip, его нужно задать в переменной DOCKER_HOST_IP, если ip адрес = 172.17.0.1, то можно данную переменную не создавать*
###### 10. Сконфигурировать домен myphotos.com:
Для этого необходимо добавить в файл `hosts` следующие записи 
(т.е. при такой конфигурации домен myphotos.com и его поддомены будут сконфигурированы только на текущем компьютере)
~~~~
127.0.0.1	myphotos.com
127.0.0.1	api.myphotos.com
127.0.0.1	soap.myphotos.com
~~~~
*Местоположение файла `hosts`:*
* **Windows**: C:\Windows\System32\drivers\etc\hosts
* **Linux**: /etc/hosts
* **macOS**: /private/etc/hosts

###### 11. Собрать и запустить docker контейнеры:
~~~~
docker-compose up
~~~~
*P.S. Если сборка и запуск docker контейнеров прошли успешно в консоли последней строчкой Вы должны увидеть строку, что **myphotos-backend server успешно запустился:***

`myphotos-backend     | 14:19:41,749 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Full 10.1.0.Final (WildFly Core 2.2.0.Final) started in 17160ms - Started 1508 of 1783 services (490 services are lazy, passive or on-demand)` 
###### 12. Открыть браузер и зайти на сайт:
* Web сайт приложения (Для компьютеров и мобильных устройств): `http://myphotos.com`;
* REST API (Для интеграции по REST протоколу):
    * `http://api.myphotos.com/swagger-ui-2x/index.html` - Swagger Version 2x
    * `http://api.myphotos.com/swagger-ui-3x/index.html` - Swagger Version 3x
* SOAP endpoint (Для интеграции по SOAP протоколу)
    * `http://soap.myphotos.com/ws/` - WSDL схемы. 
* Удаленное взаимодействие (Модуль myphotos-remote-project-1.0): 
    * `http://myphotos.com/myphotos-remote-project-1.0/` - тестовый endpoint для тестирования удаленного взаимодействия
    
*FYI: Для тестирования SOAP endpoint, необходимо установить SOAP клиент, например **Wizdler** для Chrome:*

*(https://chrome.google.com/webstore/detail/wizdler/oebpmncolmhiapingjaagmapififiakb?hl=en)*
###### 13. Если нужно отлаживать проект, то по-умолчанию docker-compose открывает следующие порты:
* 5432 - для доступа к postgres базе данных, с помощью SQL клиента;
* 8080 - для доступа к Wildfly, минуя nginx;
* 9990 - для доступа к Wildfly admin панель (Администратор: admin/password);
* 8787 - для Wildfly remote debugging;
###### 14. Чтобы остановить docker контейнеры:
~~~~
Ctrl+C
~~~~
###### 15. Чтобы удалить docker контейнеры:
~~~~
docker-compose down
~~~~

### Удаление исходников проекта:

Если на сервере нужны только docker контейнеры с готовыми образами приложения, то исходники которые уже не будут использоваться при запуске проекта могут быть удалены:

###### 1. Удалить docker образ с установленным git:
~~~~
docker rmi -f devstudy/git
~~~~
###### 2. Удалить docker образ с установленным maven:
~~~~
docker rmi -f devstudy/maven:jdk8
~~~~
###### 3. Удалить все файлы проекта кроме ./docker, ./docker-compose.yml, LICENSE, NOTICE:
~~~~
rm -rf ./env ./myphotos ./myphotos-generator ./myphotos-mdb ./myphotos-remote-project ./README.md ./.git ./.gitignore
~~~~
###### 4. Удалить локальный maven репозиторий:
~~~~
rm -rf ~/.m2
~~~~

### Запуск проекта в случае наличия готовых образов в локальном docker хранилище

После удаления исходных кодов проект запускается и останавливается следующими командами:

###### 1. Запустить docker контейнеры:
~~~~
docker-compose up
~~~~
###### 2. Остановить docker контейнеры:
~~~~
Ctrl+C
~~~~
###### 3. Удалить docker контейнеры:
~~~~
docker-compose down
~~~~