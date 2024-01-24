# NerdDemos

## This repository lets you test how Nerd is!

The compiled JAR is right here for you to test, so you can do so!  
But if you want to go the developer way...

### Note: Please *read* first!

Don't perform anything till you've had a read-through! :>

## Getting All The Code:

Once you've cloned this repository, please clone the following two repositories inside this one:

- [TheNerdProject](https://github.com/Brahvim/TheNerdProject), which is the actual engine - mostly wrappers on Processing that can be used standalone!
- [NerdExtraModules](https://github.com/Brahvim/NerdExtraModules), which has *moving parts of the engine* such as the 'scenes-and-layers' framework, the ECS, and in the future, the OpenAL, Box2D and Bullet Physics API wrappers.

You've downloaded only ~`90` MiB so far. And that's all there is! 🎉

## Dependencies

- Please go to the path where you **installed** Processing, and find inside it, `core/library`.  
Copy all of that into... let's say, `Dependencies/Processing` inside this repository's directory.

- Then find `modes/java/libraries` in that very Processing installation directory.  
...Copy all'a that into `Dependencies/Processing/libraries` (yes, with a *small* `l`)! Except...  
You could totally be saving disk space using a symlink instead for this one.  
In fact, you could do that for both of these directories!  
*That's:*

```bash
ln -s modes/java/libraries/ Dependencies/Processing/libraries
```

..on macOS and GNU-Linux. *And:*

```batch
mklink /D modes/java/libraries/ Dependencies/Processing/libraries
```

..on Microsoft Windows. Reversed paths on the latter one.

## Building And Running!

I use the Microsoft VSCode IDE to write and build Nerd, so that's what I'd recommend you to do, too.  
Feel free to deviate, though!  
I don't have a Bash/Batch script to auto-build across toolchains yet, sorry!  

What you *should* know, is that:

- I use OpenJDK 17 from <https://adoptium.net/temurin/releases/>.
- I provide an Eclipse-style `java-formatter.xml` in the `.vscode` directory.
- I use jbockle's "Format Files" VSCode extension.
- I use the "SonarLint" VSCode extension.
- I use the "cSpell" VSCode extension.

With all of that, you should be able to build and run! ^-^  
Blame me if it doesn't. I'm sorry[**!**] that I haven't tested these steps myself yet.  

*Wishing you good luck,*  
\- Brahvim 😌
