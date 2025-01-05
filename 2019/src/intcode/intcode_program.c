#include "header/intcode_program.h"

#include <stdio.h>
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

	int* vmstdin; int stdin_ptr;
	int* vmstdout; int stdout_ptr;
} Program;
*/

int load_program(Program* program, int* program_data) {
	// Program's size is at the first index
	size_t program_size = program_data[0];
	program->program_size = program_size;
	program->instruction_pointer = 0;
	program->program = (int*)allocate(program_size*sizeof(int));
	program->init_program = (int*)allocate(program_size*sizeof(int));
	memcpy(program->program, program_data+1, program_size*sizeof(int));
	memcpy(program->init_program, program_data+1, program_size*sizeof(int));

	program->vmstdin = (int*)allocate(INTCODE_STD_SIZE*sizeof(int));
	program->stdin_ptr = 0;
	program->vmstdout = (int*)allocate(INTCODE_STD_SIZE*sizeof(int));
	program->stdout_ptr = 0;

	return 0;
};

int run_program(Program* program) {
	program->stdin_ptr = 0;
	program->stdout_ptr = 0;
	while (step_program(program) == 1 && program->instruction_pointer < program->program_size) {}
	return 0;
};

int reset_program(Program* program) {
	memcpy(program->program, program->init_program, program->program_size*sizeof(int));
	memset(program->vmstdin, 0, INTCODE_STD_SIZE*sizeof(int));
	memset(program->vmstdout, 0, INTCODE_STD_SIZE*sizeof(int));
	program->stdin_ptr = 0;
	program->stdout_ptr = 0;
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
	deallocate(program->vmstdin);
	deallocate(program->vmstdout);

	return 0;
}

int input_program(Program* program, int val) {
	program->vmstdin[program->stdin_ptr++] = val;
	return 0;
}

void flush_stdout(Program* program) {
	printf("[STDOUT]: ");
	for (int i = 0; i < program->stdout_ptr; i++) {
		printf("%d ", program->vmstdout[i]);
	}
	printf("\n");
}
