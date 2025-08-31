# AST Parser and Comparison Tool
## Underlying Project
Static Analysis tools like Joern, SVF, and Fraunhofer's CPG tend to contain flaws that can inhibit program analysis. Our project, Supervised by Prof. Paria Shirani at the University of Ottawa, was to find common flaws in these generated graphs, and implement tools that can detect and fix these in generation.</p>

To highlight common issues with graphs, I implemented the **cpg-trimmer** pass that acts as a plugin to [Joern's](https://joern.io/) existing infrastructure that traverses the CPG, highlighting empty nodes, and giving context for them.

To address problems in graph generation on Joern, I implemeted a plugin that augments type information provided by program graphs. The **CPPTypeRecoveryPlugin** is a C/C++ specific plugin that recovers lost type information that Joern does not track. 

## CPG Trimmer

This plugin includes the scaffolding for a graph-editing pass as well as a logger. When used, it outputs a log file with context on the empty nodes found in a cpg (listing their Id's, type, and information about their parents and child nodes).

# Usage

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

## CPP Type Recovery Plugin

Type recognition in C/C++ is notoriously difficult. Joern uses the CDT Parser to generate it's skeleton AST for C/C++, which does not formally track type information. Despite C/C++ being rigid in terms of type constraints, the CPG fails to recognize certain types, leading to missed context, and potentially missed bugs. 

This plugin extends Joern's CPG schema by adding a pass in the c2cpg generation process. The pass analyzes C/C++ code for exisiting types, and expands this information to deduce types of mathematical operations. It also clears incorrectly parsed nodes involving type information. The pass is not meant to be all-encompassing, as the lightweight graphs are an important aspect of static analysis in Joern.

## AST Parser
**NOTE: Currently in progress, source code contains a skeleton traversal, but as of now does NOT traverse properly (See JSON to Dot file tool).**


The first step in creating a benchmark graph is normalising the Clang AST for comparisons. The parser outputs a simplified JSON file that contains the relevant edges and nodes traversed in the Clang AST. This process involves:

* Filtering through AST nodes by file location, ensuring that we omit generic AST dump content like expanded include statements.
* Finding source code associated with each node, as this will make our AST more in line with static analysis tools like Joern
* Formalizing a list of edges, rather than relying on the recursive JSON dumps from the clang AST
* 

## JSON to Dot File
  Dot files provide a very useful visualization for graphs, so transforming Clang's AST dump to a dot file with only the critical information is important. This is acheived with a simple python script that uses NetworkX to store graph information drawn from JSON. This tool is meant to allow for quick visual comparisons while more in-depth tools are created to help.
