#include <memory>

#include <clang/AST/AST.h>
#include <clang/AST/RecursiveASTVisitor.h>
#include <clang/Frontend/FrontendAction.h>
#include <clang/Frontend/CompilerInstance.h>
#include <clang/Tooling/Tooling.h>
#include <clang/AST/ASTConsumer.h>

class parseASTVisitor : public clang::RecursiveASTVisitor<parseASTVisitor> {
public:

	bool visitSourceLocation(FullSourceLoc &loc) {
		if (loc.isValid()) {
			// Visit and add node to our ast_extractor graph class
		}
	}

private:
	ASTContext *Context;
}

class parseASTConsumer : public clang::ASTConsumer {
public:
	
	virtual void HandleTranslationUnit(ASTContext &Ctx) {}	
}

class parseASTAction : public clang::ASTFrontendAction {
public:
	virtual std::unique_ptr<clang::ASTConsumer> CreateASTConsumer(clang::CompilerInstance &CI, llvm::StringRef file) {
		return std::make_unique<parseASTConsumer>();
	}
};


