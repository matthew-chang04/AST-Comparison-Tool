import networkx as nx
import json
import os

FILEPATH = "data/clang_ast/cwe121.json"

def populateGraph(root: dict, Graph: nx.DiGraph, root_id: int):
	
	cur_id = root_id

	if "inner" not in root:
		return
	for node in root["inner"]:
		print(os.path.abspath(node["range"]["begin"]["includedFrom"]["file"]))
		print(os.path.abspath(FILEPATH))
		if "range" in node and "includedFrom" in node["range"]["begin"] and os.path.abspath(node["range"]["begin"]["includedFrom"]["file"]) == os.path.abspath(FILEPATH) :
			cur_id += 1
			location = node["range"]["begin"]
			Graph.add_node(cur_id, type=node["kind"], loc=location)
			Graph.add_edge(root_id, cur_id)
			
		populateGraph(node, Graph, cur_id)

def graphToDot(G: nx.Graph, filename: str):

	f = open(filename, "x")
	
	for id, attr in G.nodes.data():
		print(attr)
		f.write(f"\"{str(id)}\" [label = {attr["type"]}, {attr["loc"]}<BR/>]\n")

	for fr, to in G.edges:
		f.write(f"\"{fr}\" -> \"{to}\"")

	f.close()


if __name__== "__main__":

	with open(FILEPATH) as f:
		data = json.load(f)

	G = nx.DiGraph()
	G.add_node(1, type="root", loc="")

	populateGraph(data, G, 1)
	graphToDot(G, "graphout.dot")


