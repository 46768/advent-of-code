# Define common libraries
add_library(ComLib STATIC
	fileReader.c
	inputManager.c
	logger.c
)

target_include_directories(ComLib PUBLIC
	"${CMAKE_CURRENT_SOURCE_DIR}/header"
)
