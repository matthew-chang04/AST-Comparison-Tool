import networkx as nx
import json



def populateGraph(root: dict, Graph: nx.DiGraph, root_id: int):
	
	cur_id = root_id
	for node in root["inner"]:
		cur_id += 1
		Graph.add_node(cur_id, type=node["kind"])
		Graph.add_edge(root_id, cur_id)
		createGraph(node, Graph, cur_id)

if __name__== "__main__":

	with open("../data/clang_AST/cwe121.json") as f:
		data = json.load(f)

	G = nx.DiGraph()
	G.add_node(1, type="root")

	populateGraph(data, G, 1)


