#include "header/intcode_parser.h"

#include <stdlib.h>

#include "allocator.h"
#include "file_util.h"
#include "file_reader.h"
#include "logger.h"

int* parse_intcode_file(FileData intcode_src) {
	// Initialization
	int program_capacity = 2;
	int* program_data = (int*)allocate(program_capacity*sizeof(int));
	int program_data_ptr = 1;

	// Section initialization
	size_t section_capacity = 1;
	char* section_buf = (char*)allocate(section_capacity*sizeof(char));

	// Main parser loop
	while (get_section(intcode_src.file_pointer, &section_buf, &section_capacity, ',') != -1) {
		program_data[program_data_ptr] = atoi(section_buf);
		program_data_ptr++;
		// Increase buffer size if pointer hit capacity
		if (program_data_ptr >= program_capacity) {
			program_capacity *= 2;
			program_data = (int*)reallocate(program_data, program_capacity*sizeof(int));
		}
	}
	program_data[0] = program_data_ptr-1;

	// Free section
	deallocate(section_buf);

	return program_data;
}
