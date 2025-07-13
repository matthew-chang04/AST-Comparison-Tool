import networkx as nx
import json
import os
filepath = input("Enter JSON file to parse:")
FILEPATH = os.path.abspath(filepath)

sourcepath = input("Enter source code file:")
SOURCEPATH = os.path.abspath(sourcepath)

def populateGraph(root: dict, Graph: nx.DiGraph, root_id: int):
	
	cur_id = root_id

	if "inner" not in root:
		return
	for node in root["inner"]:

		if "range" in node and "includedFrom" in node["range"]["begin"] and os.path.abspath(node["range"]["begin"]["includedFrom"]["file"]) == os.path.abspath(SOURCEPATH):
			cur_id += 1
			location = node["range"]["begin"]
			Graph.add_node(cur_id, type=node["kind"], loc=location)
			Graph.add_edge(root_id, cur_id)
			
		populateGraph(node, Graph, cur_id)

def graphToDot(G: nx.Graph, filename: str):

	f = open(filename, "x")
	
	f.write("digraph {\n")
	for id, attr in G.nodes.data():
		print(attr)
		f.write(f"\"{str(id)}\" [label=\"{attr["type"]}\" location=\"{attr["loc"]}\"];\n")

	for fr, to in G.edges:
		f.write(f"\"{fr}\" -> \"{to}\"\n")

	f.write("}")
	f.close()


if __name__== "__main__":

	with open(FILEPATH) as f:
		data = json.load(f)

	G = nx.DiGraph()
	G.add_node(1, type="root", loc="")

	populateGraph(data, G, 1)
	graphToDot(G, "graphout.dot")


