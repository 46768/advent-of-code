#include "header/intcode_program.h"

#include <string.h>

#include "header/intcode_cpu.h"
#include "logger.h"
#include "allocator.h"

/*
typedef struct {
	unsigned int program_size;
	unsigned int instruction_pointer;
	int* init_program;
	int* program;
} Program;
*/

int load_program(Program* program, int* program_data) {
	// Program's size is at the first index
	size_t program_size = program_data[0];
	program->program_size = program_size;
	program->instruction_pointer = 0;
	program->program = (int*)malloc(program_size*sizeof(int));
	program->init_program = (int*)malloc(program_size*sizeof(int));
	memcpy(program->program, program_data+1, program_size*sizeof(int));
	memcpy(program->init_program, program_data+1, program_size*sizeof(int));

	return 0;
};

int run_program(Program* program) {
	int step_result = 0;
	while (step_result == 0) {
		step_result = step_program(program);
	}
	return 0;
};

int reset_program(Program* program) {
	memcpy(program->program, program->init_program, program->program_size*sizeof(int));
	program->instruction_pointer = 0;

	return 0;
}

int get_index_in_program(Program* program, int index) {
	if (0 <= index && index < program->program_size) {
		return program->program[index];
	}

	return -1;
}

int set_value_in_program(Program* program, int index, int value) {
	if (0 <= index && index < program->program_size) {
		program->program[index] = value;
		return 0;
	}

	return 1;
}

int free_program(Program* program) {
	deallocate(program->program);
	deallocate(program->init_program);

	return 0;
}
