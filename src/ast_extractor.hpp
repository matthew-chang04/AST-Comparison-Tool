#include <string>
#include <vector>
#include <map>
#include <fstream>


struct Node {
	uint32_t id;
	std::string type;
	std::string details;
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
	
	int addNode(const std::string type&, const std::string label&) {
		nodes.push_back({node_id, type, label);
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
