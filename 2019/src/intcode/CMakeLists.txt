# Define a solution library
add_library(IntCode STATIC
	intcode_program.c
	intcode_parser.c
	intcode_cpu.c
)

target_include_directories(Solutions PUBLIC
	"${CMAKE_CURRENT_SOURCE_DIR}/header"
)

target_link_libraries(IntCode PUBLIC ComLib)
