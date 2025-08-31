## CPG Trimmer

This plugin includes the scaffolding for a graph-editing pass as well as a logger. When used, it outputs a log file with context on the empty nodes found in a cpg (listing their Id's, type, and information about their parents and child nodes).

### Usage

Joern Installation
```
wget https://github.com/joernio/joern/releases/latest/download/joern-install.sh
chmod +x ./joern-install.sh
sudo ./joern-install.sh
```
If this fails, you can clone Joern from source (see [Joern.io](https://joern.io/))

Plugin Setup:
Download [cpg-trimmer.zip](https://github.com/matthew-chang04/AST-Comparison-Tool/cpg-trimmer/)
```
joern --add-plugin cpg-trimmer.zip
joern

     ██╗ ██████╗ ███████╗██████╗ ███╗   ██╗
     ██║██╔═══██╗██╔════╝██╔══██╗████╗  ██║
     ██║██║   ██║█████╗  ██████╔╝██╔██╗ ██║
██   ██║██║   ██║██╔══╝  ██╔══██╗██║╚██╗██║
╚█████╔╝╚██████╔╝███████╗██║  ██║██║ ╚████║
 ╚════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝
Version: 2.0.1
Type `help` to begin

joern> open("my-example-project")
joern> run.cpgtrimmer
```
This will output a .log file with details about the empty nodes in you cpg.
## CPG Trimmer

This plugin includes the scaffolding for a graph-editing pass as well as a logger. When used, it outputs a log file with context on the empty nodes found in a cpg (listing their Id's, type, and information about their parents and child nodes).

### Usage

Joern Installation
```
wget https://github.com/joernio/joern/releases/latest/download/joern-install.sh
chmod +x ./joern-install.sh
sudo ./joern-install.sh
```
If this fails, you can clone Joern from source (see [Joern.io](https://joern.io/))

Plugin Setup:
Download [cpg-trimmer.zip](https://github.com/matthew-chang04/AST-Comparison-Tool/cpg-trimmer/)
```
joern --add-plugin cpg-trimmer.zip
joern

     ██╗ ██████╗ ███████╗██████╗ ███╗   ██╗
     ██║██╔═══██╗██╔════╝██╔══██╗████╗  ██║
     ██║██║   ██║█████╗  ██████╔╝██╔██╗ ██║
██   ██║██║   ██║██╔══╝  ██╔══██╗██║╚██╗██║
╚█████╔╝╚██████╔╝███████╗██║  ██║██║ ╚████║
 ╚════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝
Version: 2.0.1
Type `help` to begin

joern> open("my-example-project")
joern> run.cpgtrimmer
```
This will output a .log file with details about the empty nodes in you cpg.
