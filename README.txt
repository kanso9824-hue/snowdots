Snow Dots — Fabric 1.21.11 (клиентский мод)

Что нужно
- Java 21
- Fabric Loader 0.18.4+
- Fabric API 0.141.1+1.21.11 (обязательно, кладётся в папку mods рядом с модом)

Как собрать
1) Открой эту папку в IntelliJ IDEA как Gradle-проект, дождись загрузки.
   Затем: Gradle -> Tasks -> build -> build.
   Или в консоли (один раз): gradle wrapper --gradle-version 8.14.3, потом gradlew build
2) Готовый мод: build/libs/snowdots-1.0.0.jar (не файл с -sources).
3) Положи jar и Fabric API в .minecraft/mods.

Управление
- J — меню (точки на цели / зима / сила снегопада). Клавишу можно сменить в настройках управления.
- Настройки сохраняются в config/snowdots.properties.

Сборка без установки Java (через GitHub)
1) Создай новый репозиторий на github.com и загрузи в него СОДЕРЖИМОЕ папки snowdots (вместе с папкой .github).
2) Вкладка Actions -> build -> когда сборка станет зелёной, открой её и скачай артефакт snowdots-jar (внутри zip лежит jar).
