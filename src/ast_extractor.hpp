#include <string>
#include <vector>
#include <map>
#include <fstream>


struct Node {
	unsigned id;
	std::string kind;
	unsigned line;
	unsigned col;
	std::string tokName;
	std::string code;
	std::string qualType;

	Node(unsigned node_id, std::string node_kind, unsigned node_line, unsigned node_col, std::string node_tokName, std::string node_code, std::string node_qualType) : 
		id {node_id}, kind {node_kind}, line {node_line}, col {node_col}, tokName {node_tokName}, code {node_code}, qualType {node_qualType} {}

	Node(std::string& node_kind) :
		id {0}, kind {node_kind}, line {}, col {}, tokName {}, code {}, qualType {} {}

		
};

struct Edge { //contains from and to id
	unsigned from;
	unsigned to;
}


class Graph {
	unsigned node_id = 0;
	std::vector<Node> nodes;
	std::vector<Edge> edges;

public:
	
	unsigned getLastNodeID() {
		return nodes.back().id;
	}
	
	unsigned addRoot() {
		if (nodes.size() != 0) {
			return -1;
		}
		nodes.push_back({"TranslationUnitDecl"});
		return node_id++;
	}

	unsigned addNode(const std::string& kind, unsigned line, unsigned col, const std::string& tokName, std::vector attributes, std::string qualType) { 
		nodes.push_back({node_id, kind, line, col, tokName, attributes, qualType});
		return node_id++;
	}

	void addEdge(int from, int to) {
		edges.push_back({from, to});
	}

	void exportJson(std::string &filename) {
		std::ofstream out(filename);

		out << "{\n \"nodes:\" \n";

		for (int i = 0; i < nodes.size(); i++) {

		}
		
	}

}
