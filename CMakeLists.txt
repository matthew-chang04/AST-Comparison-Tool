cmake_minimum_required(VERSION 3.15)
project(clang_ast_parse)

find_package(LLVM REQUIRED CONFIG PATHS "/opt/homebrew/opt/llvm/lib/cmake/llvm")
find_package(Clang REQUIRED CONFIG PATHS "/opt/homebrew/opt/clang/lib/cmake/clang")

include_directories(${LLVM_INCLUDE_DIRS})
include_directories(${CLANG_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

add_executable(tool ClangASTExtractor.cpp)
target_link_libraries(tool clangTooling clangBasic clangASTMatchers)


