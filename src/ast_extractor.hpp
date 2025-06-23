#include <string>
#include <vector>
#include <map>
#include <fstream>


struct Node {
	uint32_t id;
	std::string kind;
	unsigned line;
	unsigned col;
	std::string tokName;
	std::string attributes;
	std::string qualType;

	Node(unsigned node_id, std::string node_kind, unsigned node_line, unsigned node_col, std::string node_tokName, std::string node_code, std::string node_qualType) : 
		id {node_id}, kind {node_kind}, line {node_line}, col {node_col}, tokName {node_tokName}, code {node_code}, qualType {node_qualType} {}
};

struct Edge { //contains from and to id
	int from;
	int to;
}


class Graph {
	int node_id = 0;
	std::vector<Node> nodes;
	std::vector<Edge> edges;

public:
	
	int addNode(const std::string& kind, unsigned line, unsigned col, const std::string& tokName, std::vector attributes, std::string qualType) { 
		nodes.push_back(node_id, kind, line, col, tokName, attributes, qualType);
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
