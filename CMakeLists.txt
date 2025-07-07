cmake_minimum_required(VERSION 3.15)
project(clang_ast_parse)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_PREFIX_PATH "/opt/homebrew/opt/llvm")

find_package(llvm REQUIRED CONFIG)
find_package(clang REQUIRED CONFIG)

add_executable(clang_ast_parse src/ClangASTExtractor.cpp)

target_include_directories(clang_ast_parse PRIVATE
	${LLVM_INCLUDE_DIRS}
	${LLVM_INCLUDE_DIRS}
	)

target_link_libraries(clang_ast_parse 
	clangTooling 
	clangToolingCore
	clangFrontend
	clangFrontendTool
	clangASTMatchers
	clangAST
	clangSerialization
	clangLex
	clangBasic
	)



