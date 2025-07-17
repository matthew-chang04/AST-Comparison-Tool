#include <clang/Lex/Lexer.h>
#include <clang/AST/AST.h>
#include <clang/AST/ASTContext.h>
#include <clang/AST/RecursiveASTVisitor.h>
#include <clang/AST/ASTConsumer.h>
#include <clang/Frontend/FrontendAction.h>
#include <clang/Tooling/Tooling.h>
#include <clang/Tooling/CommonOptionsParser.h>
#include <clang/Basic/FileEntry.h>
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <memory>
#include "ast_extractor.hpp"

using namespace clang;

class parseASTVisitor : public RecursiveASTVisitor<parseASTVisitor> {
private:
	ASTContext *Ctx;
	const SourceManager& SM;
	const LangOptions& LangOpts;
	Graph outGraph;
	std::vector<unsigned> parentStack;

public:

	explicit parseASTVisitor(ASTContext *Context) : Ctx {Context}, SM {Context->getSourceManager()}, LangOpts {Context->getLangOpts()}, outGraph {}, parentStack {} {}
	
	bool TraverseDecl(Decl *d) {
		if (!d) return true;

		parentStack.push_back(outGraph.getLastNodeID());
		bool res = RecursiveASTVisitor::TraverseDecl(d);
		parentStack.pop_back();
		return res;
	}
	

	bool TraverseStmt(Stmt *s) {
		if (!s) return true;

		parentStack.push_back(outGraph.getLastNodeID());
		bool res = RecursiveASTVisitor::TraverseStmt(s);
		parentStack.pop_back();
		return res;
	}

	bool VisitDecl(Decl *d) {
		if (d->getKind() == Decl::TranslationUnit) {
			outGraph.addRoot();
			return true;
		}

		//declare init variables
		std::string kind;
		unsigned line;
		unsigned col;
		std::string tokName { "" };
		std::string sourceCode { "" };
		std::string qualType { "" };

		SourceLocation nodeLoc = d->getLocation();

		if (nodeLoc.isValid()) {
			kind = d->getDeclKindName();
			
			PresumedLoc ploc = SM.getPresumedLoc(nodeLoc);
			line = ploc.getLine();
			col = ploc.getColumn();

			// Get source code as a string
			CharSourceRange sourceRange = CharSourceRange::getTokenRange(d->getBeginLoc(), d->getEndLoc());
			llvm::StringRef code = Lexer::getSourceText(sourceRange, SM, LangOpts, 0);
			sourceCode = code.str();

			if (NamedDecl *ND = llvm::dyn_cast<NamedDecl>(d)) {
				tokName = ND->getNameAsString();
			}

			unsigned nodeId = outGraph.addNode(kind, line, col, tokName, sourceCode, qualType);
			outGraph.addEdge(parentStack.back(), nodeId);
		}
		return true;
	}

	bool VisitStmt(Stmt *s) {

		std::string kind;
		unsigned line;
		unsigned col;
		std::string tokName { "" };
		std::string sourceCode { "" };
		std::string qualType { "" };

		SourceLocation nodeLoc = s->getBeginLoc();

		if (nodeLoc.isValid()) {
			if (Expr *ex = llvm::dyn_cast<Expr>(s)) {
				SourceLocation exprLoc = ex->getExprLoc();
				PresumedLoc ploc = SM.getPresumedLoc(exprLoc);
				line = ploc.getLine();
				col = ploc.getColumn();

				QualType qual = ex->getType();
				qualType = qual.getAsString();
				
			} else {
				PresumedLoc ploc = SM.getPresumedLoc(nodeLoc);
				line = ploc.getLine();
				col = ploc.getColumn();
			}
			kind = s->getStmtClassName();

			// Getting source code as a string
			CharSourceRange sourceRange = CharSourceRange::getTokenRange(s->getBeginLoc(), s->getEndLoc());
			llvm::StringRef code = Lexer::getSourceText(sourceRange, SM, LangOpts, 0);
			sourceCode = code.str();

			unsigned nodeId = outGraph.addNode(kind, line, col, tokName, sourceCode, qualType);
			outGraph.addEdge(parentStack.back(), nodeId);
		}
		return true;
	}

	void AstToJson() {
		auto fileRef = SM.getFileEntryRefForID(SM.getMainFileID());

		std::string outFilePrefix;
		if (fileRef) {
			FileEntryRef& fileEntry = *fileRef;

			llvm::StringRef fileName = fileEntry.getName();
			llvm::StringRef fileNameWithoutPath = llvm::sys::path::filename(fileName);
			llvm::StringRef fileWithoutExt = llvm::sys::path::stem(fileNameWithoutPath);

			outFilePrefix = fileWithoutExt.str();
		} else {
			outFilePrefix = "";
		}

		std::string filename = outFilePrefix + "_astOut.json";
		outGraph.exportJson(filename);
	}
};

class parseASTConsumer : public ASTConsumer {
public:
	
	explicit parseASTConsumer(ASTContext *Context) : Visitor{Context} {}

	virtual void HandleTranslationUnit(ASTContext &Ctx) {
		Visitor.TraverseDecl(Ctx.getTranslationUnitDecl());
		Visitor.AstToJson();
	}

private:
	parseASTVisitor Visitor;
};

class parseASTAction : public ASTFrontendAction {
public:
	virtual std::unique_ptr<ASTConsumer> CreateASTConsumer(CompilerInstance &CI, llvm::StringRef file) {
		return std::make_unique<parseASTConsumer>(&CI.getASTContext());
	}
};

/* TODO: Implement this version of main for quick testing (inputting code to the command line)
int main(int argc, char *argv[]) {
	if (argc != 2 ) {
		std::cout << "ERROR: Usage: ./test <sourcefile>";
		return -1;
	}

	std::ifstream sourceFile(argv[1]);
	if (!sourceFile.is_open()) {
		std::cout << "ERROR: Source File Invalid";
		return -1;	
	}
	
	std::stringstream buffer;
	buffer << sourceFile.rdbuf();
	std::string sourceCode = buffer.str();

	sourceFile.close();	
}
*/

int main(int argc, const char *argv[]) {
	
	if (argc != 2) {
		std::cout << "ERROR: Usage: ./generate <sourceFile>";
		return -1;
	}
	
	std::vector<std::string> sourceFiles = {argv[1]};

	std::string SDKPath = "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk";
	std::vector<std::string> flags {
		"-std=c++17", 
		"-resource-dir", "/usr/local/lib/clang/21", 
		"-isysroot", SDKPath, 
		"-isystem", SDKPath + "/usr/include/c++/v1", 
		"-isystem", SDKPath + "/usr/include/"
		};

	clang::tooling::FixedCompilationDatabase Compilations("../testfiles", flags);
	//auto OptionsParser = tooling::CommonOptionsParser::create(argc, argv, Options);
	//tooling::ClangTool Tool(OptionsParser->getCompilations(), OptionsParser->getSourcePathList());
	
	tooling::ClangTool Tool(Compilations, sourceFiles);
	return Tool.run(tooling::newFrontendActionFactory<parseASTAction>().get());
}
