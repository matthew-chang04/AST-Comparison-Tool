import networkx as nx
import json

with open("../data/clang_AST/cwe121.json") as f:
	data = json.load(f)

G = nx.DiGraph()
G.add_node(1)

for 
