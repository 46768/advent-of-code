#include "header/intcode_parser.h"

#include <stdlib.h>

#include "allocator.h"
#include "logger.h"

int* parse_intcode_string(char* intcode_string, size_t string_size) {
	// Initialization
	int program_capacity = 2;
	int* program_data = (int*)allocate(program_capacity*sizeof(int));
	int program_data_ptr = 1;

	// Section initialization
	int section_capacity = 1;
	char* section_buf = (char*)allocate(section_capacity*sizeof(char));
	int section_buf_ptr = 0;

	// Main parser loop
	for (size_t i = 0; i < string_size; i++) {
		char char_at_i = intcode_string[i];
		if ((char_at_i-48 < 0 || char_at_i-48 > 9) && (char_at_i != ',' && char_at_i != '\n')) {
			error("Unrecognized character %c at %d", char_at_i, i);
			break;
		}
		if (char_at_i == ',' || char_at_i == '\n') {
			section_buf[section_buf_ptr] = '\0';
			program_data[program_data_ptr] = atoi(section_buf);
			program_data_ptr++;
			section_buf_ptr = 0;
		} else {
			section_buf[section_buf_ptr] = char_at_i;
			section_buf_ptr++;
		}

		if (char_at_i == '\n') break;

		// Increase buffer size if pointer hit capacity
		if (program_data_ptr >= program_capacity) {
			program_capacity *= 2;
			program_data = (int*)reallocate(program_data, program_capacity*sizeof(int));
		}
		if (section_buf_ptr >= section_capacity) {
			section_capacity *= 2;
			section_buf = (char*)reallocate(section_buf, section_capacity*sizeof(int));
		}
	}

	// Parse the last section of the program
	section_buf[section_buf_ptr] = '\0';
	program_data[program_data_ptr] = atoi(section_buf);

	// Set the size at the front of data
	program_data[0] = program_data_ptr+1;

	// Free section
	deallocate(section_buf);

	return program_data;
}
