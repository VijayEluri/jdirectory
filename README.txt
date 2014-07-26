Для установки приложения необходимо:
1.  Собрать проект, указав в корневом файле pom.xml параметр jdirectory.root.directory.path,
    указывающий на корневую папку для построения дерева или указав его при сборке пректа
    например: mvn clean package -Djdirectory.root.directory.path=c:/
2.  Результирующий jdirectory.war файл или директорию jdirectory будет создан в папке
    /jdirectory-web/target

To set up the application:
1. Build the project, specifying "jdirectory.root.directory.path" parameter in the root "pom.xml" file that points to a root directory of the directory tree.
Optionally, you can provide this parameter at build time, for instance: "mvn clean package -Djdirectory.root.directory.path=c:/"
2. The resulting "jdirectory.war" file or "jdirectory" directory will be created in "/jdirectory-web/target" folder.