# Define a solution library
add_library(Solutions STATIC
	day1.c
	day2.c
	day3.c
	day4.c
)

target_include_directories(Solutions PUBLIC
	"${CMAKE_CURRENT_SOURCE_DIR}/header"
)

target_link_libraries(Solutions PUBLIC ComLib)
