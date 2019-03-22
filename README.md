# Notes-FX

## Notes-FX is a cross-platform Java 11 based Desktop app built with JavaFX, SQLite and Gradle to create, edit, view and delete personal notes.

**NotesFX** is a simple CRUD application built to show the power and simplicity of the `JavaFX` framework.

For database it uses `SQLite` as driver (tested on `3.7.2`), but you can replace it for the one you like the most.

It also uses the `Bootstrap 2` theme by `dicolar`.
For more info check: https://github.com/dicolar/jbootx

This project uses `Gradle` as build, test and dependency management system.

## How to Build:

For building and running the project run:

``` powershell
    $> gradle build
    $> gradle run
```

## Standalone usage:

```powershell
    $> gradle fatJar
    $> mv .\build\libs\To_Do_FX.jar
    $> java -jar To_Do_FX.jar
```

You can also zip `notes.db` and `To_Do_FX.jar` together and release it as a standalone cross-platform application.

## Username and password:

Default username and password are `lautaroem1` and `MYPASSWORD`

## Images:

![Login](screens/login.png)

![Register New User](screens/register.png)

![Notes](screens/notes.png)