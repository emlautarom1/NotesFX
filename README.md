# Notes-FX

## Notes-FX is a cross-platform Java 11 based Desktop app built with JavaFX, SQLite and Gradle to create, edit, view and delete personal notes.

**NotesFX** is a simple CRUD application built to show the power and simplicity of the `JavaFX` framework.

For database it uses `SQLite` as driver (tested on `3.7.2`), but you can replace it for the one you like the most.

It also uses the `Bootstrap 2` theme by `dicolar`.
For more info check: https://github.com/dicolar/jbootx

This project uses `Gradle` as build, test and dependency management system.

## How to Build and Run:

For building and running the project run:

``` powershell
    $> gradle build
    $> gradle run
```

## Usage:

- Get the latest release from the `Releases`.
- Extract `Notes_FX.zip` file and go to `Notes_FX/bin`.
- Run `Notes_FX.bat`.

Default username and password are `lautaroem1` and `MYPASSWORD`

## Custom User distributions:

After running `gradle build` you can get your own `distribution` on `build/distributions`. Extract one of the compressed files and copy the generated `notes.db` to your `Notes_FX/bin` folder, otherwise there will be no default user.

## Images:

![Login](screens/login.png)

![Register New User](screens/register.png)

![Notes](screens/notes.png)