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

	Node(const std::string& node_kind) :
		id {0}, kind {node_kind}, line {}, col {}, tokName {}, code {}, qualType {} {}		
};

struct Edge { 
	unsigned from;
	unsigned to;
};

class Graph {
	unsigned node_id = 0;
	std::vector<Node> nodes;
	std::vector<Edge> edges;

public:	
	unsigned getLastNodeID() const {
		return nodes.back().id;
	}

	unsigned addRoot() {
		if (nodes.size() != 0) {
			return -1;
		}
		nodes.push_back({"Translation_Unit_Decl"});
		return node_id++;
	}

	unsigned addNode(const std::string& kind, unsigned line, unsigned col, const std::string& tokName, std::string code, std::string qualType) { 
		nodes.push_back({node_id, kind, line, col, tokName, code, qualType});
		return node_id++;
	}

	void addEdge(unsigned from, unsigned to) {
		edges.push_back({from, to});
	}

	void exportJson(std::string &filename) {
		std::ofstream out(filename, std::ofstream::out);

		out << "{\n \"nodes:\" { \n";

		for (int i = 0; i < nodes.size(); i++) {
			out << "{ \"id\": " << nodes[i].id << ",\n";
			out << " \"kind\": " << "\"" << nodes[i].kind << "\"" << ",\n";
			out << " \"location\" : { \n";
			out << "\t \"line\" : " << nodes[i].line << ",\n";
			out << "\t \"col\" : " << nodes[i].col << "\n";
			out << "},\n";
			out << "\"code\" : " << "\"" << nodes[i].code << "\",\n";
			out << "\"additional\" : " << "{ \n";
			out << "\t\"qualType\" : \"" << nodes[i].qualType << "\",\n";
			out << "\t\"tokName\" : \"" << nodes[i].tokName << "\"\n";
			out << "}\n";
			out << "}\n"; 
		}

		out << "}" << std::endl;
		
		out << "\"edges\" : {\n";
		
		for (int i = 0; i < edges.size(); i++) {
			out << "\"" << edges[i].from << "->" << edges[i].to << "\"\n";
		}

		out << "}" << std::endl;
		
	}

};
