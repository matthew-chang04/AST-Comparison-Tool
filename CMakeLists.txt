cmake_minimum_required(VERSION 3.15)
project(clang_ast_parse)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -frtti")

find_package(llvm REQUIRED CONFIG)
find_package(clang REQUIRED CONFIG)

include_directories(${LLVM_INCLUDE_DIRS})
include_directories(${CLANG_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

add_executable(clang_ast_parse src/ClangASTExtractor.cpp)

target_link_libraries(clang_ast_parse 
	clangTooling 
	clangBasic 
	clangASTMatchers
	clangFrontend
	clangAST
	clangSerialization
	clangLex
	)

target_include_directories(clang_ast_parse PRIVATE
	${LLVM_INCLUDE_DIRS}
	${CLANG_INCLUDE_DIRS}
	)



