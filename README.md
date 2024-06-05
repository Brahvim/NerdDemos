# NerdDemos

## This repository lets you test Nerd!

If you don't know what Nerd is, [go check it out! :D](https://github.com/Brahvim/TheNerdProject)  
The compiled JAR is *right here* for you to test without needing these build steps!  
*But if you want to go the developer way,*  

### Note: Please *finish reading* first!

Don't *perform* anything described below till you've had a good read-through!  

## Getting All The Code:

*Clone this repository.* That's `git clone https://github.com/Brahvim/NerdDemos/` on the command line, if you like to use it!  
Once you've cloned this repository, you'll have a folder!  
`cd` into that folder, and clone the following two repositories there (any order works, okay?!):  

- [TheNerdProject](https://github.com/Brahvim/TheNerdProject), which is the actual engine - mostly wrappers on Processing that can be used standalone!  
- [NerdExtraModules](https://github.com/Brahvim/NerdExtraModules), which has *moving parts of the engine* such as the 'scenes-and-layers' framework, the ECS, and in the future, the OpenAL, Box2D and Bullet Physics API wrappers.  

You've downloaded only ~`90` MiB so far. And that's all there is! 🎉  

## Dependencies

- Please go to the path where you **installed** Processing, and find inside it, `core/library`.  
...Copy all'a that into `Dependencies/Processing/libraries` (yes, with a *small* `l`)!  
*Except...*  

...You could totally be saving disk space using a symlink instead for this one.  
In fact, you should do that for all of these directories! (Unless you want to test with different versions!)  

On macOS and GNU-Linux, *that's:*  

```bash  
ln -s modes/java/libraries/ Dependencies/Processing/libraries
```  

...*And on Microsoft Windows*, it's this:  

```batch  
mklink /D modes/java/libraries/ Dependencies/Processing/libraries
```  

(Notice how you have to reverse the paths on the latter one!)  

- Then find `modes/java/libraries` in that very Processing installation directory.  
Copy (or link!) all of that into... let's say, `Dependencies/Processing`, inside this repository's directory.  

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
Blame me if this doesn't make it work. I'm sorry[**!**] that I haven't tested these steps on every configuration ever!  

*Wishing you good luck,*  
\- Brahvim 😌
