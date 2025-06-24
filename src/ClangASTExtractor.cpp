#include <memory>

#include <clang/Lex/Lexer.h>
#include <clang/AST/AST.h>
#include <clang/AST/RecursiveASTVisitor.h>
#include <clang/Frontend/FrontendAction.h>
#include <clang/Frontend/CompilerInstance.h>
#include <clang/Tooling/Tooling.h>
#include <clang/AST/ASTConsumer.h>

class parseASTVisitor : public clang::RecursiveASTVisitor<parseASTVisitor> {
private:
	ASTContext *Ctx;
	Graph outGraph;


public:

	explicit parseASTVisitor(clang::ASTContext *Ctx) : clang::Context(Ctx) {}

	bool VisitDecl(Decl *d) {
		
		/* TODO: Redo this to allow for handling differtn Decls AND Stmts)
				Need to do this because the body/sub-nodes are dependent on the current node type
		*/
		//declare init variables
		std::string kind;
		unsigned line;
		unsigned col;
		std::string tokName { "" };
		std::string sourceCode { "" };
		std::string qualType { "" };

		SourceLocation nodeLoc = d->getLocation();
		clang::SourceManager *SM = Ctx.getSourceManager();
		LangOptions langopts = Ctx.getLangOpts();

		if (!nodeLoc.isValid()) {
			return true; // Still want to visit child nodes
		} else {
			kind = d->getDeclKindName();
			
			clang::PresumedLoc ploc = SM.getPresumedLoc(nodeLoc);
			line = ploc.getLine();
			col = ploc.getCol();

			CharSourceRange sourceRange = CharSourceRange::getTokenRange(d->getBeginLoc(), d->getEndLoc());
			llvm::StringRef code = Lexer::getSourceText(sourceRange, SM, langopts, 0);
			sourceCode = code.str();

			if (NamedDecl *ND = llvm::dyn_cast<NamedDecl>(d)) {
				tokName = ND->getNameAsString();
			}

			unsigned lastId = outGraph.addNode(kind, line, col, tokName, sourceCode, qualType);
			if (
		}
	}

	bool VisitStmt(Stmt *s) {

		std::string kind;
		unsigned line;
		unsigned col;
		std::string tokName { "" };
		std::string sourceCode { "" };
		std::string qualType { "" };

		clang::SourceManager *SM = Ctx.getSourceManager();
		SourceLocation nodeLoc = s->getBeginLoc();
		LangOptions langopts = Ctx.getLangOpts();

		if (!nodeLoc.isValid()) {
			return true; 
		} else {

			if (Expr *ex = llvm::dyn_cast<Expr>(s)) {
				SourceLocation exprLoc = ex->getExprLoc();
				clang::PresumedLoc ploc = SM.getPresumedLoc(exprLoc);
				line = ploc.getLine();
				col = ploc.getCol();

				qualType = ex->getType();
				
			} else {
				clang::PresumedLoc ploc = SM.getPresumedLoc(nodeLoc);
				line = ploc.getLine();
				col = ploc.getCol();
			}

			kind = s->getStmtClassName();

			// Getting source code as a string
			CharSourceRange sourceRange = CharSourceRange::getTokenRange(s->getBeginLoc(), s->getEndLoc());
			llvm::StringRef code = Lexer::getSourceText(sourceRange, SM, langopts, 0);
			sourceCode = code.str();

			outGraph.addNode(kind, line, col, tokName, sourceCode, qualType);
		}
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


