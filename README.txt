Для установки приложения необходимо:
1.  Собрать проект, указав в корневом файле pom.xml параметр jdirectory.root.directory.path,
    указывающий на корневую папку для построения дерева или указав его при сборке пректа
    например: mvn clean package -Djdirectory.root.directory.path=c:/
2.  Результирующий jdirectory.war файл или директорию jdirectory будет создан в папке
    /jdirectory-web/target