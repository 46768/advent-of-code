# Define common libraries
add_library(ComLib STATIC
	file_reader.c
	input_manager.c
	logger.c
	allocator.c
	file_util.c
)

target_include_directories(ComLib PUBLIC
	"${CMAKE_CURRENT_SOURCE_DIR}/header"
)
