#include <memory>

#include <clang/AST/AST.h>
#include <clang/AST/RecursiveASTVisitor.h>
#include <clang/Frontend/FrontendAction.h>
#include <clang/Frontend/CompilerInstance.h>
#include <clang/Tooling/Tooling.h>
#include <clang/AST/ASTConsumer.h>

class parseASTVisitor : public clang::RecursiveASTVisitor<parseASTVisitor> {
private:
	ASTContext *Context;
	Graph out_graph;

public:

	explicit parseASTVisitor(clang::ASTContext *Ctx) : clang::Context(Ctx) {}


	bool VisitDecl(Decl *d) {
		/* TODO: 
			
			implement a special case for NamedDecl nodes
			dump location
			
		*/
		//declare init variables
		std::string kind;
		unsigned line;
		unsigned col;
		std::string tokName { "" };
		std::vector<std::string> attributes {};
		std::string qualType { "" };

		SourceLocation nodeLoc = d->getLocation();
		if (!nodeLoc.isValid()) {
			return true; // Still want to visit child nodes
		} else {
			kind = d->getDeclKindName();
			
			clang::SourceManager *SM = Ctx.getSourceManager();
			clang::PresumedLoc ploc = SM.getPresumedLoc(nodeLoc);
			line = ploc.getLine();
			col = ploc.getCol();

			if (NamedDecl *ND = llvm::dyn_cast<NamedDecl>(d)) {
				tokName = ND->getNameAsString();
			}
		}
	}

	bool VisitStmt(Stmt *s) {
		/* TODO: grab usefu, statement info
			
			populate the attribute list with the bitfield classes 
			copy overlapping code from Decl, NOTE THAT: you have to change the getLocation to getBeginLoc

			`
		
		*/

		
		if (Expr *ex = llvm::dyn_cast<Expr>(s)) {
			/* TODO:

				Verify with joern to see the types of things they have (is it names? is it return types (probably))
				You can get almost anything (CHECK DOXYGEN)
			*/
		}
	}

	bool VisitExpr(Expr *x) {
		//TODO: expression info
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


