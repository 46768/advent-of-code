cmake_minimum_required(VERSION 3.15)

project(AOC2019 LANG C)
set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED True)
set(CMAKE_MAKE_PROGRAM make)
set(CMAKE_C_COMPILER gcc)

add_executable(AOC2019 "${PROJECT_SOURCE_DIR}/src/main.c")
